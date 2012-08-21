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
package com.tysanclan.site.projectewok.components;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Achievement;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.AchievementComparator;

/**
 * @author Jeroen Steenbeeke
 */
public class AchievementsPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public AchievementsPanel(String id, User user) {
		super(id);

		List<Achievement> achievements = new LinkedList<Achievement>();

		achievements.addAll(user.getAchievements());

		Collections.sort(achievements, AchievementComparator.INSTANCE);

		add(new ListView<Achievement>("achievements",
				ModelMaker.wrap(achievements)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Achievement> item) {
				Achievement a = item.getModelObject();

				item.add(new Image("achievement", new DynamicImageResource() {

					private static final long serialVersionUID = 1L;

					@Override
					protected byte[] getImageData() {
						return item.getModelObject().getIcon().getImage();
					}
				}));

				item.add(new Label("name", a.getName()));

				item.add(new Label("group", a.getGroup() != null ? a.getGroup()
						.getName() : ""));

				item.add(new Image("game", new DynamicImageResource() {

					private static final long serialVersionUID = 1L;

					@Override
					protected byte[] getImageData() {
						return item.getModelObject().getGame().getImage();
					}
				}).setVisible(a.getGame() != null));

				item.add(new Label("description", a.getDescription())
						.setEscapeModelStrings(false));
			}
		});

	}
}
