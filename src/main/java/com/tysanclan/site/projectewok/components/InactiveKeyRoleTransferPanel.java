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
package com.tysanclan.site.projectewok.components;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerNominationDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.TruthsayerNominationFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.InactiveKeyRoleTransferPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class InactiveKeyRoleTransferPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private UserDAO userDAO;

	@SpringBean
	private TruthsayerNominationDAO nominationDAO;

	private Form<RoleType> nominationForm;

	public InactiveKeyRoleTransferPanel(String id, RoleType roleType) {
		super(id);

		Role role = roleService.getRoleByType(roleType);

		if (role == null)
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);

		UserFilter filter = new UserFilter();
		filter.retired(false);
		filter.rank(Rank.REVERED_MEMBER);
		filter.orRank(Rank.FULL_MEMBER);
		filter.orRank(Rank.SENIOR_MEMBER);
		filter.username().orderBy(true);

		List<User> users = userDAO.findByFilter(filter)
				.filter(u -> nominationDAO
						.findByFilter(new TruthsayerNominationFilter().user(u))
						.isEmpty()).toJavaList();

		final DropDownChoice<User> userChoice = new TysanDropDownChoice<User>(
				"user", null, users);
		userChoice.setNullValid(false);
		userChoice.setRequired(true);

		nominationForm = new Form<RoleType>("transferForm",
				new Model<RoleType>(roleType)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				roleService.initiateTransfer(getModelObject(),
						userChoice.getModelObject());

				setResponsePage(new InactiveKeyRoleTransferPage());
			}

		};

		nominationForm.add(userChoice);
		nominationForm
				.add(new Label("name", role.getName()).setRenderBodyOnly(true));
		nominationForm.add(new Label("name2", role.getName())
				.setRenderBodyOnly(true));
		nominationForm.add(new Label("name3", role.getName())
				.setRenderBodyOnly(true));

		add(nominationForm);
	}
}
