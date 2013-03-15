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
package com.tysanclan.site.projectewok.pages.member;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.model.GameRealmCartesian;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class DeadRealmRemovalPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	public DeadRealmRemovalPage(Game game) {
		super("Remove dead realms");

		if (!getUser().equals(game.getCoordinator())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		List<GameRealmCartesian> cartesians = new LinkedList<GameRealmCartesian>();

		for (Realm realm : game.getRealms()) {
			cartesians.add(new GameRealmCartesian(game, realm));
		}

		init(cartesians);
	}

	public DeadRealmRemovalPage(Realm realm) {
		super("Remove dead games");

		if (!getUser().equals(realm.getOverseer())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		List<GameRealmCartesian> cartesians = new LinkedList<GameRealmCartesian>();

		for (Game game : realm.getGames()) {
			cartesians.add(new GameRealmCartesian(game, realm));
		}

		init(cartesians);
	}

	private void init(List<GameRealmCartesian> cartesians) {
		getAccordion().add(
				new ListView<GameRealmCartesian>("deadrealms", cartesians) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
					 */
					@Override
					protected void populateItem(
							ListItem<GameRealmCartesian> item) {
						GameRealmCartesian cartesian = item.getModelObject();

						Game game = cartesian.getGame();
						Realm realm = cartesian.getRealm();

						item.add(new Label("game", game.getName()));
						item.add(new Label("realm", realm.getName()));
						item.add(new Label("listed", Model.of(gameService
								.countPlayers(game, realm))));
						item.add(new IconLink.Builder("images/icons/cross.png",
								new DefaultClickResponder<GameRealmCartesian>(

								new Model<GameRealmCartesian>(cartesian)) {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClick() {
										GameRealmCartesian cart = getModelObject();

										Game g = cart.getGame();
										Realm r = cart.getRealm();

										User user = getUser();

										gameService.removeFromRealm(user, r, g);

										setResponsePage(new OverviewPage());

									}

								}).newInstance("delete"));
					}
				});
	}
}
