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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.InactivityNotification;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class InactivityNotificationDAOImpl extends
		EwokHibernateDAO<InactivityNotification> implements
		com.tysanclan.site.projectewok.entities.dao.InactivityNotificationDAO {

	@Override
	protected Criteria createCriteria(
			SearchFilter<InactivityNotification> filter) {
		Criteria criteria = getSession().createCriteria(
				InactivityNotification.class);

		// if (filter instanceof InactivityNotificationFilter) {
		// InactivityNotificationFilter cf = (InactivityNotificationFilter)
		// filter;
		// }

		return criteria;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void deleteNotificationForUser(User user) {
		String hql = "delete from InactivityNotification where user = :user";
		getSession().createQuery(hql).setEntity("user", user).executeUpdate();
	}

	@Override
	public List<Long> getUnnotifiedInactiveUsers() {
		Criteria criteria = getSession().createCriteria(User.class);

		DetachedCriteria dc = DetachedCriteria
				.forClass(InactivityNotification.class);
		dc.setProjection(Projections.property("user"));

		criteria.add(Subqueries.propertyNotIn("id", dc));

		Disjunction d = Restrictions.disjunction();

		d.add(getTrialMemberInactivityCriterion());
		d.add(getVacationMemberInactivityCriterion());
		d.add(getRetiredMemberInactivityCriterion());
		d.add(getRegularMemberInactivityCriterion());

		criteria.add(d);

		criteria.setProjection(Projections.property("id"));

		List<Long> ids = listOf(criteria);

		return ids;
	}

	private Criterion getRegularMemberInactivityCriterion() {
		Conjunction c = Restrictions.conjunction();

		c.add(Restrictions.eq("retired", false));
		c.add(Restrictions.eq("vacation", false));
		c.add(Restrictions.in("rank", MemberUtil.getNonTrialRanks()));
		c.add(Restrictions.le("lastAction", DateUtil.daysAgo(12)));

		return c;
	}

	private Criterion getRetiredMemberInactivityCriterion() {
		Conjunction c = Restrictions.conjunction();

		c.add(Restrictions.eq("retired", true));
		c.add(Restrictions.in("rank", MemberUtil.getNonTrialRanks()));
		c.add(Restrictions.le("lastAction", DateUtil.monthsAgo(11)));

		return c;
	}

	private Criterion getVacationMemberInactivityCriterion() {
		Conjunction c = Restrictions.conjunction();

		c.add(Restrictions.eq("retired", false));
		c.add(Restrictions.eq("vacation", true));
		c.add(Restrictions.in("rank", MemberUtil.getNonTrialRanks()));
		c.add(Restrictions.le("lastAction", DateUtil.daysAgo(55)));

		return c;
	}

	private Criterion getTrialMemberInactivityCriterion() {
		Conjunction c = Restrictions.conjunction();

		c.add(Restrictions.eq("rank", Rank.TRIAL));
		c.add(Restrictions.le("lastAction", DateUtil.daysAgo(5)));

		return c;
	}
}
