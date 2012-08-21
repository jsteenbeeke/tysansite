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
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.dao.filters.BugFilter;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class BugDAOImpl extends EwokHibernateDAO<Bug> implements
		com.tysanclan.site.projectewok.entities.dao.BugDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<Bug> filter) {
		Criteria criteria = getSession().createCriteria(Bug.class);

		if (filter instanceof BugFilter) {
			BugFilter cf = (BugFilter) filter;

			if (cf.getDescription() != null) {
				criteria.add(Restrictions.eq("description", cf.getDescription()));
			}
			if (!cf.getReportTypes().isEmpty()) {
				criteria.add(Restrictions.in("reportType", cf.getReportTypes()));
			}
			if (!cf.getAllowedStatus().isEmpty()) {
				criteria.add(Restrictions.in("status", cf.getAllowedStatus()));
			}
			if (cf.getExclude() != null) {
				criteria.add(Restrictions.ne("id", cf.getExclude()));
			}
		}

		return criteria;
	}
}
