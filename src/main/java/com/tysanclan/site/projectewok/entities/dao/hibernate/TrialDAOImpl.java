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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.dao.filters.TrialFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class TrialDAOImpl extends EwokHibernateDAO<Trial> implements
		com.tysanclan.site.projectewok.entities.dao.TrialDAO {

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.TrialDAO#getTrialByThread(com.tysanclan.site.projectewok.entities.ForumThread)
	 */
	@Override
	public Trial getTrialByThread(ForumThread thread) {
		Criteria criteria = getSession().createCriteria(Trial.class);

		criteria.add(Restrictions.eq("trialThread", thread));

		return (Trial) criteria.uniqueResult();
	}

	@Override
	protected Criteria createCriteria(SearchFilter<Trial> filter) {
		Criteria criteria = getSession().createCriteria(Trial.class);

		if (filter instanceof TrialFilter) {
			TrialFilter cf = (TrialFilter) filter;
			if (cf.getStartAfter() != null) {
				criteria.createAlias("trialThread", "thread");
				criteria.add(Restrictions.gt("thread.postTime",
						cf.getStartAfter()));
			}
			if (cf.getRestrained() != null) {
				criteria.add(Restrictions.eq("restrained", cf.getRestrained()));
			}
			if (cf.getAccused() != null) {
				criteria.add(Restrictions.eq("accused", cf.getAccused()));
			}

			if (!cf.isWithTrialThread()) {
				criteria.add(Restrictions.isNull("trialThread"));
			}
		}

		return criteria;
	}
}
