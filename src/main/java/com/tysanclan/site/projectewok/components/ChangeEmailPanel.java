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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class ChangeEmailPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
	public ChangeEmailPanel(String id, User user) {
		super(id);

		Form<User> changeMailForm = new Form<User>(
		        "changemailform", ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> mailField = (TextField<String>) get("mail");
				User u = getModelObject();

				String email = mailField.getModelObject();

				if (email != null
				        && !email.equals(u.getEMail())) {
					userService.createEmailChangeRequest(
					        u, email);
					info("Confirmation mail sent");
				}

				ChangeEmailPanel.this.onSubmit();
			}

		};

		changeMailForm.add(new TextField<String>("mail",
		        new Model<String>(user.getEMail())));

		add(changeMailForm);
	}

	public abstract void onSubmit();
}
