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
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanTopPanel;
import com.tysanclan.site.projectewok.auth.TysanNonMemberSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.ForumOverviewPage;
import com.tysanclan.site.projectewok.pages.forum.ForumUserPreferencesPage;
import com.tysanclan.site.projectewok.pages.forum.JoinPage;
import com.tysanclan.site.projectewok.pages.forum.OverviewPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanNonMemberSecured
public class TysanUserPanel extends TysanTopPanel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumService forumService;

	@SpringBean
	private MembershipService memberService;

	public TysanUserPanel(String id, User user) {
		super(id, ModelMaker.wrap(user));

		forumService.addUnreadPosts(user);

		memberService.registerAction(user);

		addOverviewLink();
		addForumLink();
		add(new LogoutLink("logout"));
		addJoinLink(user.getRank());

		addPreferencesLink();
	}

	/**
	 *
	 */
	private void addJoinLink(Rank rank) {
		add(new Link<Void>("joinlink") {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new JoinPage());
			}

		}.setVisible(rank == Rank.FORUM));
	}

	/**
	 *
	 */
	private void addPreferencesLink() {
		add(new Link<Void>("preferences") {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ForumUserPreferencesPage());
			}

		});
	}

	private void addForumLink() {
		add(new Link<Void>("forums") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new ForumOverviewPage());

			}
		});
	}

	/**
	 *
	 */
	private void addOverviewLink() {
		add(new Link<Void>("overview") {

			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new OverviewPage());
			}
		});
	}
}
