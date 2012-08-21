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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.Conversation;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.filters.ConversationFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ConversationDAOImpl extends EwokHibernateDAO<Conversation> implements
		com.tysanclan.site.projectewok.entities.dao.ConversationDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<Conversation> _filter) {
		ConversationFilter filter = (ConversationFilter) _filter;

		Criteria crit = getSession().createCriteria(Conversation.class);

		if (filter.isSortByLastResponse()) {
			filter.addOrderBy("id", false);
			filter.addOrderBy("lastResponse", false);
		}

		if (!filter.getParticipants().isEmpty()) {
			for (User participant : filter.getParticipants()) {
				DetachedCriteria dc = DetachedCriteria
						.forClass(ConversationParticipation.class);
				dc.add(Restrictions.eq("user", participant));
				dc.setProjection(Projections.property("conversation"));
				crit.add(Subqueries.propertyIn("id", dc));
			}
		}

		return crit;
	}

}
