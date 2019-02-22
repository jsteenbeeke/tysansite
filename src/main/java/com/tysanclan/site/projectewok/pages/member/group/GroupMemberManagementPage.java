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

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;

public class GroupMemberManagementPage extends
		AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	public class DeleteResponder extends DefaultClickResponder<User> {
		private static final long serialVersionUID = 1L;

		public DeleteResponder(User user) {
			super(ModelMaker.wrap(user));
		}

		@Override
		public void onClick() {
			groupService.removeFromGroup(getModelObject(),
					groupModel.getObject());

			setResponsePage(new GroupMemberManagementPage(
					groupModel.getObject()));

		}

	}

	@SpringBean
	private GroupService groupService;

	private IModel<Group> groupModel;

	public GroupMemberManagementPage(Group group) {
		super(group.getName() + " - Member Management");

		if (!getUser().equals(group.getLeader()))
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);

		this.groupModel = ModelMaker.wrap(group);

		add(
				new ListView<User>("members", ModelMaker.wrap(group
						.getGroupMembers())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<User> item) {
						item.add(new MemberListItem("user", item
								.getModelObject()));
						item.add(new IconLink.Builder(
								"images/icons/delete.png", new DeleteResponder(
										item.getModelObject())).newInstance(
								"delete").setVisible(
								!item.getModelObject().equals(getUser())));

					}
				});
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		groupModel.detach();
	}
}
