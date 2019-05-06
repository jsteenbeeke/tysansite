package com.tysanclan.site.projectewok.beans.impl.byrank;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import org.springframework.stereotype.Component;

@Component
@Dataset(SingleFullMemberPopulator.KEY)
public class SingleFullMemberPopulator extends SingleRankPopulator {
	public static final String KEY = "single-full-member";

	public SingleFullMemberPopulator() {
		super(Rank.FULL_MEMBER);
	}
}
