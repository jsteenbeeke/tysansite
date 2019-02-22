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
import com.tysanclan.site.projectewok.entities.filter.ForumPostFilter;
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
class ForumPostDAOImpl extends HibernateDAO<ForumPost, ForumPostFilter> implements
		com.tysanclan.site.projectewok.entities.dao.ForumPostDAO {


	@Override
	public int getUnreadSize(User user) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<UnreadForumPost> root = criteriaQuery.from(UnreadForumPost.class);

		criteriaQuery.select(criteriaBuilder.count(root)).where(
				criteriaBuilder.equal(root.get(UnreadForumPost_.user), user)
		);

		return ((Number) entityManager.createQuery(criteriaQuery).getSingleResult()).intValue();
	}

	@Override
	public int countByContext(User user, ForumThread contextObject,
							  ForumViewContext context) {
		return context.countPosts(entityManager, contextObject, user);
	}

	@Override
	public List<ForumPost> findByContext(User user, ForumThread contextObject,
										 ForumViewContext context, long first, long count) {
		return context
				.getPosts(entityManager, contextObject, user, first, count);
	}
}
