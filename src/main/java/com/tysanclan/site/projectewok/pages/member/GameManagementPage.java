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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.util.ImageUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class GameManagementPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameDAO gameDAO;

	@SpringBean
	private GameService gameService;

	public GameManagementPage() {
		super("Game Management");

		getAccordion()
				.add(new ListView<Game>("games", ModelMaker.wrap(gameDAO
						.findAll())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<Game> item) {
						Game game = item.getModelObject();

						item.add(new Label("name", game.getName()));
						item.add(new Image("icon", new ByteArrayResource(
								ImageUtil.getMimeType(game.getImage()), game
										.getImage())));
						if (game.getCoordinator() != null) {
							item.add(new MemberListItem("coordinator", game
									.getCoordinator()));
							IconLink.Builder builder = new IconLink.Builder(
									"images/icons/user_edit.png",
									new DefaultClickResponder<Game>(ModelMaker
											.wrap(game)) {
										private static final long serialVersionUID = 1L;

										@Override
										public void onClick() {
											setResponsePage(new EditGameSupervisorPage(
													getModelObject()));
										}
									});

							item.add(builder.newInstance("edit"));
						} else {
							item.add(new WebMarkupContainer("edit")
									.setVisible(false));
							IconLink.Builder builder = new IconLink.Builder(
									"images/icons/user_add.png",
									new DefaultClickResponder<Game>(ModelMaker
											.wrap(game)) {
										private static final long serialVersionUID = 1L;

										@Override
										public void onClick() {
											setResponsePage(new EditGameSupervisorPage(
													getModelObject()));
										}
									});

							item.add(builder.newInstance("coordinator"));
						}
						item.add(new Label("realmcount", new Model<Integer>(
								game.getRealms().size())));
						item.add(new Label("playercount", new Model<Integer>(
								gameService.countActivePlayers(game))));
						item.add(new Label("groupcount", new Model<Integer>(
								game.getGroups().size())));

						IconLink.Builder builder = new IconLink.Builder(
								"images/icons/cross.png",
								new DefaultClickResponder<Game>(ModelMaker
										.wrap(game)) {
									private static final long serialVersionUID = 1L;

									/**
									 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
									 */
									@Override
									public void onClick() {
										Game g = getModelObject();

										if (gameService.isGameInactive(g)) {
											gameService
													.deleteGame(getUser(), g);
										}

										setResponsePage(new RealmManagementPage());
									}

								});

						builder.setImageVisible(gameService
								.isGameInactive(game));

						item.add(builder.newInstance("remove"));
					}

				});

	}
}
