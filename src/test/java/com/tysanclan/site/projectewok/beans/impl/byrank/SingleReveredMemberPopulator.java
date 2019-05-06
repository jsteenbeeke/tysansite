package com.tysanclan.site.projectewok.beans.impl.byrank;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import org.springframework.stereotype.Component;

@Component
@Dataset(SingleReveredMemberPopulator.KEY)
public class SingleReveredMemberPopulator extends SingleRankPopulator {
	public static final String KEY = "single-revered-member";

	public SingleReveredMemberPopulator() {
		super(Rank.REVERED_MEMBER);
	}
}
