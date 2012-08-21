/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.entities.dao.hibernate;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.AcceptanceVote;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.TruthsayerNomination;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerNominationDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class UserDAOImpl extends EwokHibernateDAO<User> implements
		com.tysanclan.site.projectewok.entities.dao.UserDAO {
	@Autowired
	private TruthsayerNominationDAO truthsayerNominationDAO;

	/**
	 * @param truthsayerNominationDAO
	 *            the truthsayerNominationDAO to set
	 */
	public void setTruthsayerNominationDAO(
			TruthsayerNominationDAO truthsayerNominationDAO) {
		this.truthsayerNominationDAO = truthsayerNominationDAO;
	}

	@Override
	public int countByRank(Rank rank) {
		Criteria crit = getSession().createCriteria(User.class);
		crit.add(Restrictions.eq("rank", rank));
		crit.add(Restrictions.eq("retired", false));
		crit.setProjection(Projections.rowCount());

		return ((Number) crit.uniqueResult()).intValue();
	}

	@Override
	public User load(String username, String password) {
		Criteria crit = getSession().createCriteria(User.class);

		crit.add(Restrictions.eq("username", username).ignoreCase());
		crit.add(Restrictions.eq("password", MemberUtil.hashPassword(password)));
		return (User) crit.uniqueResult();
	}

	/**
	 * @see com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO#createCriteria(com.tysanclan.site.projectewok.dataaccess.SearchFilter)
	 */
	@Override
	protected Criteria createCriteria(SearchFilter<User> filter) {
		Criteria criteria = getSession().createCriteria(User.class);

		if (filter instanceof UserFilter) {
			UserFilter userFilter = (UserFilter) filter;
			if (userFilter.getGroup() != null) {
				criteria.createCriteria("groups").add(
						Restrictions.eq("group_id", userFilter.getGroup()
								.getId()));
			}
			if (userFilter.getUsername() != null) {
				criteria.add(Restrictions.eq("username",
						userFilter.getUsername()).ignoreCase());
			}
			if (userFilter.getRanks() != null) {
				criteria.add(Restrictions.in("rank", userFilter.getRanks()));
			}
			if (userFilter.getPassword() != null) {
				criteria.add(Restrictions.eq("password",
						userFilter.getPassword()));
			}
			if (userFilter.getActiveSince() != null) {
				criteria.add(Restrictions.ge("lastAction",
						userFilter.getActiveSince()));
			}
			if (userFilter.getActiveBefore() != null) {
				criteria.add(Restrictions.lt("lastAction",
						userFilter.getActiveBefore()));
			}
			if (userFilter.getEmail() != null) {
				criteria.add(Restrictions.eq("eMail", userFilter.getEmail()));
			}
			if (userFilter.getRetired() != null) {
				criteria.add(Restrictions.eq("retired", userFilter.getRetired()));
			}
			if (userFilter.getVacation() != null) {
				criteria.add(Restrictions.eq("vacation",
						userFilter.getVacation()));
			}
			if (userFilter.getRealm() != null) {
				Criteria sc = criteria.createCriteria("playedGames");
				sc.add(Restrictions.eq("realm", userFilter.getRealm()));
			}

			if (userFilter.getBugReportMaster() != null) {
				criteria.add(Restrictions.eq("bugReportMaster",
						userFilter.getBugReportMaster()));
			}

			if (userFilter.getTruthsayerNominated() != null) {
				List<TruthsayerNomination> nominated = truthsayerNominationDAO
						.findAll();
				Set<Long> userids = new HashSet<Long>();
				for (TruthsayerNomination nomination : nominated) {
					userids.add(nomination.getUser().getId());
				}

				if (userFilter.getTruthsayerNominated()) {
					criteria.add(userids.isEmpty() ? Restrictions
							.sqlRestriction(" 1 = 2") : Restrictions.in("id",
							userids));
				} else {
					if (!userids.isEmpty()) {
						criteria.add(Restrictions.not(Restrictions.in("id",
								userids)));
					}
				}
			}
		}

		return criteria;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.UserDAO#getTrialMembersReadyForVote()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getTrialMembersReadyForVote() {
		Criteria criteria = getSession().createCriteria(User.class);

		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.DAY_OF_YEAR, -14);

		criteria.add(Restrictions.eq("rank", Rank.TRIAL));
		criteria.add(Restrictions.le("joinDate", cal.getTime()));

		Criteria reverse = getSession().createCriteria(AcceptanceVote.class);
		reverse.createAlias("trialMember", "user");
		reverse.setProjection(Projections.property("user.id"));

		List<?> activeVotes = reverse.list();

		if (!activeVotes.isEmpty()) {
			criteria.add(Restrictions.not(Restrictions.in("id", activeVotes)));
		}

		return criteria.list();
	}
}
