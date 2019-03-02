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
package com.tysanclan.site.projectewok.pages.member.justice;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.NotificationService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.components.ConfirmationLink;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;
import io.vavr.control.Option;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Ties
 */
@TysanRankSecured(Rank.TRUTHSAYER)
public class TruthSayerEditUserPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDao;

	@SpringBean
	private UserService userService;

	@SpringBean
	private NotificationService notificationService;

	public TruthSayerEditUserPage(User user) {
		super("Edit user");

		User givenUser = null;
		if (user != null) {
			givenUser = userDao.load(user.getId()).getOrElseThrow(
					() -> new RestartResponseAtInterceptPageException(
							OverviewPage.class));
		}

		Form<Void> form = new Form<Void>("userSearchForm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				UserFilter filter = new UserFilter();
				filter.username(
						get("username").getDefaultModelObjectAsString());
				Option<User> foundUser = userDao.getUniqueByFilter(filter);
				if (foundUser.isEmpty()) {
					warn("Could not find user");
					return;
				}
				setResponsePage(new TruthSayerEditUserPage(foundUser.get()));
			}
		};
		form.add(new TextField<>("username",
				new Model<>(givenUser != null ? givenUser.getUsername() : "")));
		SubmitLink submit = new SubmitLink("submit", form);
		submit.add(new ContextImage("search", "images/icons/magnifier.png"));
		form.add(submit);

		ConfirmationLink<User> removeAvatarLink = new ConfirmationLink<User>(
				"removeAvatar", new Model<>(givenUser),
				"Are you sure you want to delete this users avatar?") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				User selectedUser = getModelObject();
				if (selectedUser != null) {
					userService.setUserAvatar(selectedUser.getId(), null);
					notificationService.notifyUser(selectedUser,
							"Your avatar has been removed by a Truthsayer");
					setResponsePage(new TruthSayerEditUserPage(selectedUser));
				}
			}

		};

		ConfirmationLink<User> removeSignatureLink = new ConfirmationLink<User>(
				"removeSignature", new Model<User>(givenUser),
				"Are you sure you want to delete this users signature?") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				User selectedUser = getModelObject();
				if (selectedUser != null) {
					userService.setUserSignature(selectedUser.getId(), "");
					notificationService.notifyUser(selectedUser,
							"Your signature has been removed by a Truthsayer");
					setResponsePage(new TruthSayerEditUserPage(selectedUser));
				}
			}

		};

		ConfirmationLink<User> removeCustomTitleLink = new ConfirmationLink<User>(
				"removeCustomTitleLink", new Model<>(givenUser),
				"Are you sure you want to delete this users custom title?") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				User selectedUser = getModelObject();
				if (selectedUser != null) {
					userService.setUserCustomTitle(selectedUser.getId(), "");
					notificationService.notifyUser(selectedUser,
							"Your custom title has been removed by a Truthsayer");
					setResponsePage(new TruthSayerEditUserPage(selectedUser));
				}
			}

		};

		removeAvatarLink.setEnabled(
				givenUser != null && givenUser.getImageURL() != null
						&& !givenUser.getImageURL().isEmpty());
		removeSignatureLink.setEnabled(
				givenUser != null && givenUser.getSignature() != null
						&& !givenUser.getSignature().isEmpty());
		removeCustomTitleLink.setEnabled(
				givenUser != null && givenUser.getCustomTitle() != null
						&& !givenUser.getCustomTitle().isEmpty());

		removeAvatarLink
				.add(new ContextImage("delete", "images/icons/delete.png"));
		removeSignatureLink
				.add(new ContextImage("delete", "images/icons/delete.png"));
		removeCustomTitleLink
				.add(new ContextImage("delete", "images/icons/delete.png"));

		add(new Label("username",
				new Model<>(givenUser != null ? givenUser.getUsername() : "")));
		add(removeAvatarLink);
		add(removeSignatureLink);
		add(removeCustomTitleLink);
		add(form);
	}
}
