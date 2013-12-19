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

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.dao.filters.ExpenseFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ExpenseDAOImpl extends EwokHibernateDAO<Expense> implements
		com.tysanclan.site.projectewok.entities.dao.ExpenseDAO {

	@Override
	protected Criteria createCriteria(SearchFilter<Expense> filter) {
		Criteria criteria = getSession().createCriteria(Expense.class);

		if (filter instanceof ExpenseFilter) {
			ExpenseFilter eFilter = (ExpenseFilter) filter;
			if (eFilter.getFrom() != null) {
				criteria.add(Restrictions.or(
						Restrictions.ge("end", eFilter.getFrom()),
						Restrictions.isNull("end")));
			}
			if (eFilter.getTo() != null) {
				criteria.add(Restrictions.or(
						Restrictions.le("start", eFilter.getTo()),
						Restrictions.isNull("start")));
			}
		}

		criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);

		return criteria;
	}

	@Override
	public List<Expense> getActiveExpenses() {
		Criteria criteria = getSession().createCriteria(Expense.class);

		criteria.add(Restrictions.or(Restrictions.ge("end", new Date()),
				Restrictions.isNull("end")));

		return listOf(criteria);
	}
}
