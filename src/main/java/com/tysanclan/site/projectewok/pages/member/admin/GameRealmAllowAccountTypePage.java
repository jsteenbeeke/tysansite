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
package com.tysanclan.site.projectewok.pages.member.admin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.model.GameRealmCartesian;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
public class GameRealmAllowAccountTypePage extends
		AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	@SpringBean
	private RoleService roleService;

	private List<GameRealmCartesian> gameRealms;

	public GameRealmAllowAccountTypePage() {
		super("Allowed Account Types");

		if (!getUser().equals(roleService.getSteward())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		gameRealms = new LinkedList<GameRealmCartesian>();

		for (Game game : gameService.getActiveGames()) {
			for (Realm realm : game.getRealms()) {
				gameRealms.add(new GameRealmCartesian(game, realm));
			}
		}

		getAccordion().add(
				new ListView<AccountType>("types", Arrays.asList(AccountType
						.values())) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<AccountType> item) {
						Label name = new Label("type", item.getModelObject()
								.toString());

						name.setRenderBodyOnly(true);

						item.add(name);
					}

				});

		getAccordion().add(
				new ListView<GameRealmCartesian>("realms", gameRealms) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(
							final ListItem<GameRealmCartesian> item) {
						GameRealmCartesian cart = item.getModelObject();

						item.add(new Label("game", cart.getGame().getName()));
						item.add(new Label("realm", cart.getRealm().getName()));

						item.add(new ListView<AccountType>("types", Arrays
								.asList(AccountType.values())) {

							private static final long serialVersionUID = 1L;

							@Override
							protected void populateItem(
									final ListItem<AccountType> iitem) {
								GameRealmCartesian crt = item.getModelObject();

								AccountType type = iitem.getModelObject();

								boolean allowed = gameService
										.isValidAccountType(crt.getGame(),
												crt.getRealm(), type);

								if (!allowed) {
									iitem.add(new IconLink.Builder(
											"images/icons/tick.png",
											new DefaultClickResponder<AccountType>(
													new Model<AccountType>(type)) {
												private static final long serialVersionUID = 1L;

												@Override
												public void onClick() {
													GameRealmCartesian c = item
															.getModelObject();
													AccountType acc = iitem
															.getModelObject();

													gameService.allowAccountType(
															c.getGame(),
															c.getRealm(), acc);

													setResponsePage(new GameRealmAllowAccountTypePage());
												}
											}).newInstance("button"));
								} else {
									iitem.add(new IconLink.Builder(
											"images/icons/cross.png",
											new DefaultClickResponder<AccountType>(
													new Model<AccountType>(type)) {
												private static final long serialVersionUID = 1L;

												@Override
												public void onClick() {
													GameRealmCartesian c = item
															.getModelObject();
													AccountType acc = iitem
															.getModelObject();

													gameService
															.disallowAccountType(
																	c.getGame(),
																	c.getRealm(),
																	acc);

													setResponsePage(new GameRealmAllowAccountTypePage());
												}
											}).newInstance("button"));
								}
							}

						});
					}

				});
	}
}
