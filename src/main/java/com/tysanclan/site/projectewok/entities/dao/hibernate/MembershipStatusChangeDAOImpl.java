/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange_;
import com.tysanclan.site.projectewok.entities.filter.MembershipStatusChangeFilter;
import io.vavr.collection.Array;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class MembershipStatusChangeDAOImpl extends
		HibernateDAO<MembershipStatusChange, MembershipStatusChangeFilter>
		implements
		com.tysanclan.site.projectewok.entities.dao.MembershipStatusChangeDAO {

	@Override
	public SortedMap<Date, Long> getMutationsByDate(Date start, Date end) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);
		Root<MembershipStatusChange> root = query
				.from(MembershipStatusChange.class);

		query.select(criteriaBuilder
				.tuple(root.get(MembershipStatusChange_.changeTime),
						criteriaBuilder.sum(root
								.get(MembershipStatusChange_.memberSizeMutation))));

		Array<Predicate> predicates = Array.empty();

		if (start != null) {
			predicates = predicates.append(criteriaBuilder.greaterThanOrEqualTo(
					root.get(MembershipStatusChange_.changeTime), start));
		}

		if (end != null) {
			predicates = predicates.append(criteriaBuilder.lessThanOrEqualTo(
					root.get(MembershipStatusChange_.changeTime), end));
		}

		List<Tuple> results = entityManager.createQuery(
				query.where(predicates.toJavaArray(Predicate.class)))
				.getResultList();

		SortedMap<Date, Long> map = new TreeMap<Date, Long>(
				(d1, d2) -> -d1.compareTo(d2));

		for (Tuple res : results) {
			Date date = (Date) res.get(0);
			Long sum = (Long) res.get(1);

			map.put(date, sum);
		}

		return map;
	}
}
