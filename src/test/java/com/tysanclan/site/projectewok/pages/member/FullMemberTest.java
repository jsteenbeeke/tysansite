package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.impl.byrank.SingleFullMemberPopulator;

@Dataset(SingleFullMemberPopulator.KEY)
public class FullMemberTest extends AbstractClickThroughTester {

	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.FULL_MEMBER);
	}
}
