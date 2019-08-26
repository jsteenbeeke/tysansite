package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.site.projectewok.components.GroupOverviewPanel;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.pages.member.group.*;
import org.apache.wicket.markup.html.link.Link;
import org.junit.Test;

public class GroupLeaderTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfGroupLeader(Group.JoinPolicy.INVITATION);
	}

	@Test
	public void testGroupDescription() {
		overview();
		wicket().clickLink("groups:0:grouppanel:editdescription");
		wicket().assertRenderedPage(EditGroupDescriptionPage.class);
	}

	@Test
	public void testEditMOTD() {
		overview();
		wicket().clickLink("groups:0:grouppanel:editmotd:link");
		wicket().assertRenderedPage(EditMOTDPage.class);
	}

	@Test
	public void testInvites() {
		overview();
		wicket().assertComponent("groups:0:grouppanel",
								 GroupOverviewPanel.class);
		wicket().assertComponent("groups:0:grouppanel:invite", Link.class);

		wicket().clickLink("groups:0:grouppanel:invite");
		wicket().assertRenderedPage(InviteGroupMemberPage.class);
	}

	@Test
	public void testGroupForums() {
		overview();
		wicket().clickLink("groups:0:grouppanel:forums");
		wicket().assertRenderedPage(GroupForumManagementPage.class);
	}

	@Test
	public void testGroupManagement() {
		overview();
		wicket().clickLink("groups:0:grouppanel:management");
		wicket().assertRenderedPage(GroupMemberManagementPage.class);
	}

	@Test
	public void testLeave() {
		overview();
		wicket().clickLink("groups:0:grouppanel:leave");
		wicket().assertRenderedPage(LeaveGroupPage.class);
	}

}
