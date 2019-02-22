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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.components.renderer.GameChoiceRenderer;
import com.tysanclan.site.projectewok.components.renderer.GameRealmCartesianRenderer;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.RealmPetition;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;
import com.tysanclan.site.projectewok.entities.dao.RealmPetitionDAO;
import com.tysanclan.site.projectewok.model.GameRealmCartesian;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.CHANCELLOR, Rank.FULL_MEMBER, Rank.SENIOR_MEMBER,
		Rank.SENATOR, Rank.TRUTHSAYER, Rank.REVERED_MEMBER })
public class CreateRealmPetitionPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameDAO gameDAO;

	@SpringBean
	private RealmDAO realmDAO;

	@SpringBean
	private RealmPetitionDAO realmPetitionDAO;

	public CreateRealmPetitionPage() {
		super("Create Realm Petition");

		List<GameRealmCartesian> allCombinations = new LinkedList<GameRealmCartesian>();

		List<Game> games = gameDAO.findAll();

		for (Game game : games) {
			List<Realm> curr = new LinkedList<Realm>();
			curr.addAll(realmDAO.findAll());
			curr.removeAll(game.getRealms());

			for (Realm realm : curr) {
				allCombinations.add(new GameRealmCartesian(game, realm));
			}

		}

		for (RealmPetition petition : realmPetitionDAO.findAll()) {
			if (petition.getGame() != null && petition.getRealm() != null) {
				allCombinations.remove(new GameRealmCartesian(petition
						.getGame(), petition.getRealm()));
			}
		}

		Form<RealmPetition> newRealm = new Form<RealmPetition>("new") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private RealmService realmService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> nameField = (TextField<String>) get("name");
				DropDownChoice<Game> gameSelect = (DropDownChoice<Game>) get("game");

				String name = nameField.getModelObject();
				Game game = gameSelect.getModelObject();

				realmService.createRealmPetition(name, getUser(), game);

				setResponsePage(new SignRealmPetitionsPage());
			}

		};

		newRealm.add(new Button("submit").setVisible(!games.isEmpty()));

		newRealm.add(new TextField<String>("name", new Model<String>(""))
				.setRequired(true));

		newRealm.add(new DropDownChoice<Game>("game", ModelMaker
				.wrap((Game) null), ModelMaker.wrapChoices(games),
				new GameChoiceRenderer()).setRequired(true));

		add(newRealm);

		Form<RealmPetition> existing = new Form<RealmPetition>("existing") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private RealmService realmService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<GameRealmCartesian> gameRealmSelect = (DropDownChoice<GameRealmCartesian>) get("existing");

				GameRealmCartesian cartesian = gameRealmSelect.getModelObject();

				realmService.createRealmPetition(cartesian.getRealm(),
						getUser(), cartesian.getGame());

				setResponsePage(new SignRealmPetitionsPage());
			}
		};

		existing.add(new DropDownChoice<GameRealmCartesian>("existing",
				new Model<GameRealmCartesian>(null), allCombinations,
				new GameRealmCartesianRenderer()).setRequired(true));

		existing.add(new Button("submit").setVisible(!allCombinations.isEmpty()));

		add(new WebMarkupContainer("existingRealmsHeader")
				.setVisible(!allCombinations.isEmpty()));

		add(existing);
	}

}
