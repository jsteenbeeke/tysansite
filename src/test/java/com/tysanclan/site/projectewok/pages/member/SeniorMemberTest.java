package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;

public class SeniorMemberTest extends AbstractClickThroughTester {

	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.SENIOR_MEMBER);
	}
}
