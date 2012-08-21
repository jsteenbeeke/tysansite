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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.TysanTopPanel;
import com.tysanclan.site.projectewok.beans.AuthenticationService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ActivationDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ActivationFilter;
import com.tysanclan.site.projectewok.pages.ActivationNotificationPage;
import com.tysanclan.site.projectewok.pages.PasswordRequestPage;

public class TysanLoginPanel extends TysanTopPanel {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(TysanLoginPanel.class);

	@SpringBean
	private AuthenticationService authenticationService;

	@SpringBean
	private UserDAO userDAO;

	public TysanLoginPanel(String id) {
		super(id);

		final TextField<String> username = new TextField<String>("username",
				new Model<String>(""));
		final PasswordTextField password = new PasswordTextField("password",
				new Model<String>(""));
		final Button submit = new Button("submitter",
				new Model<String>("Login"));

		final Form<Void> form = new Form<Void>("loginform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ActivationDAO activationDAO;

			@SpringBean
			private MembershipService membershipService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onValidate()
			 */
			@Override
			protected void onValidate() {
				super.onValidate();
			}

			@Override
			protected void onSubmit() {
				String un = username.getModelObject();
				String pw = password.getModelObject();

				boolean validMember = authenticationService.isValidMember(un,
						pw);
				// Performance step, do not check valid user if already valid
				// member
				boolean validUser = !validMember
						&& authenticationService.isValidUser(un, pw);

				if (validMember || validUser) {
					User u = userDAO.load(un, pw);

					log.info("Valid login for " + un);

					if (u != null) {
						ActivationFilter filter = new ActivationFilter();
						filter.setUser(u);

						int activationCount = activationDAO
								.countByFilter(filter);

						if (activationCount > 0) {
							setResponsePage(new ActivationNotificationPage(
									activationDAO.findByFilter(filter).get(0)));
						} else {
							TysanSession session = ((TysanPage) getPage())
									.getTysanSession();
							session.setPreviousLogin(u.getLastAction());
							session.setCurrentUserId(u.getId());

							if (validMember) {
								setResponsePage(new com.tysanclan.site.projectewok.pages.member.OverviewPage());
							} else {
								setResponsePage(new com.tysanclan.site.projectewok.pages.forum.OverviewPage());
							}
						}

						membershipService.onLogin(u);
					} else {
						error("Unable to fetch user object");
					}

				} else {
					error("Username or password incorrect");

					setResponsePage(new PasswordRequestPage(un));
				}
			}
		};

		add(form);

		form.add(username);
		form.add(password);
		form.add(submit);
	}

	/**
	 * @return The authentication service used by this panel
	 */
	public AuthenticationService getAuthenticationService() {
		return authenticationService;
	}

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Override
	protected void onDetach() {
		super.onDetach();

		authenticationService = null;
		userDAO = null;
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * @param userDAO
	 *            the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
