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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.MemberPage;

/**
 * @author Jeroen Steenbeeke
 */
public class MemberListItem extends Panel {
	private static final long serialVersionUID = 1L;

	private IModel<User> userModel;

	public MemberListItem(String id, User user) {
		super(id);

		userModel = ModelMaker.wrap(user);

		add(new RankIcon("icon", user.getRank()));

		PageParameters params = new PageParameters();
		params.add("userid", user.getId().toString());
		Link<User> link = new BookmarkablePageLink<User>("profile",
				MemberPage.class, params);

		link.add(new Label("username", new Model<String>(user.getUsername())));
		add(link);
	}

	public User getUser() {
		return userModel.getObject();
	}

	/**
	 * @see org.apache.wicket.Component#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		userModel.detach();
	}
}
