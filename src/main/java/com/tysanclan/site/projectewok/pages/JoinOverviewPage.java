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
package com.tysanclan.site.projectewok.pages;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.MemberUtil;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * @author Jeroen Steenbeeke
 */
public class JoinOverviewPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public JoinOverviewPage() {
		super("Register");

		User user = getUser();

		if (user != null) {
			if (MemberUtil.isMember(user)) {
				setResponsePage(
						com.tysanclan.site.projectewok.pages.member.OverviewPage.class);
			} else {
				setResponsePage(
						com.tysanclan.site.projectewok.pages.forum.OverviewPage.class);
			}
		}

		add(new BookmarkablePageLink<Void>("joinapply",
				RegisterAndApplyPage.class));
		add(new BookmarkablePageLink<Void>("register", RegistrationPage.class));
		// add(new BookmarkablePageLink<Void>("register",
		// RegistrationPage.class));
	}
}
