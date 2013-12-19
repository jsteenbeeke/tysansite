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
import com.tysanclan.site.projectewok.entities.MobileUserAgent;
import com.tysanclan.site.projectewok.entities.dao.filters.MobileUserAgentFilter;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class MobileUserAgentDAOImpl extends EwokHibernateDAO<MobileUserAgent>
		implements
		com.tysanclan.site.projectewok.entities.dao.MobileUserAgentDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<MobileUserAgent> filter) {
		Criteria criteria = getSession().createCriteria(MobileUserAgent.class);

		if (filter instanceof MobileUserAgentFilter) {
			MobileUserAgentFilter cf = (MobileUserAgentFilter) filter;

			if (cf.getMobile() != null) {
				criteria.add(Restrictions.eq("mobile", cf.getMobile()));
			} else if (cf.isSearchUnknownType()) {
				criteria.add(Restrictions.isNull("mobile"));
			}

			if (cf.getIdentifier() != null) {
				criteria.add(Restrictions.eq("identifier", cf.getIdentifier()));
			}
		}

		return criteria;
	}
}
