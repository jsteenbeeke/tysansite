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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.pages.ForumPage;

/**
 * @author Jeroen Steenbeeke
 */
public class AutoForumLink extends
		LoginAwareLink<ForumLink, BookmarkablePageLink<Forum>> {
	private static final long serialVersionUID = 1L;

	private final boolean condition;

	public AutoForumLink(String id, Forum forum) {
		this(id, forum, 1L);
	}

	public AutoForumLink(String id, Forum forum, long pageIndex) {
		this(id, forum, forum.getName(), 1L);
	}

	public AutoForumLink(String id, Forum forum, String text) {
		this(id, forum, text, 1L);
	}

	/**
	 * 
	 */
	public AutoForumLink(String id, Forum forum, String text, long pageIndex) {
		super(id, text);

		ForumLink llink = new ForumLink(LINK_LABEL, forum, pageIndex);
		PageParameters params = new PageParameters();
		params.add("forumid", Long.toString(forum.getId()));
		params.add("pageid", Long.toString(pageIndex));
		BookmarkablePageLink<Forum> ulink = new BookmarkablePageLink<Forum>(
				LINK_LABEL, ForumPage.class, params);

		condition = super.isLoggedInCondition() && forum.isMembersOnly();

		setLoggedInLink(llink);
		setNotLoggedInLink(ulink);
	}

	/**
	 * @see com.tysanclan.site.projectewok.components.LoginAwareLink#isLoggedInCondition()
	 */
	@Override
	protected boolean isLoggedInCondition() {
		return condition;
	}
}
