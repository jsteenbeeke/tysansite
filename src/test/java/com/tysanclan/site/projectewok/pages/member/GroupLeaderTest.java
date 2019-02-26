package com.tysanclan.site.projectewok.pages.member;

import org.junit.Test;

import com.tysanclan.site.projectewok.pages.member.group.EditGroupDescriptionPage;
import com.tysanclan.site.projectewok.pages.member.group.EditMOTDPage;
import com.tysanclan.site.projectewok.pages.member.group.GroupForumManagementPage;
import com.tysanclan.site.projectewok.pages.member.group.GroupMemberManagementPage;
import com.tysanclan.site.projectewok.pages.member.group.InviteGroupMemberPage;
import com.tysanclan.site.projectewok.pages.member.group.LeaveGroupPage;

public class GroupLeaderTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfGroupLeader();
	}

	@Test
	public void testGroupDescription() {
		overview();
		getTester().clickLink("groups:0:grouppanel:editdescription");
		getTester().assertRenderedPage(EditGroupDescriptionPage.class);
	}

	@Test
	public void testEditMOTD() {
		overview();
		getTester().clickLink("groups:0:grouppanel:editmotd:link");
		getTester().assertRenderedPage(EditMOTDPage.class);
	}

	@Test
	public void testInvites() {
		overview();
		getTester().clickLink("groups:0:grouppanel:invite");
		getTester().assertRenderedPage(InviteGroupMemberPage.class);
	}

	@Test
	public void testGroupForums() {
		overview();
		getTester().clickLink("groups:0:grouppanel:forums");
		getTester().assertRenderedPage(GroupForumManagementPage.class);
	}

	@Test
	public void testGroupManagement() {
		overview();
		getTester().clickLink("groups:0:grouppanel:management");
		getTester().assertRenderedPage(GroupMemberManagementPage.class);
	}

	@Test
	public void testLeave() {
		overview();
		getTester().clickLink("groups:0:grouppanel:leave");
		getTester().assertRenderedPage(LeaveGroupPage.class);
	}

}
