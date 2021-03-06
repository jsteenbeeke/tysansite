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

import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ProfileDAO;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class SkypeOverviewPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ProfileDAO profileDAO;

	public SkypeOverviewPage() {
		super("Skype accounts");

		add(new ListView<User>("users", profileDAO.getSkypeUsers()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				User user = item.getModelObject();
				item.add(new MemberListItem("username", user));
				String skypeAddress = user.getProfile()
						.getInstantMessengerAddress();

				item.add(new Label("skype", skypeAddress).add(AttributeModifier
						.replace("href",
								String.format("skype:%s?call", skypeAddress))));
			}

		});

	}
}
