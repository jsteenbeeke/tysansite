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
package com.tysanclan.site.projectewok.util.forum;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;

import javax.persistence.EntityManager;

/**
 * @author Jeroen Steenbeeke
 */
public interface ForumViewContext extends Serializable {
	List<ForumCategory> getCategories(EntityManager em, User viewer, long offset,
			long count);

	int countCategories(EntityManager em, User viewer);

	List<Forum> getForums(EntityManager em, ForumCategory context, User viewer,
			long offset, long count);

	int countForums(EntityManager em, ForumCategory context, User viewer);

	List<ForumThread> getThreads(EntityManager em, Forum context, User viewer,
			long offset, long count);

	int countThreads(EntityManager em, Forum context, User viewer);

	List<ForumPost> getPosts(EntityManager em, ForumThread context, User viewer,
			long offset, long count);

	int countPosts(EntityManager em, ForumThread context, User viewer);

}
