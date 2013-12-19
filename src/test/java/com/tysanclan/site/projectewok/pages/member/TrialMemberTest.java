package com.tysanclan.site.projectewok.pages.member;

import org.junit.Test;

import com.tysanclan.site.projectewok.components.PupilPanel;

public class TrialMemberTest extends AbstractClickThroughTester {

	public TrialMemberTest() {
		super(16L);
	}

	@Test
	public void testMentorPanel() {
		overview();
		getTester().assertComponent("mentor", PupilPanel.class);
	}
}
