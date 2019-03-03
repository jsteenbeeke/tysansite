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
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.filter.ConversationParticipationFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ConversationParticipationDAOImpl extends
		HibernateDAO<ConversationParticipation, ConversationParticipationFilter>
		implements
		com.tysanclan.site.projectewok.entities.dao.ConversationParticipationDAO {

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.ConversationParticipationDAO#countUnreadMessages(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	public long countUnreadMessages(User user) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
		Root<ConversationParticipation> root = query
				.from(ConversationParticipation.class);
		Subquery<Long> subquery = query.subquery(Long.class);
		Root<Message> subqueryRoot = subquery.from(Message.class);

		Join<Message, Conversation> join = subqueryRoot
				.join(Message_.conversation);
		subquery.select(criteriaBuilder.count(join)).where(criteriaBuilder
				.equal(join.get(Conversation_.id),
						root.get(ConversationParticipation_.conversation)));

		query.select(criteriaBuilder.count(root)).where(criteriaBuilder
				.lessThan(criteriaBuilder.size(root
								.get(ConversationParticipation_.readMessages)),
						subquery.as(Integer.class)));

		return entityManager.createQuery(query).getSingleResult();
	}
}
