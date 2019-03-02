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
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.filter.ForumThreadFilter;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ForumThreadDAOImpl extends HibernateDAO<ForumThread, ForumThreadFilter>
		implements com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO {
	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO#findByPostTime(com.tysanclan.site.projectewok.entities.Forum)
	 */
	@Override
	public List<ForumThread> findByPostTime(Forum forum) {
		ForumThreadFilter filter = new ForumThreadFilter();
		filter.forum(forum);
		filter.postTime().orderBy(false);

		return findByFilter(filter).toJavaList();
	}

	@Override
	public int countByContext(User user, Forum contextObject,
			ForumViewContext context) {
		return context.countThreads(entityManager, contextObject, user);
	}

	@Override
	public List<ForumThread> findByContext(User user, Forum contextObject,
			ForumViewContext context, int first, int count) {
		return context
				.getThreads(entityManager, contextObject, user, first, count);
	}
}
