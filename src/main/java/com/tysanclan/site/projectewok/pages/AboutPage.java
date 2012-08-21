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
package com.tysanclan.site.projectewok.pages;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.components.RankIcon;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.util.ImageUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class AboutPage extends TysanPage {
	@SpringBean
	private UserDAO userDAO;

	@SpringBean
	private GameDAO gameDAO;

	private final Map<Rank, String> rankDescriptions = new HashMap<Rank, String>();
	private final List<Rank> ranks = new LinkedList<Rank>();

	public AboutPage() {
		super("About");

		UserFilter filter = new UserFilter();
		filter.addRank(Rank.CHANCELLOR);

		List<User> users = userDAO.findByFilter(filter);

		User leader = null;
		if (!users.isEmpty()) {
			leader = users.get(0);
			add(new MemberListItem("leader", leader));
		} else {
			add(new Label("leader", "<i>None</i>")
			        .setEscapeModelStrings(false));
		}

		UserFilter filter2 = new UserFilter();
		filter2.addRank(Rank.SENATOR);

		List<User> senate = userDAO.findByFilter(filter2);
		add(new ListView<User>("senate", ModelMaker
		        .wrap(senate)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				User user = item.getModelObject();

				item
				        .add(new MemberListItem("member",
				                user));
			}

		});

		add(new ListView<Game>("games", ModelMaker
		        .wrap(gameDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(
			        final ListItem<Game> item) {
				Game game = item.getModelObject();

				final int rowCount = game.getRealms()
				        .size();
				final String gameName = game.getName();

				item.add(new ListView<Realm>("realms",
				        ModelMaker.wrap(game.getRealms())) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private GameService gameService;

					/**
					 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
					 */
					@Override
					protected void populateItem(
					        ListItem<Realm> innerItem) {
						WebMarkupContainer iconContainer = new WebMarkupContainer(
						        "iconcontainer");
						WebMarkupContainer supervisorContainer = new WebMarkupContainer(
						        "supervisorcontainer");
						Label gameNameLabel = new Label(
						        "name", gameName);

						if (innerItem.getIndex() != 0) {
							iconContainer.setVisible(false);
							gameNameLabel.setVisible(false);
							supervisorContainer
							        .setVisible(false);
						} else {
							SimpleAttributeModifier mod = new SimpleAttributeModifier(
							        "rowspan",
							        Integer
							                .toString(rowCount));
							iconContainer.add(mod);
							gameNameLabel.add(mod);
							supervisorContainer.add(mod);
						}

						innerItem.add(iconContainer);
						innerItem.add(gameNameLabel);
						innerItem.add(supervisorContainer);

						Realm realm = innerItem
						        .getModelObject();
						Game _game = item.getModelObject();

						PageParameters params = new PageParameters();
						params.add("id", Long
						        .toString(realm.getId()));

						Link<Realm> realmLink = new BookmarkablePageLink<Realm>(
						        "realm", RealmPage.class,
						        params);

						realmLink.add(new Label("name",
						        realm.getName()));

						innerItem
						        .add(new Label(
						                "playercount",
						                Integer
						                        .toString(gameService
						                                .countPlayers(
						                                        _game,
						                                        realm))));

						innerItem.add(realmLink);

						iconContainer
						        .add(new Image(
						                "icon",
						                new ByteArrayResource(
						                        ImageUtil
						                                .getMimeType(_game
						                                        .getImage()),
						                        _game
						                                .getImage())));

						if (_game.getCoordinator() != null) {

							supervisorContainer
							        .add(new MemberListItem(
							                "supervisor",
							                _game
							                        .getCoordinator()));
						} else {
							supervisorContainer
							        .add(new Label(
							                "supervisor",
							                "-"));
						}

					}
				});

			}
		});

		UserFilter filter3 = new UserFilter();
		filter3.addRank(Rank.CHANCELLOR);
		filter3.addRank(Rank.SENATOR);
		filter3.addRank(Rank.TRUTHSAYER);
		filter3.addRank(Rank.REVERED_MEMBER);
		filter3.addRank(Rank.SENIOR_MEMBER);
		filter3.addRank(Rank.FULL_MEMBER);
		filter3.addRank(Rank.JUNIOR_MEMBER);
		filter3.addRank(Rank.TRIAL);
		filter3.setRetired(false);

		add(new Label("activecount", new Model<Integer>(
		        userDAO.countByFilter(filter3))));

		UserFilter filter4 = new UserFilter();
		filter4.addRank(Rank.CHANCELLOR);
		filter4.addRank(Rank.SENATOR);
		filter4.addRank(Rank.TRUTHSAYER);
		filter4.addRank(Rank.REVERED_MEMBER);
		filter4.addRank(Rank.SENIOR_MEMBER);
		filter4.addRank(Rank.FULL_MEMBER);
		filter4.addRank(Rank.JUNIOR_MEMBER);
		filter4.addRank(Rank.TRIAL);

		add(new Label("membercount", new Model<Integer>(
		        userDAO.countByFilter(filter4))));

		addRank(Rank.CHANCELLOR,
		        "The leader of the Clan, has executive power");
		addRank(Rank.SENATOR,
		        "A member of the Senate, has legislative power");
		addRank(Rank.TRUTHSAYER,
		        "Upholder of justice, has judiciary power");
		addRank(Rank.REVERED_MEMBER,
		        "A member who has been with Tysan for more than 5 years");
		addRank(Rank.SENIOR_MEMBER,
		        "A member who has been with Tysan for more than 2 years");
		addRank(Rank.FULL_MEMBER,
		        "A member who has been with Tysan for more than 4 months");
		addRank(
		        Rank.JUNIOR_MEMBER,
		        "A member who has passed his Trial period and gained at least 60% votes in favor");
		addRank(Rank.TRIAL,
		        "A member who is currently in his Trial period");

		add(new ListView<Rank>("ranks", ranks) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Rank> item) {
				Rank rank = item.getModelObject();
				item.add(new RankIcon("icon", rank));
				item
				        .add(new Label("name", rank
				                .toString()));
				item.add(new Label("description",
				        rankDescriptions.get(rank)));

			}

		});

	}

	
	private void addRank(Rank rank, String description) {
		rankDescriptions.put(rank, description);
		ranks.add(rank);

	}
}
