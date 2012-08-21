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

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.tysanclan.site.projectewok.entities.MumbleServer;
import com.tysanclan.site.projectewok.ws.mumble.MMOMumbleServerStatus;
import com.tysanclan.site.projectewok.ws.mumble.MumbleUser;
import com.tysanclan.site.projectewok.ws.mumble.ServerStatus;

public class MumbleServerStatusPage extends AbstractSingleAccordionMemberPage {

	public MumbleServerStatusPage(MumbleServer server) {
		super("Server Information: " + server.getName());

		getAccordion().add(new Label("name", server.getName()));
		getAccordion().add(
				new Label("url", server.getUrl())
						.add(new SimpleAttributeModifier("href", server
								.getUrl())));
		getAccordion().add(new Label("password", server.getPassword()));

		ServerStatus status = MMOMumbleServerStatus.getServerStatus(
				server.getServerID(), server.getApiToken(),
				server.getApiSecret());

		getAccordion().add(
				new ListView<MumbleUser>("users", status.getUsers()) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<MumbleUser> mui) {
						MumbleUser mu = mui.getModelObject();
						mui.add(new ContextImage("icon",
								"images/icons/mumble.png"));
						mui.add(new Label("name", mu.getName()));
					}

				});
	}

}
