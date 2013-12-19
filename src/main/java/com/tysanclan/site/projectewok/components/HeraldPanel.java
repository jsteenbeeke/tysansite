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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.member.PaypalSettingsPage;
import com.tysanclan.site.projectewok.pages.member.admin.HeraldTransferPage;
import com.tysanclan.site.projectewok.pages.member.admin.MumbleServerAdminPage;
import com.tysanclan.site.projectewok.pages.member.admin.RequestPaymentPage;

public class HeraldPanel extends TysanOverviewPanel<User> {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	public HeraldPanel(String id, User user) {
		super(id, "Herald");

		setVisible(user.equals(roleService.getHerald()));

		add(createLink("paypal", PaypalSettingsPage.class, "PayPal settings",
				new DoesNotHavePaypalCondition(user)));

		add(new Link<User>("requestPayment") {
			private static final long serialVersionUID = 1L;

			/**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
			@Override
			public void onClick() {
				setResponsePage(new RequestPaymentPage(getUser()));
			}
		});

		add(new Link<User>("transfer") {
			private static final long serialVersionUID = 1L;

			/**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
			@Override
			public void onClick() {
				setResponsePage(new HeraldTransferPage());
			}
		});

		add(new Link<User>("mumble", ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			/**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
			@Override
			public void onClick() {
				setResponsePage(new MumbleServerAdminPage(getModelObject()));
			}
		});

	}
}
