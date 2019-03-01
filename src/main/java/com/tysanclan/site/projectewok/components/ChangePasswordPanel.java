/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.components;

import com.jeroensteenbeeke.hyperion.password.argon2.Argon2PasswordHasher;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.util.HashException;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class ChangePasswordPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	public ChangePasswordPanel(String id, User user) {
		super(id);

		Form<User> changeForm = new Form<User>("changeForm",
											   ModelMaker.wrap(user)) {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				User u = getModelObject();

				PasswordTextField oldField = (PasswordTextField) get("old");
				PasswordTextField newField1 = (PasswordTextField) get("new1");
				PasswordTextField newField2 = (PasswordTextField) get("new2");

				String oldPW = oldField.getModelObject();
				String newPW1 = newField1.getModelObject();
				String newPW2 = newField2.getModelObject();

				boolean valid = true;

				if (oldPW == null
						|| !Argon2PasswordHasher.checkExistingPassword(oldPW.toCharArray()).withHash(u.getArgon2hash())) {
					valid = false;
					warn("Old password invalid");
				}

				if (newPW1 == null || newPW2 == null || newPW1.length() < 8
						|| newPW2.length() < 8) {
					valid = false;
					warn("New password invalid");
				}

				if (newPW1 != null && newPW2 != null
						&& !newPW1.equals(newPW2)) {
					valid = false;
					warn("New passwords do not match!");
				}

				if (valid) {
					userService.setUserPassword(u, newPW1);

					info("Password succesfully changed");

					onChanged();
				}
			}
		};

		changeForm.add(new PasswordTextField("old", new Model<String>("")));
		changeForm.add(new PasswordTextField("new1", new Model<String>("")));
		changeForm.add(new PasswordTextField("new2", new Model<String>("")));

		add(changeForm);
	}

	public abstract void onChanged();
}
