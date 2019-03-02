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

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.AuthenticationService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import io.vavr.collection.Seq;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
public class DebugWindow extends Panel {
	private static final long serialVersionUID = 1L;

	public DebugWindow(String id, Class<? extends Page> page) {
		super(id);

		add(new Label("page", page.getName()));

		Form<?> debugLoginForm = new Form<Void>("debugLogin") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private AuthenticationService authService;

			@SpringBean
			private UserDAO userDAO;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> devUsernameField = (TextField<String>) get(
						"devusername");
				TextField<String> usernameField = (TextField<String>) get(
						"username");
				PasswordTextField passwordField = (PasswordTextField) get(
						"password");

				String devUsername = devUsernameField.getModelObject();
				String username = usernameField.getModelObject();
				String password = passwordField.getModelObject();

				boolean validUser = authService
						.isValidUser(devUsername, password);
				boolean validMember = authService
						.isValidMember(devUsername, password);

				if (validUser || validMember) {
					UserFilter filter = new UserFilter();
					filter.username(username);

					Seq<User> users = userDAO.findByFilter(filter);

					if (!users.isEmpty()) {
						User user = users.get(0);
						((TysanPage) getPage()).getTysanSession()
								.forEach(session -> {
									session.setCurrentUserId(user.getId());
									if (validMember) {
										setResponsePage(
												new com.tysanclan.site.projectewok.pages.member.OverviewPage());
									} else {
										setResponsePage(
												new com.tysanclan.site.projectewok.pages.forum.OverviewPage());
									}
								});

					}
				}

			}
		};

		debugLoginForm.add(new TextField<>("devusername", new Model<>("")));
		debugLoginForm.add(new TextField<>("username", new Model<>("")));
		debugLoginForm.add(new PasswordTextField("password", new Model<>("")));

		add(debugLoginForm);
	}
}
