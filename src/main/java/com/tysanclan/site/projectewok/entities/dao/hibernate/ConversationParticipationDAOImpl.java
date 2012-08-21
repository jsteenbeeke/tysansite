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
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.filters.ConversationParticipationFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ConversationParticipationDAOImpl extends
		EwokHibernateDAO<ConversationParticipation>
		implements
		com.tysanclan.site.projectewok.entities.dao.ConversationParticipationDAO {

	@Override
	protected Criteria createCriteria(
			SearchFilter<ConversationParticipation> filter) {
		// if (!(filter instanceof ConversationParticipationFilter)) {
		// throw new IllegalArgumentException(
		// "ConversationParticipationFilter expected");
		// }

		Criteria criteria = getSession().createCriteria(
				ConversationParticipation.class);

		if (filter instanceof ConversationParticipationFilter) {
			ConversationParticipationFilter cfilter = (ConversationParticipationFilter) filter;

			if (cfilter.getUser() != null)
				criteria.add(Restrictions.eq("user", cfilter.getUser()));
		}

		return criteria;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.ConversationParticipationDAO#countUnreadMessages(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	public long countUnreadMessages(User user) {
		StringBuilder builder = new StringBuilder();

		builder.append("SELECT COUNT(*) FROM ConversationParticipation cp WHERE cp.user = :user AND size(cp.readMessages) < (SELECT COUNT(*) FROM Message m, Conversation c WHERE c.id = cp.conversation AND m.conversation = c.id)");

		Query query = getSession().createQuery(builder.toString());
		query.setEntity("user", user);

		return (Long) query.uniqueResult();
	}
}
