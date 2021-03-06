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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.PasswordRequest;
import com.tysanclan.site.projectewok.entities.dao.PasswordRequestDAO;
import com.tysanclan.site.projectewok.entities.filter.PasswordRequestFilter;
import io.vavr.collection.Seq;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jeroen Steenbeeke
 */
public class PasswordRequestConfirmationPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public static class PasswordRequestConfirmationPageParams {
		private final String key;

		public PasswordRequestConfirmationPageParams(String key) {
			super();
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	@SpringBean
	private PasswordRequestDAO passwordRequestDAO;

	/**
	 *
	 */
	public PasswordRequestConfirmationPage(PageParameters params) {
		super("Reset password");

		PasswordRequestConfirmationPageParams parameters;
		try {
			parameters = requiredString("key").forParameters(params)
					.toClass(PasswordRequestConfirmationPageParams.class);
		} catch (PageParameterExtractorException e) {
			throw new AbortWithHttpErrorCodeException(
					HttpServletResponse.SC_NOT_FOUND);
		}

		PasswordRequestFilter filter = new PasswordRequestFilter();
		filter.key(parameters.getKey());

		Seq<PasswordRequest> requests = passwordRequestDAO.findByFilter(filter);
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

				PasswordTextField tfPassword = (PasswordTextField) get(
						"password");
				PasswordTextField tfPassword2 = (PasswordTextField) get(
						"password2");

				if (valid && tfPassword.getModelObject().length() < 8) {
					valid = false;
					error("Password must be at least 8 characters");
				}
				if (valid && !tfPassword.getModelObject()
						.equals(tfPassword2.getModelObject())) {
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
		requestForm
				.add(new PasswordTextField("password2", new Model<String>("")));

		add(requestForm);

	}
}
