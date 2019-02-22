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

import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.MailService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.PasswordRequest;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.PasswordRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.PasswordRequestFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;

/**
 * @author Jeroen Steenbeeke
 */
public class PasswordRequestPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	public PasswordRequestPage(String username) {
		super("Request Password");

		UserFilter filter = new UserFilter();
		filter.setUsername(username);

		String _username = username;

		User user = null;

		List<User> users = userDAO.findByFilter(filter);
		if (!users.isEmpty()) {
			user = users.get(0);
		}

		Form<User> requestPasswordForm = new Form<User>("requestpassword",
				ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private MailService mailService;

			@SpringBean
			private UserService userService;

			@SpringBean
			private PasswordRequestDAO passwordRequestDAO;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				User u = getModelObject();

				PasswordRequestFilter f = new PasswordRequestFilter();
				f.setUser(u);

				List<PasswordRequest> requests = passwordRequestDAO
						.findByFilter(f);

				if (u != null && requests.isEmpty()) {
					PasswordRequest request = userService
							.generatePasswordRequest(u);

					String requestBody = mailService
							.getPasswordRequestMailBody(u.getUsername(),
									request.getKey());
					mailService.sendHTMLMail(u.getEMail(),
							"Tysan Clan Password Reset", requestBody);
				}

				info("Password reset mail sent!");
			}

		};

		requestPasswordForm.setVisible(user != null);

		requestPasswordForm.add(new TextField<String>("username",
				new Model<String>(_username)).setEnabled(false));

		add(requestPasswordForm);

	}
}
