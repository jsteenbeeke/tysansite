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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class RealmManagementPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RealmDAO realmDAO;

	@SpringBean
	private RealmService realmService;

	public RealmManagementPage() {
		super("Realm Management");

		add(
				new ListView<Realm>("realms", ModelMaker.wrap(realmDAO
						.findAll())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<Realm> item) {
						Realm realm = item.getModelObject();

						item.add(new Label("name", realm.getName()));
						if (realm.getOverseer() != null) {
							item.add(new MemberListItem("coordinator", realm
									.getOverseer()));
							IconLink.Builder builder = new IconLink.Builder(
									"images/icons/user_edit.png",
									new DefaultClickResponder<Realm>(ModelMaker
											.wrap(realm)) {
										private static final long serialVersionUID = 1L;

										@Override
										public void onClick() {
											setResponsePage(new EditRealmSupervisorPage(
													getModelObject()));
										}
									});

							item.add(builder.newInstance("edit"));
						} else {
							item.add(new WebMarkupContainer("edit")
									.setVisible(false));
							IconLink.Builder builder = new IconLink.Builder(
									"images/icons/user_add.png",
									new DefaultClickResponder<Realm>(ModelMaker
											.wrap(realm)) {
										private static final long serialVersionUID = 1L;

										@Override
										public void onClick() {
											setResponsePage(new EditRealmSupervisorPage(
													getModelObject()));
										}
									});

							item.add(builder.newInstance("coordinator"));
						}
						item.add(new Label("gamescount", new Model<Integer>(
								realm.getGames().size())));
						item.add(new Label("playercount", new Model<Integer>(
								realmService.countActivePlayers(realm))));
						item.add(new Label("groupcount", new Model<Integer>(
								realm.getGroups().size())));

						IconLink.Builder builder = new IconLink.Builder(
								"images/icons/cross.png",
								new DefaultClickResponder<Realm>(ModelMaker
										.wrap(realm)) {
									private static final long serialVersionUID = 1L;

									/**
									 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
									 */
									@Override
									public void onClick() {
										Realm rlm = getModelObject();

										if (realmService.isRealmInactive(rlm)) {
											realmService.deleteRealm(rlm,
													getUser());
										}

										setResponsePage(new RealmManagementPage());
									}

								});

						builder.setImageVisible(realmService
								.isRealmInactive(realm));

						item.add(builder.newInstance("remove"));
					}

				});

	}
}
