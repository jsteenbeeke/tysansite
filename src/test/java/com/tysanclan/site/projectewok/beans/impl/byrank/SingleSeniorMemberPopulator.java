package com.tysanclan.site.projectewok.beans.impl.byrank;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import org.springframework.stereotype.Component;

@Component
@Dataset(SingleSeniorMemberPopulator.KEY)
public class SingleSeniorMemberPopulator extends SingleRankPopulator {
	public static final String KEY = "single-senior-member";

	public SingleSeniorMemberPopulator() {
		super(Rank.SENIOR_MEMBER);
	}
}
