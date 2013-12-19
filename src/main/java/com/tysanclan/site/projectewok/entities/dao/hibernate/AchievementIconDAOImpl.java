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
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.AchievementIcon;
import com.tysanclan.site.projectewok.entities.dao.filters.AchievementIconFilter;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class AchievementIconDAOImpl extends EwokHibernateDAO<AchievementIcon>
		implements
		com.tysanclan.site.projectewok.entities.dao.AchievementIconDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<AchievementIcon> filter) {
		Criteria criteria = getSession().createCriteria(AchievementIcon.class);

		if (filter instanceof AchievementIconFilter) {
			AchievementIconFilter cf = (AchievementIconFilter) filter;

			if (cf.getUnclaimed() != null) {
				if (cf.getUnclaimed()) {
					criteria.add(Restrictions.isNull("achievement"));
					criteria.add(Restrictions.isNull("proposal"));
				} else {
					Disjunction d = Restrictions.disjunction();
					d.add(Restrictions.isNotNull("achievement"));
					d.add(Restrictions.isNotNull("proposal"));
					criteria.add(d);
				}
			}
			if (cf.getCreatorOnly() != null) {
				criteria.add(Restrictions.eq("creatorOnly", cf.getCreatorOnly()));
			} else if (cf.isCreatorOnlyAsNull()) {
				criteria.add(Restrictions.isNull("creatorOnly"));
			}
			if (cf.getApproved() != null) {
				criteria.add(Restrictions.eq("approved", cf.getApproved()));
			} else if (cf.isApprovedAsNull()) {
				criteria.add(Restrictions.isNull("approved"));
			}
			if (cf.getCreator() != null) {
				criteria.add(Restrictions.eq("creator", cf.getCreator()));
			}
		}

		return criteria;
	}
}
