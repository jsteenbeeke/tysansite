package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.impl.byrank.SingleSeniorMemberPopulator;

@Dataset(SingleSeniorMemberPopulator.KEY)
public class SeniorMemberTest extends AbstractClickThroughTester {

	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.SENIOR_MEMBER);
	}
}
