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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.User;

public class PaypalSettingsPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	public PaypalSettingsPage(User user) {
		super("PayPal settings");

		final TextField<String> paypalAddress = new TextField<String>(
				"address", new Model<String>(user.getPaypalAddress()));

		Form<User> settingsForm = new Form<User>("form") {
			private static final long serialVersionUID = 1L;
			@SpringBean
			private UserService userService;

			@Override
			protected void onSubmit() {
				userService.setPaypalAddress(getUser(),
						paypalAddress.getModelObject());

				setResponsePage(new OverviewPage());
			}
		};

		settingsForm.add(paypalAddress);

		getAccordion().add(settingsForm);

	}
}
