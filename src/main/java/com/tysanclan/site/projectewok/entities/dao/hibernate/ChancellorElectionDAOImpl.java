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
import com.tysanclan.site.projectewok.entities.ChancellorElection;
import com.tysanclan.site.projectewok.entities.dao.filters.ChancellorElectionFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ChancellorElectionDAOImpl extends EwokHibernateDAO<ChancellorElection>
		implements
		com.tysanclan.site.projectewok.entities.dao.ChancellorElectionDAO {

	@Override
	protected Criteria createCriteria(SearchFilter<ChancellorElection> filter) {
		Criteria criteria = getSession().createCriteria(
				ChancellorElection.class);

		if (filter instanceof ChancellorElectionFilter) {
			ChancellorElectionFilter f = (ChancellorElectionFilter) filter;

			if (f.getStartAfter() != null) {
				criteria.add(Restrictions.ge("start", f.getStartAfter()));
			}
			if (f.getStartBefore() != null) {
				criteria.add(Restrictions.le("start", f.getStartBefore()));
			}
			if (f.getWinner() != null) {
				criteria.add(Restrictions.eq("winner", f.getWinner()));
			} else if (f.isNoWinner()) {
				criteria.add(Restrictions.isNull("winner"));
			}
		}

		return criteria;
	}
}
