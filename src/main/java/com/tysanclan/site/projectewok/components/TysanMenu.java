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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.pages.*;

/**
 * @author Jeroen Steenbeeke
 */
public class TysanMenu extends Panel {
	private static final long serialVersionUID = 1L;

	public TysanMenu(String id, boolean loggedIn) {
		super(id);
		add(new BookmarkablePageLink<Void>("news", NewsPage.class));
		add(new BookmarkablePageLink<Void>("about", AboutPage.class));
		add(new BookmarkablePageLink<Void>("charter", CharterPage.class));
		add(new BookmarkablePageLink<Void>("regulations", RegulationPage.class));
		add(new BookmarkablePageLink<Void>("join", JoinOverviewPage.class));
		add(new BookmarkablePageLink<Void>("members", RosterPage.class));
		add(new BookmarkablePageLink<Void>("groups", GroupsPage.class));
		add(new BookmarkablePageLink<Void>("history", HistoryPage.class));

		if (!loggedIn) {
			add(new BookmarkablePageLink<Void>("forums",
					ForumOverviewPage.class));
		} else {
			add(new Link<Forum>("forums") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new ForumOverviewPage());
				}

			});
		}
	}

}
