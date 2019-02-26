package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;
import org.junit.Test;

import com.tysanclan.site.projectewok.components.PupilPanel;

public class TrialMemberTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.TRIAL);
	}

	@Test
	public void testMentorPanel() {
		overview();
		getTester().assertComponent("mentor", PupilPanel.class);
	}
}
