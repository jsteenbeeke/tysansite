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
package com.tysanclan.site.projectewok.pages.member;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.components.AccountPanel;
import com.tysanclan.site.projectewok.components.renderer.GameRealmCartesianRenderer;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.model.GameRealmCartesian;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class EditAccountsPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameDAO gameDAO;

	/**
	 * 
	 */
	public EditAccountsPage() {
		super("Edit Accounts");

		Form<User> form = new Form<User>("gameform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GameService gameService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<GameRealmCartesian> cartesianChoice = (DropDownChoice<GameRealmCartesian>) get("choices");
				GameRealmCartesian cartesian = cartesianChoice.getModelObject();

				gameService.addPlayedGame(getUser(), cartesian.getGame(),
						cartesian.getRealm());

				setResponsePage(new EditAccountsPage());
			}
		};

		add(form);

		List<GameRealmCartesian> cartesians = new LinkedList<GameRealmCartesian>();

		for (Game game : gameDAO.findAll()) {
			for (Realm realm : game.getRealms()) {
				cartesians.add(new GameRealmCartesian(game, realm));
			}
		}

		for (UserGameRealm ugr : getUser().getPlayedGames()) {
			cartesians.remove(new GameRealmCartesian(ugr.getGame(), ugr
					.getRealm()));
		}

		form.add(new DropDownChoice<GameRealmCartesian>("choices",
				new Model<GameRealmCartesian>(null), cartesians,
				new GameRealmCartesianRenderer()));

		add(form);

		List<UserGameRealm> played = new LinkedList<UserGameRealm>();
		played.addAll(getUser().getPlayedGames());

		Collections.sort(played, new Comparator<UserGameRealm>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			public int compare(UserGameRealm o1, UserGameRealm o2) {
				return o1.getGame().getName()
						.compareToIgnoreCase(o2.getGame().getName());
			}
		});

		add(new ListView<UserGameRealm>("played", ModelMaker.wrap(played)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<UserGameRealm> item) {
				UserGameRealm ugr = item.getModelObject();

				item.add(new AccountPanel("accountpanel", ugr));

			}
		});

	}
}
