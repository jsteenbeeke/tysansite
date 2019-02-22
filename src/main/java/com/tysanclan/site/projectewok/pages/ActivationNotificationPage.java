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
package com.tysanclan.site.projectewok.pages;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.MailService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.Activation;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.StringUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class ActivationNotificationPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public ActivationNotificationPage(Activation activation) {
		super("Account not yet active");

		if (activation == null || activation.getUser() == null) {
			throw new RestartResponseAtInterceptPageException(NewsPage.class);
		}

		Form<Activation> activationForm = new Form<Activation>("resendform",
				ModelMaker.wrap(activation)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			@SpringBean
			private MailService mailService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> mailField = (TextField<String>) get("email");

				if (StringUtil.isValidEMail(mailField.getModelObject())) {

					User user = getModelObject().getUser();

					if (userService.setUserMail(user,
							mailField.getModelObject())) {

						mailService.sendHTMLMail(user.getEMail(),
								"Tysan Clan Forums", mailService
										.getActivationMailBody(user
												.getUsername(),
												getModelObject()
														.getActivationKey()));

						info("Activation e-mail has been sent again!");
					} else {
						error("That e-mail address is already in use for another account!");
					}

				} else {
					error("Please specify a valid e-mail address!");
				}

			}

		};

		activationForm.add(new TextField<String>("email", new Model<String>(
				activation.getUser().getEMail())));

		add(activationForm);
	}
}
