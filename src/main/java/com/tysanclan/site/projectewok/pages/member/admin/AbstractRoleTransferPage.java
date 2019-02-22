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

import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.TysanDropDownChoice;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.RoleTransfer;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;

@TysanMemberSecured
public abstract class AbstractRoleTransferPage extends
		AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private UserDAO userDAO;

	public AbstractRoleTransferPage(RoleType type) {
		super("Transfer of " + type.toString());

		if (!getUser().equals(getRole(roleService))) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		RoleTransfer transfer = roleService.getCurrentTransfer(type);

		if (transfer != null)
			throw new RestartResponseAtInterceptPageException(
					new TransferInProgressPage(type));

		UserFilter filter = new UserFilter();
		filter.setRetired(false);
		filter.addRank(Rank.CHANCELLOR);
		filter.addRank(Rank.SENATOR);
		filter.addRank(Rank.TRUTHSAYER);
		filter.addRank(Rank.REVERED_MEMBER);
		filter.addRank(Rank.FULL_MEMBER);
		filter.addRank(Rank.SENIOR_MEMBER);
		filter.addRank(Rank.JUNIOR_MEMBER);
		filter.addOrderBy("username", true);

		List<User> users = userDAO.findByFilter(filter);

		final TysanDropDownChoice<User> userChoice = new TysanDropDownChoice<User>(
				"user", null, users);
		userChoice.setNullValid(false);
		userChoice.setRequired(false);

		Form<RoleType> transferForm = new Form<RoleType>("transferForm",
				new Model<RoleType>(type)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				roleService.initiateTransfer(getModelObject(),
						userChoice.getModelObject());

				setResponsePage(new TransferInProgressPage(getModelObject()));
			}
		};

		transferForm.add(new Label("type", type.toString()));

		transferForm.add(userChoice);

		add(transferForm);
	}

	public abstract User getRole(RoleService service);
}
