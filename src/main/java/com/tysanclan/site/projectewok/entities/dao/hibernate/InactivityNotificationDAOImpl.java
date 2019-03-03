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

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.InactivityNotification;
import com.tysanclan.site.projectewok.entities.InactivityNotification_;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.User_;
import com.tysanclan.site.projectewok.entities.filter.InactivityNotificationFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class InactivityNotificationDAOImpl extends
		HibernateDAO<InactivityNotification, InactivityNotificationFilter>
		implements
		com.tysanclan.site.projectewok.entities.dao.InactivityNotificationDAO {

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteNotificationForUser(User user) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<InactivityNotification> criteriaDelete = criteriaBuilder
				.createCriteriaDelete(InactivityNotification.class);
		Root<InactivityNotification> root = criteriaDelete
				.from(InactivityNotification.class);

		entityManager.createQuery(criteriaDelete.where(criteriaBuilder
				.equal(root.get(InactivityNotification_.user), user)))
				.executeUpdate();
	}

	@Override
	public List<Long> getUnnotifiedInactiveUsers() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder
				.createQuery(Long.class);

		Root<User> root = criteriaQuery.from(User.class);

		Subquery<InactivityNotification> subquery = criteriaQuery
				.subquery(InactivityNotification.class);
		Root<InactivityNotification> subqueryRoot = subquery
				.from(InactivityNotification.class);

		criteriaQuery.select(root.get(User_.id));
		criteriaQuery.where(criteriaBuilder.not(criteriaBuilder
				.exists(subquery.select(subqueryRoot).where(criteriaBuilder
						.equal(subqueryRoot.get(InactivityNotification_.user),
								root.get(User_.id))))), criteriaBuilder
				.or(getRegularMemberInactivityCriterion(criteriaBuilder, root),
						getRetiredMemberInactivityCriterion(criteriaBuilder,
								root),
						getVacationMemberInactivityCriterion(criteriaBuilder,
								root),
						getTrialMemberInactivityCriterion(criteriaBuilder,
								root)));

		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	private Predicate getRegularMemberInactivityCriterion(
			CriteriaBuilder criteriaBuilder, Root<User> userRoot) {
		return criteriaBuilder
				.and(criteriaBuilder.equal(userRoot.get(User_.retired), false),
						criteriaBuilder
								.equal(userRoot.get(User_.vacation), false),
						userRoot.get(User_.rank)
								.in(MemberUtil.getNonTrialRanks()),
						criteriaBuilder.lessThanOrEqualTo(
								userRoot.get(User_.lastAction),
								DateUtil.daysAgo(12)));
	}

	private Predicate getRetiredMemberInactivityCriterion(
			CriteriaBuilder criteriaBuilder, Root<User> userRoot) {
		return criteriaBuilder
				.and(criteriaBuilder.equal(userRoot.get(User_.retired), true),
						userRoot.get(User_.rank)
								.in(MemberUtil.getNonTrialRanks()),
						criteriaBuilder.lessThanOrEqualTo(
								userRoot.get(User_.lastAction),
								DateUtil.monthsAgo(11)));
	}

	private Predicate getVacationMemberInactivityCriterion(
			CriteriaBuilder criteriaBuilder, Root<User> userRoot) {
		return criteriaBuilder
				.and(criteriaBuilder.equal(userRoot.get(User_.retired), false),
						criteriaBuilder
								.equal(userRoot.get(User_.vacation), true),
						userRoot.get(User_.rank)
								.in(MemberUtil.getNonTrialRanks()),
						criteriaBuilder.lessThanOrEqualTo(
								userRoot.get(User_.lastAction),
								DateUtil.daysAgo(55)));
	}

	private Predicate getTrialMemberInactivityCriterion(
			CriteriaBuilder criteriaBuilder, Root<User> userRoot) {
		return criteriaBuilder.and(criteriaBuilder
				.equal(userRoot.get(User_.rank), Rank.TRIAL), criteriaBuilder
				.lessThanOrEqualTo(userRoot.get(User_.lastAction),
						DateUtil.daysAgo(5)));
	}
}
