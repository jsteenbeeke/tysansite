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
package com.tysanclan.site.projectewok.pages.forum;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
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
	@SpringBean
	private ActivationDAO activationDAO;

	/**
     * 
     */
	public ActivationPage() {
		super("Account activation");

		PageParameters params = RequestCycle.get()
		        .getPageParameters();

		String key = params.getString("key");

		ActivationFilter filter = new ActivationFilter();
		filter.setKey(key);
		List<Activation> activations = activationDAO
		        .findByFilter(filter);

		if (activations.isEmpty()) {
			throw new RestartResponseAtInterceptPageException(
			        AccessDeniedPage.class);
		}

		Activation activation = activations.get(0);

		add(new Label("username", activation.getUser()
		        .getUsername()));

		add(new Form<Activation>("activation", ModelMaker
		        .wrap(activation)) {

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
				if (userService
				        .activateAccount(getModelObject())) {
					setResponsePage(new AccountActivePage(
					        getModelObject().getUser()));
				} else {
					error("Could not activate account, perhaps it was already activated?");
				}
			}

		});

	}
}
