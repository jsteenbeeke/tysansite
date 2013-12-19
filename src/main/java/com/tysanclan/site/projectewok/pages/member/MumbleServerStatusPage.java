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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;

import com.tysanclan.site.projectewok.entities.MumbleServer;

public class MumbleServerStatusPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	public MumbleServerStatusPage(MumbleServer server) {
		super("Server Information: " + server.getName());

		add(new Label("name", server.getName()));
		add(
				new Label("url", server.getUrl()).add(AttributeModifier
						.replace("href", server.getUrl())));
		add(new Label("password", server.getPassword()));
	}

}
