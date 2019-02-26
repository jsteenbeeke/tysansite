package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;

public class JuniorMemberTest extends AbstractClickThroughTester {

	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.JUNIOR_MEMBER);
	}
}
