package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;

public class ReveredMemberTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.REVERED_MEMBER);
	}
}
