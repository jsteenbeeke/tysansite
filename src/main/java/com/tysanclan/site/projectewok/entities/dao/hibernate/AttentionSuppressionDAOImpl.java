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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.AttentionSuppression;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.AttentionSuppressionDAO;

@Component
@Scope("request")
class AttentionSuppressionDAOImpl extends
		EwokHibernateDAO<AttentionSuppression> implements
		AttentionSuppressionDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<AttentionSuppression> filter) {
		Criteria criteria = getSession().createCriteria(
				AttentionSuppression.class);

		return criteria;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean isSuppressed(
			Class<? extends IRequiresAttentionCondition> conditionClass,
			Long dismissalId, User user) {
		if (user == null || dismissalId == null || conditionClass == null) {
			return false;
		}

		String conditionClassName = conditionClass.getName();

		String containingClassName = getContainingClassName(conditionClass);

		Criteria criteria = getSession().createCriteria(
				AttentionSuppression.class);
		criteria.add(Restrictions.eq("dismissableId", dismissalId));
		criteria.add(Restrictions.eq("conditionClass", conditionClassName));
		criteria.add(Restrictions.eq("containingClass", containingClassName));
		criteria.add(Restrictions.eq("user", user));

		criteria.setProjection(Projections.rowCount());

		Number count = (Number) criteria.uniqueResult();

		return count.intValue() > 0;
	}

	private String getContainingClassName(
			Class<? extends IRequiresAttentionCondition> conditionClass) {
		String containingClassName = conditionClass.getName();

		// Condition is an inner class
		if (conditionClass.getDeclaringClass() != null) {
			containingClassName = conditionClass.getDeclaringClass().getName();
		}
		return containingClassName;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void suppress(
			Class<? extends IRequiresAttentionCondition> conditionClass,
			Long dismissableId, User user) {
		if (user == null || dismissableId == null || conditionClass == null) {
			return;
		}

		if (!isSuppressed(conditionClass, dismissableId, user)) {
			AttentionSuppression suppression = new AttentionSuppression();
			suppression.setConditionClass(conditionClass.getName());
			suppression
					.setContainingClass(getContainingClassName(conditionClass));
			suppression.setDismissableId(dismissableId);
			suppression.setUser(user);

			save(suppression);
		}
	}
}
