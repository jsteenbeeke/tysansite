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

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;

/**
 * Forum context for banned users
 * @author Jeroen Steenbeeke
 */
public class ShadowForumViewContext extends AbstractForumViewContext {

	private static final long serialVersionUID = 1L;

	@Override
	public int countCategories(Session sess, User viewer) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT COUNT(*) FROM FORUMCATEGORY FC ");
		q.append("WHERE EXISTS (");

		q.append("SELECT * FROM FORUM f ");
		q.append("WHERE f.DTYPE!='GroupForum' AND f.MEMBERSONLY=false AND f.category_id = FC.id");

		q.append(")");

		SQLQuery query = sess.createSQLQuery(q.toString());

		return count(query);
	}

	@Override
	public List<ForumCategory> getCategories(Session sess, User viewer,
			long offset, long count) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT * FROM FORUMCATEGORY FC ");
		q.append("WHERE EXISTS (");

		q.append("SELECT * FROM FORUM f ");
		q.append("WHERE f.DTYPE!='GroupForum' AND f.MEMBERSONLY=false AND f.category_id = FC.id");

		q.append(") ORDER BY id ASC LIMIT :count OFFSET :offset");

		SQLQuery query = sess.createSQLQuery(q.toString());
		query.setLong("count", count);
		query.setLong("offset", offset);
		query.addEntity(ForumCategory.class);

		return listOf(query);
	}

	@Override
	public int countForums(Session sess, ForumCategory context, User viewer) {

		StringBuilder q = new StringBuilder();
		q.append("SELECT COUNT(*) FROM FORUM f ");
		q.append("WHERE f.DTYPE!='GroupForum' AND f.MEMBERSONLY=false AND f.category_id = :cat");

		SQLQuery query = sess.createSQLQuery(q.toString());
		query.setLong("cat", context.getId());

		return count(query);
	}

	@Override
	public List<Forum> getForums(Session sess, ForumCategory context,
			User viewer, long offset, long count) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT * FROM FORUM f ");
		q.append("WHERE f.DTYPE!='GroupForum' AND f.MEMBERSONLY=false AND f.category_id = :cat ");
		q.append("ORDER BY position ASC ");
		q.append("LIMIT :count OFFSET :offset");

		SQLQuery query = sess.createSQLQuery(q.toString());
		query.setLong("cat", context.getId());
		query.setLong("count", count);
		query.setLong("offset", offset);
		query.addEntity(Forum.class);

		return listOf(query);
	}

	@Override
	public int countThreads(Session sess, Forum context, User viewer) {

		StringBuilder q = new StringBuilder();

		q.append("SELECT COUNT(*) FROM FORUMTHREAD FT WHERE ft.forum_id = :forum AND ");
		q.append("NOT EXISTS (SELECT * FROM trial WHERE trialthread_id = ft.id) AND ");
		q.append("EXISTS (SELECT * FROM FORUMPOST FP WHERE (fp.shadow = false OR fp.poster_id = :viewer ) AND fp.thread_id = ft.id)");

		SQLQuery query = sess.createSQLQuery(q.toString());
		query.setLong("forum", context.getId());
		query.setLong("viewer", viewer.getId());

		return count(query);

	}

	@Override
	public List<ForumThread> getThreads(Session sess, Forum context,
			User viewer, long offset, long count) {
		StringBuilder q = new StringBuilder();

		q.append("SELECT * FROM FORUMTHREAD FT WHERE ft.forum_id = :forum AND ");
		q.append("EXISTS (SELECT * FROM FORUMPOST FP WHERE (fp.shadow = false OR fp.poster_id = :viewer ) AND fp.thread_id = ft.id) ");
		q.append("AND NOT EXISTS (SELECT * FROM trial WHERE trialthread_id = ft.id) ");
		q.append("ORDER BY STICKY DESC, lastPost DESC ");
		q.append("LIMIT :count OFFSET :offset");

		SQLQuery query = sess.createSQLQuery(q.toString());
		query.setLong("forum", context.getId());
		query.setLong("count", count);
		query.setLong("offset", offset);
		query.setLong("viewer", viewer.getId());
		query.setLong("viewer2", viewer.getId());
		query.addEntity(ForumThread.class);

		return listOf(query);
	}

	@Override
	public int countPosts(Session sess, ForumThread context, User viewer) {
		StringBuilder q = new StringBuilder();

		q.append("SELECT COUNT(*) FROM FORUMPOST FP WHERE (fp.shadow = false OR fp.poster_id = :viewer ) AND fp.thread_id = :thread");

		SQLQuery query = sess.createSQLQuery(q.toString());
		query.setLong("thread", context.getId());
		query.setLong("viewer", viewer.getId());

		return count(query);
	}

	@Override
	public List<ForumPost> getPosts(Session sess, ForumThread context,
			User viewer, long offset, long count) {
		StringBuilder q = new StringBuilder();

		q.append("SELECT * FROM FORUMPOST FP WHERE (fp.shadow = false OR fp.poster_id = :viewer)");
		q.append(" AND fp.thread_id = :thread ");
		q.append("ORDER BY time ASC LIMIT :count OFFSET :offset");

		SQLQuery query = sess.createSQLQuery(q.toString());
		query.setLong("thread", context.getId());
		query.setLong("count", count);
		query.setLong("offset", offset);
		query.setLong("viewer", viewer.getId());
		query.addEntity(ForumPost.class);

		return listOf(query);
	}

}
