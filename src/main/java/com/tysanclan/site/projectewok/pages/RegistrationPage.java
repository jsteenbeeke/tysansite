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

import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.MailService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.Activation;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.util.StringUtil;

public class RegistrationPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserService userBean;

	private final String passId;

	private CaptchaImageResource resource;

	public RegistrationPage() {
		super("Forum Registration");

		passId = randomString(6, 8);

		resource = new CaptchaImageResource(passId);

		final TextField<String> tfUsername = new TextField<String>("username",
				new Model<String>(""));
		tfUsername.setRequired(true);
		final TextField<String> tfMail = new TextField<String>("mail",
				new Model<String>(""));
		tfMail.setRequired(true);
		final PasswordTextField tfPassword = new PasswordTextField("password",
				new Model<String>(""));
		tfPassword.setRequired(true);
		final PasswordTextField tfPassword2 = new PasswordTextField(
				"password2", new Model<String>(""));
		tfPassword2.setRequired(true);

		final TextField<String> tfCaptcha = new TextField<String>(
				"captchaResponse", new Model<String>(""));
		tfCaptcha.setRequired(true);

		Form<?> registrationForm = new Form<Void>("registerform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private MailService mailService;

			@SpringBean
			private UserDAO userDAO;

			@Override
			protected void onSubmit() {
				UserService ub = getUserBean();
				boolean valid = true;

				if (ub.hasUser(tfUsername.getModelObject())) {
					valid = false;
					RegistrationPage.this.error("Username already taken");
				}
				if (valid) {
					String username = tfUsername.getModelObject();
					for (int i = 0; i < username.length(); i++) {
						if (!Character.isLetterOrDigit(username.charAt(i))) {
							valid = false;
							RegistrationPage.this
									.error("Username may only contain letters and digits");
							break;
						}
					}
					if (valid) {
						if (username.length() < 2) {
							valid = false;
							RegistrationPage.this.error("Username too short");
						}
						if (valid) {
							if (!Character.isLetter(username.charAt(0))) {
								valid = false;
								RegistrationPage.this
										.error("Username must start with a letter");
							}
						}
					}
				}
				if (valid && tfPassword.getModelObject().length() < 8) {
					valid = false;
					RegistrationPage.this
							.error("Password must be at least 8 characters");
				}
				if (valid
						&& !tfPassword.getModelObject().equals(
								tfPassword2.getModelObject())) {
					valid = false;
					RegistrationPage.this.error("Passwords do not match");
				}
				if (valid && !StringUtil.isValidEMail(tfMail.getModelObject())) {
					valid = false;
					RegistrationPage.this
							.error("Please provide a valid e-mail address");
				}
				if (valid) {
					UserFilter filter = new UserFilter();
					filter.setEmail(tfMail.getModelObject());
					long users = userDAO.countByFilter(filter);
					if (users != 0) {
						valid = false;
						RegistrationPage.this
								.error("That e-mail address is already in use");
					}

				}

				if (!passId.equals(tfCaptcha.getModelObject())) {
					resource.invalidate();
					valid = false;
					RegistrationPage.this.error("Challenge response invalid");
				}

				if (valid) {
					User user = ub.createUser(tfUsername.getModelObject(),
							tfPassword.getModelObject(),
							tfMail.getModelObject());
					if (user != null) {
						Activation activation = ub.getActivationByUser(user);

						mailService.sendHTMLMail(
								tfMail.getModelObject(),
								"Tysan Clan Forums",
								mailService.getActivationMailBody(
										user.getUsername(),
										activation.getActivationKey()));

						info("You have succesfully registered as "
								+ tfUsername.getModelObject());
						tfUsername.clearInput();
						tfMail.clearInput();

						setResponsePage(new ForumOverviewPage());

					}
				}
			}
		};

		registrationForm.add(tfUsername);
		registrationForm.add(tfPassword);
		registrationForm.add(tfPassword2);
		registrationForm.add(tfMail);
		registrationForm.add(tfCaptcha);
		registrationForm.add(new Image("captcha", resource));

		add(registrationForm);
	}

	private static int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	private static String randomString(int min, int max) {
		int num = randomInt(min, max);
		byte b[] = new byte[num];
		for (int i = 0; i < num; i++)
			b[i] = (byte) randomInt('a', 'z');
		return new String(b);
	}

	/**
	 * @return the userBean
	 */
	public UserService getUserBean() {
		return userBean;
	}

	/**
	 * @param userBean
	 *            the userBean to set
	 */
	public void setUserBean(UserService userBean) {
		this.userBean = userBean;
	}

}
