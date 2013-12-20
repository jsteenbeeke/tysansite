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
package com.tysanclan.site.projectewok.pages.forum;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanNonMemberSecured;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.components.renderer.GameRealmCartesianRenderer;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.model.GameRealmCartesian;
import com.tysanclan.site.projectewok.pages.ForumThreadPage;
import com.tysanclan.site.projectewok.util.StringUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;

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
		realmChoice.setRequired(true);
		realmChoice.setNullValid(false);

		final TextArea<String> otherGamesDescription = new TextArea<String>(
				"othergames", new Model<String>(""));
		otherGamesDescription.setRequired(true);

		final TextArea<String> sortOfPersonArea = new TextArea<String>(
				"sortofperson", new Model<String>(""));
		sortOfPersonArea.setRequired(true);

		final TextArea<String> lookingForArea = new TextArea<String>(
				"lookingfor", new Model<String>(""));
		lookingForArea.setRequired(true);

		Form<User> joinForm = new Form<User>("joinform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private MembershipService membershipService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				User user = getUser();
				GameRealmCartesian cart = realmChoice.getModelObject();
				Game game = null;
				Realm realm = null;

				if (cart != null) {
					game = cart.getGame();
					realm = cart.getRealm();
				}

				String otherGames = BBCodeUtil.stripTags(otherGamesDescription
						.getModelObject());
				String sortOfPerson = BBCodeUtil.stripTags(sortOfPersonArea
						.getModelObject());
				String lookingFor = BBCodeUtil.stripTags(lookingForArea
						.getModelObject());

				boolean valid = true;

				if (StringUtil.countWords(sortOfPerson) < 30) {
					JoinPage2.this
							.error("Please describe the sort of person you are in at least 30 words");
					valid = false;
				}

				if (StringUtil.countWords(lookingFor) < 30) {
					JoinPage2.this
							.error("Please describe the sort of clan you are looking for you are in at least 30 words");
					valid = false;
				}

				if (valid) {
					StringBuilder motivation = new StringBuilder();
					motivation
							.append("<strong>What sort of person are you?</strong><br />\n");
					motivation.append(sortOfPerson);
					motivation.append("<br /><br />");

					motivation
							.append("<strong>What are you looking for in a clan?</strong><br />\n");
					motivation.append(lookingFor);
					motivation.append("<br /><br />");

					motivation
							.append("<strong>What other games do you play?</strong><br />\n");
					motivation.append(otherGames);
					motivation.append("<br /><br />");

					ForumThread thread = membershipService.applyForMembership(
							user, motivation.toString(), game, realm);

					membershipService.registerAction(user);

					setResponsePage(new ForumThreadPage(thread.getId(), 1,
							false));
				}

			}

		};

		add(joinForm);

		joinForm.add(realmChoice);
		joinForm.add(otherGamesDescription);
		joinForm.add(sortOfPersonArea);
		joinForm.add(lookingForArea);

		if (grlms.isEmpty()) {
			realmChoice.setVisible(false);
			realmChoice.setEnabled(false);
		} else {
			realmChoice.setRequired(true);
			realmChoice.setNullValid(false);
		}

	}
}
