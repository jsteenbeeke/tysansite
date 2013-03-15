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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class CharterPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService service;

	public CharterPage() {
		super("Charter");

		User herald = service.getHerald();
		User steward = service.getSteward();
		User treasurer = service.getTreasurer();

		if (herald != null) {
			add(new MemberListItem("herald", herald));
		} else {
			add(new Label("herald", "vacant"));
		}
		if (steward != null) {
			add(new MemberListItem("steward", steward));
		} else {
			add(new Label("steward", "vacant"));
		}
		if (treasurer != null) {
			add(new MemberListItem("treasurer", treasurer));
		} else {
			add(new Label("treasurer", "vacant"));
		}
	}
}
