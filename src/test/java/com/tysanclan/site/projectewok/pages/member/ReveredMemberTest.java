package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.impl.byrank.SingleReveredMemberPopulator;

@Dataset(SingleReveredMemberPopulator.KEY)
public class ReveredMemberTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.REVERED_MEMBER);
	}
}
