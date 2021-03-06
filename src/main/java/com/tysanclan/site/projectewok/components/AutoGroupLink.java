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

import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.pages.GroupPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Jeroen Steenbeeke
 */
public class AutoGroupLink
		extends LoginAwareLink<GroupLink, BookmarkablePageLink<Group>> {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public AutoGroupLink(String id, Group group) {
		super(id, group.getName());

		setLoggedInLink(new GroupLink(LINK_LABEL, group));

		PageParameters params = new PageParameters();
		params.add("groupid", group.getId().toString());
		setNotLoggedInLink(
				new BookmarkablePageLink<Group>(LINK_LABEL, GroupPage.class,
						params));
	}

	/**
	 * @see com.tysanclan.site.projectewok.components.LoginAwareLink#isLoggedInCondition()
	 */
	@Override
	protected boolean isLoggedInCondition() {
		return false; // Always use bookmarkable pagelinks
	}
}
