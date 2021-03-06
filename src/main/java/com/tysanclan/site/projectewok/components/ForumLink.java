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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.pages.ForumPage;
import org.apache.wicket.markup.html.link.Link;

/**
 * Link to a ForumThread
 *
 * @author Jeroen Steenbeeke
 */
public class ForumLink extends Link<Forum> {
	private static final long serialVersionUID = 1L;

	private long page;

	public ForumLink(String id, Forum forum) {
		this(id, forum, 1L);
	}

	public ForumLink(String id, Forum forum, long page) {
		super(id, ModelMaker.wrap(forum));
		this.page = page;
	}

	@Override
	public void onClick() {
		setResponsePage(new ForumPage(getModelObject(), page));
	}
}
