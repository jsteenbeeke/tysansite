package com.tysanclan.site.projectewok.beans.impl.byrank;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import org.springframework.stereotype.Component;

@Component
@Dataset(SingleSenatorPopulator.KEY)
public class SingleSenatorPopulator extends SingleRankPopulator {
	public static final String KEY = "single-senator";

	public SingleSenatorPopulator() {
		super(Rank.SENATOR);
	}
}
