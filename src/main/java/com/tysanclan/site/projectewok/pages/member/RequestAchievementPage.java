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

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.StoredImageResource;
import com.tysanclan.site.projectewok.entities.Achievement;
import com.tysanclan.site.projectewok.entities.AchievementRequest;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.AchievementDAO;
import com.tysanclan.site.projectewok.util.AchievementComparator;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class RequestAchievementPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private AchievementDAO achievementDAO;

	public RequestAchievementPage() {
		super("Request Achievement");

		User user = getUser();

		Set<Game> games = new HashSet<Game>();

		List<Achievement> available = achievementDAO.findAll();
		available.removeAll(user.getAchievements());

		for (UserGameRealm ugr : user.getPlayedGames()) {
			games.add(ugr.getGame());
		}

		Set<Achievement> remove = new HashSet<Achievement>();

		for (AchievementRequest r : user.getAchievementRequests()) {
			remove.add(r.getAchievement());
		}

		for (Achievement a : available) {
			if (a.getGame() != null) {
				if (!games.contains(a.getGame())) {
					remove.add(a);
				}
			}
			if (a.getGroup() != null) {
				if (!user.getGroups().contains(a.getGroup())) {
					remove.add(a);
				}
			}
		}
		available.removeAll(remove);

		Collections.sort(available, AchievementComparator.INSTANCE);

		add(
				new ListView<Achievement>("achievements", ModelMaker
						.wrap(available)) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<Achievement> item) {
						Achievement achievement = item.getModelObject();

						item.add(new Image("icon", new StoredImageResource(
								achievement.getIcon().getImage())));

						if (achievement.getGame() != null) {
							item.add(new Image("game", new StoredImageResource(
									achievement.getGame().getImage())));
						} else {
							item.add(new WebMarkupContainer("game")
									.setVisible(false));
						}

						if (achievement.getGroup() != null) {
							item.add(new Label("group", achievement.getGroup()
									.getName()));
						} else {
							item.add(new Label("group", "-"));
						}

						item.add(new Label("name", achievement.getName()));
						item.add(new Label("description", achievement
								.getDescription()).setEscapeModelStrings(false));
						item.add(new IconLink.Builder(
								"images/icons/accept.png",
								new DefaultClickResponder<Achievement>(
										ModelMaker.wrap(achievement)) {

									private static final long serialVersionUID = 1L;

									@Override
									public void onClick() {
										setResponsePage(new RequestAchievementPage2(
												getModelObject()));
									}

								}).newInstance("request"));
					}

				});
	}

}
