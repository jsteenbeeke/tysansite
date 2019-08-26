package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.site.projectewok.pages.member.group.LeaveGroupPage;
import org.junit.Test;

public class GroupMemberTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfGroupMember();
	}

	@Test
	public void testLeave() {
		overview();
		wicket().clickLink("groups:0:grouppanel:leave");
		wicket().assertRenderedPage(LeaveGroupPage.class);
	}
}
