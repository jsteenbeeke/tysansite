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

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.filter.ForumFilter;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ForumDAOImpl extends HibernateDAO<Forum, ForumFilter>
		implements com.tysanclan.site.projectewok.entities.dao.ForumDAO {
	@Override
	public boolean isPostUnread(User user, ForumPost post) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder
				.createQuery(Long.class);
		Root<UnreadForumPost> root = criteriaQuery.from(UnreadForumPost.class);

		criteriaQuery.select(criteriaBuilder.count(root));
		criteriaQuery.where(criteriaBuilder
						.equal(root.get(UnreadForumPost_.forumPost), post),
				criteriaBuilder.equal(root.get(UnreadForumPost_.user), user));

		return ((Number) entityManager.createQuery(criteriaQuery)
				.getSingleResult()).intValue() == 1;
	}

	@Override
	public int countByContext(User user, ForumCategory contextObject,
			ForumViewContext context) {
		return context.countForums(entityManager, contextObject, user);
	}

	@Override
	public List<Forum> findByContext(User user, ForumCategory contextObject,
			ForumViewContext context, int first, int count) {
		return context
				.getForums(entityManager, contextObject, user, first, count);
	}
}
