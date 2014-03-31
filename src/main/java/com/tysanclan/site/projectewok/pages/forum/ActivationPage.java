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
package com.tysanclan.site.projectewok.pages.forum;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.Activation;
import com.tysanclan.site.projectewok.entities.dao.ActivationDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ActivationFilter;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;

/**
 * @author Jeroen Steenbeeke
 */
public class ActivationPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public static class ActivationPageParams {
		private final String key;

		public ActivationPageParams(String key) {
			super();
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	@SpringBean
	private ActivationDAO activationDAO;

	/**
	 * 
	 */
	public ActivationPage(PageParameters params) {
		super("Account activation");

		ActivationPageParams parameters;
		try {
			parameters = requiredString("key").forParameters(params).toClass(
					ActivationPageParams.class);
		} catch (PageParameterExtractorException e) {
			throw new AbortWithHttpErrorCodeException(
					HttpServletResponse.SC_NOT_FOUND);
		}

		ActivationFilter filter = new ActivationFilter();
		filter.setKey(parameters.getKey());
		List<Activation> activations = activationDAO.findByFilter(filter);

		if (activations.isEmpty()) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		Activation activation = activations.get(0);

		add(new Label("username", activation.getUser().getUsername()));

		add(new Form<Activation>("activation", ModelMaker.wrap(activation)) {

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
				if (userService.activateAccount(getModelObject())) {
					setResponsePage(new AccountActivePage(getModelObject()
							.getUser()));
				} else {
					error("Could not activate account, perhaps it was already activated?");
				}
			}

		});

	}
}
