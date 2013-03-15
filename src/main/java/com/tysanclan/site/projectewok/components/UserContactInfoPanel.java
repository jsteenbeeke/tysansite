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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.UserGameRealm;

/**
 * @author Jeroen Steenbeeke
 */
public class UserContactInfoPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public UserContactInfoPanel(String id, User user) {
		super(id, ModelMaker.wrap(user));

		add(new MemberListItem("user", user));

		String aimAddress = user.getProfile() != null ? user.getProfile()
				.getInstantMessengerAddress() : "";

		add(new Label("aim", aimAddress).add(
				AttributeModifier.replace("href", "aim:addbuddy?screenname="
						+ aimAddress)).setVisible(
				aimAddress != null && !aimAddress.isEmpty()));
		add(new DateLabel("lastlogin", user.getLastAction()));

		WebMarkupContainer accounts = new WebMarkupContainer("accounts");

		accounts.add(new ListView<UserGameRealm>("realms", ModelMaker.wrap(user
				.getPlayedGames())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<UserGameRealm> item) {
				item.setModel(new CompoundPropertyModel<UserGameRealm>(item
						.getModel()));

				StringBuilder builder = new StringBuilder();

				for (GameAccount acc : item.getModelObject().getAccounts()) {
					if (builder.length() > 0) {
						builder.append(", ");
					}

					builder.append(acc.toString());
				}

				item.add(new Label("realm.name"));
				item.add(new Label("accountlist",
						builder.length() > 0 ? builder.toString() : "-"));
			}
		});

		accounts.setVisible(!user.getPlayedGames().isEmpty());

		add(accounts);
	}
}
