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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;

/**
 * @author Jeroen Steenbeeke
 */
public class AutoThreadLink extends
		LoginAwareLink<ThreadLink, BookmarkablePageLink<ForumThread>> {
	private static final long serialVersionUID = 1L;

	private final boolean condition;

	public AutoThreadLink(String id, ForumThread thread) {
		this(id, thread, thread.getTitle());
	}

	public AutoThreadLink(String id, ForumThread thread, Integer page) {
		this(id, thread, page, thread.getTitle());
	}

	public AutoThreadLink(String id, ForumThread thread, String text) {
		this(id, thread, 1, text);
	}

	public AutoThreadLink(String id, ForumThread thread, Integer page,
			String text) {
		super(id, text);

		ThreadLink tl = new ThreadLink(LINK_LABEL, thread, page);
		PageParameters pp = new PageParameters();
		pp.add("threadid", thread.getId().toString());
		pp.add("pageid", page.toString());
		BookmarkablePageLink<ForumThread> bpl = new BookmarkablePageLink<ForumThread>(
				LINK_LABEL, ForumThreadPage.class, pp);

		condition = super.isLoggedInCondition()
				&& (thread.isShadow() || thread.getForum().isMembersOnly());

		setLoggedInLink(tl);
		setNotLoggedInLink(bpl);
	}

	/**
	 * @see com.tysanclan.site.projectewok.components.LoginAwareLink#isLoggedInCondition()
	 */
	@Override
	protected boolean isLoggedInCondition() {
		return condition;
	}
}
