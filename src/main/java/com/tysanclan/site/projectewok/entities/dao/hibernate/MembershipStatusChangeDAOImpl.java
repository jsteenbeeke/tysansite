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

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange;
import com.tysanclan.site.projectewok.entities.dao.MembershipStatusChangeDAO;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class MembershipStatusChangeDAOImpl extends
		EwokHibernateDAO<MembershipStatusChange> implements
		com.tysanclan.site.projectewok.entities.dao.MembershipStatusChangeDAO {

	@Override
	protected Criteria createCriteria(
			SearchFilter<MembershipStatusChange> filter) {
		return getSession().createCriteria(MembershipStatusChangeDAO.class);
	}

	@Override
	public SortedMap<Date, Long> getMutationsByDate(Date start, Date end) {
		Criteria criteria = getSession().createCriteria(
				MembershipStatusChange.class);

		if (start != null)
			criteria.add(Restrictions.ge("changeTime", start));

		if (end != null)
			criteria.add(Restrictions.le("changeTime", end));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("changeTime"))
				.add(Projections.sum("memberSizeMutation")));

		List<Object[]> results = listOf(criteria);

		SortedMap<Date, Long> map = new TreeMap<Date, Long>(
				new Comparator<Date>() {
					@Override
					public int compare(Date d1, Date d2) {
						return -d1.compareTo(d2);
					}
				});

		for (Object[] res : results) {
			Date date = (Date) res[0];
			Long sum = (Long) res[1];

			map.put(date, sum);
		}

		return map;
	}
}
