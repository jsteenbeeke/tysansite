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
package com.tysanclan.site.projectewok.pages.member.admin;

import com.jeroensteenbeeke.hyperion.solstice.data.FilterDataProvider;
import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.LambdaRenderer;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;

@TysanMemberSecured
public class StewardManageBugMastersPage
		extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private BugTrackerService tracker;

	@SpringBean
	private UserDAO userDAO;

	public StewardManageBugMastersPage() {
		super("Bug Managers");

		if (!getUser().equals(roleService.getSteward()))
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);

		add(new DataView<User>("current",
				FilterDataProvider.of(getFilter(true, true), userDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<User> item) {
				User user = item.getModelObject();

				item.add(new MemberListItem("user", user));
				item.add(new IconLink.Builder("images/icons/delete.png",
						new DeleteResponder(user)).newInstance("delete"));

			}

		});

		final DropDownChoice<User> userChoice = new DropDownChoice<User>("user",
				ModelMaker.wrap((User) null), ModelMaker.wrapChoices(
				userDAO.findByFilter(getFilter(false, false)).toJavaList()),
				LambdaRenderer.of(User::getUsername));

		Form<User> addMasterForm = new Form<User>("addMasterForm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				tracker.grantBugRights(userChoice.getModelObject());

				setResponsePage(new StewardManageBugMastersPage());

			}
		};

		addMasterForm.add(userChoice);

		add(addMasterForm);

	}

	/**
	 * @return
	 */
	private UserFilter getFilter(boolean bugReportMaster,
			boolean includeJuniorAndLower) {
		UserFilter filter = new UserFilter();
		filter.bugReportMaster(bugReportMaster);
		filter.rank(Rank.CHANCELLOR);
		filter.orRank(Rank.SENATOR);
		filter.orRank(Rank.TRUTHSAYER);
		filter.orRank(Rank.REVERED_MEMBER);
		filter.orRank(Rank.SENIOR_MEMBER);
		filter.orRank(Rank.FULL_MEMBER);
		if (includeJuniorAndLower) {
			filter.orRank(Rank.JUNIOR_MEMBER);
			filter.orRank(Rank.TRIAL);
		}
		filter.username().orderBy(true);
		return filter;
	}

	private class DeleteResponder extends DefaultClickResponder<User> {
		private static final long serialVersionUID = 1L;

		public DeleteResponder(User user) {
			super(ModelMaker.wrap(user));
		}

		@Override
		public void onClick() {
			tracker.revokeBugRights(getModelObject());

			setResponsePage(new StewardManageBugMastersPage());
		}
	}

}
