package com.tysanclan.site.projectewok.pages.member.group;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
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
