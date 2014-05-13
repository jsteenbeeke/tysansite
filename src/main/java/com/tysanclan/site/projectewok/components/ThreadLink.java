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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.wicket.markup.html.link.Link;

import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;

/**
 * Link to a ForumThread
 * 
 * @author Jeroen Steenbeeke
 */
public class ThreadLink extends Link<ForumThread> {
	private static final long serialVersionUID = 1L;

	private boolean publicView;

	private int pageNumber;

	private Long threadId;

	public ThreadLink(@Nonnull String id, @Nullable ForumThread thread) {
		this(id, thread, false);
	}

	public ThreadLink(@Nonnull String id, @Nullable ForumThread thread, int page) {
		this(id, thread, page, false);
	}

	public ThreadLink(@Nonnull String id, @Nullable ForumThread thread,
			boolean publicView) {
		this(id, thread, 1, publicView);
	}

	public ThreadLink(@Nonnull String id, @Nullable ForumThread thread,
			int page, boolean publicView) {
		super(id);
		this.threadId = thread != null ? thread.getId() : null;
		this.publicView = publicView;
		this.pageNumber = page;
	}

	@Override
	public void onClick() {
		if (threadId != null) {
			setResponsePage(new ForumThreadPage(threadId, pageNumber,
					publicView));
		}
	}
}
