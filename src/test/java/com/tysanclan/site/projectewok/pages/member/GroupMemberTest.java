package com.tysanclan.site.projectewok.pages.member;

import org.junit.Test;

import com.tysanclan.site.projectewok.pages.member.group.LeaveGroupPage;

public class GroupMemberTest extends AbstractClickThroughTester {
	public GroupMemberTest() {
		super(15L);
	}

	@Test
	public void testLeave() {
		overview();
		getTester().clickLink("groups:0:grouppanel:leave");
		getTester().assertRenderedPage(LeaveGroupPage.class);
	}
}
