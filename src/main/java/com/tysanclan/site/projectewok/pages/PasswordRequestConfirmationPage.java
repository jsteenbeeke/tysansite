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

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.PasswordRequest;
import com.tysanclan.site.projectewok.entities.dao.PasswordRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.PasswordRequestFilter;

/**
 * @author Jeroen Steenbeeke
 */
public class PasswordRequestConfirmationPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private PasswordRequestDAO passwordRequestDAO;

	/**
	 * 
	 */
	public PasswordRequestConfirmationPage(PageParameters params) {
		super("Reset password");
		String key = params.get("key").toOptionalString();

		if (key == null) {
			throw new RestartResponseAtInterceptPageException(NewsPage.class);
		}

		PasswordRequestFilter filter = new PasswordRequestFilter();
		filter.setKey(key);

		List<PasswordRequest> requests = passwordRequestDAO
				.findByFilter(filter);
		if (requests.isEmpty()) {
			throw new RestartResponseAtInterceptPageException(NewsPage.class);
		}

		PasswordRequest request = requests.get(0);

		Form<PasswordRequest> requestForm = new Form<PasswordRequest>(
				"requestform", ModelMaker.wrap(request)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				boolean valid = true;

				PasswordTextField tfPassword = (PasswordTextField) get("password");
				PasswordTextField tfPassword2 = (PasswordTextField) get("password2");

				if (valid && tfPassword.getModelObject().length() < 8) {
					valid = false;
					error("Password must be at least 8 characters");
				}
				if (valid
						&& !tfPassword.getModelObject().equals(
								tfPassword2.getModelObject())) {
					valid = false;
					error("Passwords do not match");
				}

				if (valid) {
					userService.processPasswordReset(getModelObject(),
							tfPassword.getModelObject());

					info("Password successfully changed");

					setResponsePage(NewsPage.class);
				}
			}

		};

		requestForm
				.add(new PasswordTextField("password", new Model<String>("")));
		requestForm.add(new PasswordTextField("password2",
				new Model<String>("")));

		add(requestForm);

	}
}
