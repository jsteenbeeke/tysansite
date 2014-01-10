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
package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.LogService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.components.ChangeEmailPanel;
import com.tysanclan.site.projectewok.components.ChangePasswordPanel;
import com.tysanclan.site.projectewok.components.PrimaryPreferencesPanel;
import com.tysanclan.site.projectewok.components.ProfilePanel;
import com.tysanclan.site.projectewok.entities.GamingGroup;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.VacationPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class MemberPreferencesPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public MemberPreferencesPage() {
		super("Preferences");

		User user = getUser();

		add(new PrimaryPreferencesPanel("prefPanel", user) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see com.tysanclan.site.projectewok.components.ChangeEmailPanel#onSubmit()
			 */
			@Override
			public void onSubmit() {
				setResponsePage(new MemberPreferencesPage());
			}
		});

		add(new ChangeEmailPanel("mailPanel", user) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see com.tysanclan.site.projectewok.components.ChangeEmailPanel#onSubmit()
			 */
			@Override
			public void onSubmit() {
				setResponsePage(new MemberPreferencesPage());
			}
		});
		add(new ChangePasswordPanel("passwordPanel", user) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see com.tysanclan.site.projectewok.components.ChangePasswordPanel#onChanged()
			 */
			@Override
			public void onChanged() {
				setResponsePage(new MemberPreferencesPage());
			}
		});

		add(new ProfilePanel("profilePanel", user) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onUpdated() {
				setResponsePage(new MemberPreferencesPage());

			}
		});

		Rank rank = user.getRank();

		boolean retireable = false;

		switch (rank) {
			case REVERED_MEMBER:
			case SENIOR_MEMBER:
				for (Group group : user.getGroups()) {
					if (group.getLeader().equals(user)
							&& group instanceof GamingGroup) {
						break;
					}
				}
				retireable = true;
			default:
		}

		add(new WebMarkupContainer("no").setVisible(!retireable
				&& !user.isRetired()));

		add(new Form<User>("unretire", ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			@SpringBean
			private LogService logService;

			@Override
			protected void onSubmit() {
				User _user = getModelObject();
				userService.setUserRetirement(_user.getId(), false);
				logService.logUserAction(_user, "Membership",
						"User has come out of retirement");
				setResponsePage(new MemberPreferencesPage());
			}

		}.setVisible(user.isRetired()));

		add(new Form<User>("retire", ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			@SpringBean
			private LogService logService;

			@Override
			protected void onSubmit() {
				User _user = getModelObject();
				userService.setUserRetirement(_user.getId(), true);
				logService.logUserAction(_user, "Membership",
						"User has retired");
				setResponsePage(new MemberPreferencesPage());

			}

		}.setVisible(!user.isRetired() && retireable));

		add(new WebMarkupContainer("trialwarning")
				.setVisible(user.getRank() == Rank.TRIAL));

		add(new Form<User>("vacation", ModelMaker.wrap(user)) {

			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				userService.activateVacationMode(getUser());

				getPage().getSession().invalidate();

				setResponsePage(VacationPage.class);
			}
		});

		add(new Form<User>("terminate", ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				setResponsePage(new ConfirmMembershipTerminationPage());
			}
		});
	}

}
