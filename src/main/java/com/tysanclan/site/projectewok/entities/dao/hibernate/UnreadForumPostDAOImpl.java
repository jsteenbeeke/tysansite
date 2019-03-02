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
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.UnreadForumPost;
import com.tysanclan.site.projectewok.entities.UnreadForumPost_;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.filter.UnreadForumPostFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

/**
 * @author Ties
 */
@Component
@Scope("request")
public class UnreadForumPostDAOImpl
		extends HibernateDAO<UnreadForumPost, UnreadForumPostFilter> implements
		com.tysanclan.site.projectewok.entities.dao.UnreadForumPostDAO {

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void markAsRead(User user, ForumPost post) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<UnreadForumPost> delete = criteriaBuilder
				.createCriteriaDelete(UnreadForumPost.class);
		Root<UnreadForumPost> root = delete.from(UnreadForumPost.class);

		entityManager.createQuery(delete.where(
				criteriaBuilder.equal(root.get(UnreadForumPost_.user), user),
				criteriaBuilder
						.equal(root.get(UnreadForumPost_.forumPost), post)))
				.executeUpdate();
	}

}
