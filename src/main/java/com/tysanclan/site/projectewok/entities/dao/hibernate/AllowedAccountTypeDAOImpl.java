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
import com.tysanclan.site.projectewok.entities.AllowedAccountType;
import com.tysanclan.site.projectewok.entities.dao.filters.AllowedAccountTypeFilter;

/**
 * 
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class AllowedAccountTypeDAOImpl extends EwokHibernateDAO<AllowedAccountType>
		implements
		com.tysanclan.site.projectewok.entities.dao.AllowedAccountTypeDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<AllowedAccountType> filter) {
		Criteria criteria = getSession().createCriteria(
				AllowedAccountType.class);

		if (filter instanceof AllowedAccountTypeFilter) {
			AllowedAccountTypeFilter cf = (AllowedAccountTypeFilter) filter;
			if (cf.getGame() != null) {
				criteria.add(Restrictions.eq("game", cf.getGame()));
			}
			if (cf.getRealm() != null) {
				criteria.add(Restrictions.eq("realm", cf.getRealm()));
			}
			if (cf.getAccountType() != null) {
				criteria.add(Restrictions.eq("type", cf.getAccountType()));
			}
		}

		return criteria;
	}
}
