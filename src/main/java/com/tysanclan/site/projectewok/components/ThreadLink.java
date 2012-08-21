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

import org.apache.wicket.markup.html.link.Link;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
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
	private Integer page;

	public ThreadLink(String id, ForumThread thread) {
		this(id, thread, false);
	}

	public ThreadLink(String id, ForumThread thread, Integer page) {
		this(id, thread, page, false);
	}

	public ThreadLink(String id, ForumThread thread, boolean publicView) {
		this(id, thread, 1, publicView);
	}

	public ThreadLink(String id, ForumThread thread, Integer page,
			boolean publicView) {
		super(id, ModelMaker.wrap(thread));
		this.publicView = publicView;
		this.page = page;
	}

	@Override
	public void onClick() {
		setResponsePage(new ForumThreadPage(getModelObject().getId(), page,
				publicView));
	}
}
