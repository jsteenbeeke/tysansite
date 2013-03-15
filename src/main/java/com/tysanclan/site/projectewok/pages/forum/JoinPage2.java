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
package com.tysanclan.site.projectewok.pages.forum;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanNonMemberSecured;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.components.TysanTinyMCESettings;
import com.tysanclan.site.projectewok.components.renderer.GameRealmCartesianRenderer;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.model.GameRealmCartesian;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;
import com.tysanclan.site.projectewok.util.HTMLSanitizer;
import com.tysanclan.site.projectewok.util.StringUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanNonMemberSecured
public class JoinPage2 extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	public JoinPage2() {
		super("Join Tysan");

		List<Game> games = gameService.getActiveGames();

		List<GameRealmCartesian> grlms = new LinkedList<GameRealmCartesian>();

		for (Game game : games) {
			for (Realm realm : game.getRealms()) {
				grlms.add(new GameRealmCartesian(game, realm));
			}
		}

		final DropDownChoice<GameRealmCartesian> realmChoice = new DropDownChoice<GameRealmCartesian>(
				"gamerealm", new Model<GameRealmCartesian>(null), grlms,
				new GameRealmCartesianRenderer());

		Form<User> joinForm = new Form<User>("joinform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private MembershipService membershipService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				User user = getUser();
				TextArea<String> motivationArea = (TextArea<String>) get("motivation");
				String motivation = motivationArea.getModelObject();
				GameRealmCartesian cart = realmChoice.getModelObject();
				Game game = null;
				Realm realm = null;

				if (cart != null) {
					game = cart.getGame();
					realm = cart.getRealm();
				}

				List<String> words = new LinkedList<String>();
				String consider = HTMLSanitizer.stripTags(motivation);
				String current = "";
				for (int i = 0; i < consider.length(); i++) {
					char c = consider.charAt(i);
					if (Character.isWhitespace(c) && !current.isEmpty()) {
						words.add(current);
						current = "";
					} else {
						current += c;
					}
				}
				if (!current.isEmpty()) {
					words.add(current);
				}

				if (words.size() < 75) {
					error(StringUtil
							.combineStrings(
									"Your join application should contain at least 75 words. You currently only have ",
									words.size(), " words"));
				} else {
					ForumThread thread = membershipService.applyForMembership(
							user, motivation, game, realm);

					membershipService.registerAction(user);

					setResponsePage(new ForumThreadPage(thread.getId(), 1,
							false));
				}

			}

		};

		add(joinForm);

		TextArea<String> motivationArea = new TextArea<String>("motivation",
				new Model<String>(""));
		motivationArea.add(new TinyMceBehavior(new TysanTinyMCESettings()));

		joinForm.add(realmChoice);

		if (grlms.isEmpty()) {
			realmChoice.setVisible(false);
			realmChoice.setEnabled(false);
		} else {
			realmChoice.setRequired(true);
			realmChoice.setNullValid(false);
		}

		joinForm.add(motivationArea);

	}
}
