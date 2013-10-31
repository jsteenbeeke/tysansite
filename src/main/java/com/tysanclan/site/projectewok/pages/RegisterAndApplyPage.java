package com.tysanclan.site.projectewok.pages;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.beans.MailService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.components.renderer.GameRealmCartesianRenderer;
import com.tysanclan.site.projectewok.entities.Activation;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.model.GameRealmCartesian;
import com.tysanclan.site.projectewok.util.HTMLSanitizer;
import com.tysanclan.site.projectewok.util.StringUtil;

public class RegisterAndApplyPage extends TysanPage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	private final String passId;

	private CaptchaImageResource resource;

	public RegisterAndApplyPage() {
		super("Join Tysan");

		List<Game> games = gameService.getActiveGames();

		List<GameRealmCartesian> grlms = new LinkedList<GameRealmCartesian>();

		for (Game game : games) {
			for (Realm realm : game.getRealms()) {
				grlms.add(new GameRealmCartesian(game, realm));
			}
		}

		final DropDownChoice<GameRealmCartesian> realmChoice = new DropDownChoice<GameRealmCartesian>(
				"gamerealm", new Model<GameRealmCartesian>(null), grlms,
				new GameRealmCartesianRenderer());
		realmChoice.setRequired(true);
		realmChoice.setNullValid(false);

		final TextArea<String> otherGamesDescription = new TextArea<String>(
				"othergames", new Model<String>(""));
		otherGamesDescription.setRequired(true);

		final TextArea<String> sortOfPersonArea = new TextArea<String>(
				"sortofperson", new Model<String>(""));
		sortOfPersonArea.setRequired(true);

		final TextArea<String> lookingForArea = new TextArea<String>(
				"lookingfor", new Model<String>(""));
		lookingForArea.setRequired(true);

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

		Form<User> registrationForm = new Form<User>("registerform") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			@SpringBean
			private MailService mailService;

			@SpringBean
			private UserDAO userDAO;

			@SpringBean
			private MembershipService membershipService;

			@Override
			protected void onSubmit() {
				boolean valid = true;

				if (userService.hasUser(tfUsername.getModelObject())) {
					valid = false;
					RegisterAndApplyPage.this.error("Username already taken");
				}
				if (valid) {
					String username = tfUsername.getModelObject();
					for (int i = 0; i < username.length(); i++) {
						if (!Character.isLetterOrDigit(username.charAt(i))) {
							valid = false;
							RegisterAndApplyPage.this
									.error("Username may only contain letters and digits");
							break;
						}
					}
					if (valid) {
						if (username.length() < 2) {
							valid = false;
							RegisterAndApplyPage.this
									.error("Username too short");
						}
						if (valid) {
							if (!Character.isLetter(username.charAt(0))) {
								valid = false;
								RegisterAndApplyPage.this
										.error("Username must start with a letter");
							}
						}
					}
				}
				if (valid && tfPassword.getModelObject().length() < 8) {
					valid = false;
					RegisterAndApplyPage.this
							.error("Password must be at least 8 characters");
				}
				if (valid
						&& !tfPassword.getModelObject().equals(
								tfPassword2.getModelObject())) {
					valid = false;
					RegisterAndApplyPage.this.error("Passwords do not match");
				}
				if (valid && !StringUtil.isValidEMail(tfMail.getModelObject())) {
					valid = false;
					RegisterAndApplyPage.this
							.error("Please provide a valid e-mail address");
				}
				if (valid) {
					UserFilter filter = new UserFilter();
					filter.setEmail(tfMail.getModelObject());
					long users = userDAO.countByFilter(filter);
					if (users != 0) {
						valid = false;
						RegisterAndApplyPage.this
								.error("That e-mail address is already in use");
					}

				}

				if (!passId.equals(tfCaptcha.getModelObject())) {
					resource.invalidate();
					valid = false;
					RegisterAndApplyPage.this
							.error("Challenge response invalid");
				}

				Game game = null;
				Realm realm = null;

				String otherGames = HTMLSanitizer
						.stripTags(otherGamesDescription.getModelObject());
				String sortOfPerson = HTMLSanitizer.stripTags(sortOfPersonArea
						.getModelObject());
				String lookingFor = HTMLSanitizer.stripTags(lookingForArea
						.getModelObject());

				if (StringUtil.countWords(sortOfPerson) < 30) {
					RegisterAndApplyPage.this
							.error("Please describe the sort of person you are in at least 30 words");
					valid = false;
				}

				if (StringUtil.countWords(lookingFor) < 30) {
					RegisterAndApplyPage.this
							.error("Please describe the sort of clan you are looking for you are in at least 30 words");
					valid = false;
				}

				if (valid) {

					GameRealmCartesian cart = realmChoice.getModelObject();

					if (cart != null) {
						game = cart.getGame();
						realm = cart.getRealm();
					} else {
						valid = false;
					}

				}

				if (valid) {
					User user = userService.createUser(
							tfUsername.getModelObject(),
							tfPassword.getModelObject(),
							tfMail.getModelObject());
					if (user != null) {
						Activation activation = userService
								.getActivationByUser(user);

						mailService.sendHTMLMail(
								tfMail.getModelObject(),
								"Tysan Clan Forums",
								mailService.getActivationMailBody(
										user.getUsername(),
										activation.getActivationKey()));

						StringBuilder motivation = new StringBuilder();
						motivation
								.append("<strong>What sort of person are you?</strong><br />\n");
						motivation.append(sortOfPerson);
						motivation.append("<br /><br />");

						motivation
								.append("<strong>What are you looking for in a clan?</strong><br />\n");
						motivation.append(lookingFor);
						motivation.append("<br /><br />");

						motivation
								.append("<strong>What other games do you play?</strong><br />\n");
						motivation.append(otherGames);
						motivation.append("<br /><br />");

						ForumThread thread = membershipService
								.applyForMembership(user,
										motivation.toString(), game, realm);

						membershipService.registerAction(user);

						setResponsePage(new ForumThreadPage(thread.getId(), 1,
								false));

					}
				}
			}
		};

		registrationForm.add(realmChoice);
		registrationForm.add(otherGamesDescription);
		registrationForm.add(sortOfPersonArea);
		registrationForm.add(lookingForArea);
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
}