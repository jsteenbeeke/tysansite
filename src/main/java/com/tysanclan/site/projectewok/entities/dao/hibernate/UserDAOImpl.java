/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.events.IEventDispatcher;
import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.rest.api.util.HashException;
import com.tysanclan.site.projectewok.entities.AcceptanceVote;
import com.tysanclan.site.projectewok.entities.AcceptanceVote_;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.User_;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.event.RankChangeEvent;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.Date;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class UserDAOImpl extends HibernateDAO<User, UserFilter> implements
		com.tysanclan.site.projectewok.entities.dao.UserDAO {

	@Autowired
	private IEventDispatcher dispatcher;

	public void setDispatcher(IEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}


	@Override
	public long countByRank(Rank rank) {
		UserFilter filter = new UserFilter();
		filter.rank(rank);
		filter.retired(false);

		return countByFilter(filter);
	}

	@Override
	public List<User> findByRank(Rank rank) {
		UserFilter filter = new UserFilter();
		filter.rank(rank);

		return findByFilter(filter).toJavaList();
	}

	@Override
	public User load(String username, String password) {
		try {
			UserFilter filter = new UserFilter();
			filter.username().equalsIgnoreCase(username);
			filter.password(MemberUtil.hashPassword(password));

			return getUniqueByFilter(filter).getOrNull();
		} catch (HashException e) {
			return null;
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.UserDAO#getTrialMembersReadyForVote()
	 */
	@Override
	public List<User> getTrialMembersReadyForVote() {
		Date fourteenDaysAgo = DateUtil.daysAgo(14);

		UserFilter filter = new UserFilter();
		filter.rank(Rank.TRIAL);
		filter.joinDate().lessThanOrEqualTo(fourteenDaysAgo);

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
		Root<User> root = query.from(User.class);

		Subquery<Long> subquery = query.subquery(Long.class);
		Root<AcceptanceVote> acceptanceVoteRoot = subquery.from(AcceptanceVote.class);

		subquery.select(acceptanceVoteRoot.get(AcceptanceVote_.trialMember).get(User_.id));

		query.select(root).where(criteriaBuilder.not(root.get(User_.id).in(subquery)),
								 criteriaBuilder.equal(root.get(User_.rank), Rank.TRIAL),
								 criteriaBuilder.lessThanOrEqualTo(root.get(User_.joinDate), fourteenDaysAgo)
		);


		return entityManager.createQuery(query).getResultList();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(User object) {
		Rank oldRank = object.getOldRank();
		Rank newRank = object.getRank();

		if (oldRank != null && newRank != null && oldRank != newRank) {
			object.clearOldRank();

			dispatcher.dispatchEvent(new RankChangeEvent(object));
		}

		super.update(object);
	}
}
