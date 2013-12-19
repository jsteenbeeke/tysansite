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
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.UnreadForumPost;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Ties
 */
@Component
@Scope("request")
public class UnreadForumPostDAOImpl extends EwokHibernateDAO<UnreadForumPost>
		implements
		com.tysanclan.site.projectewok.entities.dao.UnreadForumPostDAO {

	@Override
	protected Criteria createCriteria(SearchFilter<UnreadForumPost> filter) {
		return getSession().createCriteria(UnreadForumPost.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void markAsRead(User user, ForumPost post) {
		Query query = getSession()
				.createQuery(
						"delete from UnreadForumPost where user = :reader and forumPost = :post");

		query.setEntity("reader", user);
		query.setEntity("post", post);

		query.executeUpdate();
	}

}
