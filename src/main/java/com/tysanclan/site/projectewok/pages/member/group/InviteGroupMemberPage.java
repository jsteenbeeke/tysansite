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
package com.tysanclan.site.projectewok.pages.member.group;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
public class InviteGroupMemberPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	public InviteGroupMemberPage(Group group) {
		super("Invite members to group");

		Form<Group> addForm = new Form<Group>("inviteForm",
				ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GroupService groupService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<User> userChoice = (DropDownChoice<User>) get("user");

				Group gr = getModelObject();
				User user = userChoice.getModelObject();

				groupService.inviteUserToGroup(user, gr);

				setResponsePage(new InviteGroupMemberPage(gr));
			}

		};

		UserFilter filter = new UserFilter();
		filter.addRank(Rank.CHANCELLOR);
		filter.addRank(Rank.SENATOR);
		filter.addRank(Rank.TRUTHSAYER);
		filter.addRank(Rank.REVERED_MEMBER);
		filter.addRank(Rank.SENIOR_MEMBER);
		filter.addRank(Rank.FULL_MEMBER);
		filter.addRank(Rank.JUNIOR_MEMBER);
		filter.addRank(Rank.TRIAL);

		List<User> users = new LinkedList<User>();

		users.addAll(userDAO.findByFilter(filter));

		users.removeAll(group.getInvitedMembers());
		users.removeAll(group.getGroupMembers());

		Collections.sort(users, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getUsername().toLowerCase()
						.compareTo(o2.getUsername().toLowerCase());
			}

		});

		addForm.add(new DropDownChoice<User>("user", ModelMaker
				.wrap((User) null), ModelMaker.wrap(users)));

		add(addForm);

	}
}
