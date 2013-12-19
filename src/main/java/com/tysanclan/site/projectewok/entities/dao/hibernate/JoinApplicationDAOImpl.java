/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.dao.filters.JoinApplicationFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class JoinApplicationDAOImpl extends EwokHibernateDAO<JoinApplication>
		implements
		com.tysanclan.site.projectewok.entities.dao.JoinApplicationDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<JoinApplication> filter) {
		Criteria criteria = getSession().createCriteria(JoinApplication.class);

		if (filter instanceof JoinApplicationFilter) {
			JoinApplicationFilter jaf = (JoinApplicationFilter) filter;

			if (jaf.getApplicant() != null) {
				criteria.add(Restrictions.eq("applicant", jaf.getApplicant()));
			}
			if (jaf.getMentor() != null) {
				criteria.add(Restrictions.eq("mentor", jaf.getMentor()));
			}
			if (jaf.getJoinThread() != null) {
				criteria.add(Restrictions.eq("joinThread", jaf.getJoinThread()));
			}

			if (jaf.getDateBefore() != null) {
				criteria.add(Restrictions.lt("startDate", jaf.getDateBefore()));
			}
		}

		return criteria;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.JoinApplicationDAO#getJoinApplicationByThread(com.tysanclan.site.projectewok.entities.ForumThread)
	 */
	@Override
	public JoinApplication getJoinApplicationByThread(ForumThread thread) {
		Criteria criteria = getSession().createCriteria(JoinApplication.class);
		criteria.add(Restrictions.eq("joinThread", thread));
		criteria.setFetchSize(1);

		return (JoinApplication) criteria.uniqueResult();
	}

}
