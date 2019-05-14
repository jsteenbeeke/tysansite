package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.site.projectewok.TysanPageTester;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.beans.impl.senate.SenateElectionOneRemainingAndActivePopulator;
import com.tysanclan.site.projectewok.beans.impl.senate.SenateElectionWithOnlyASingleCandidatePopulator;
import com.tysanclan.site.projectewok.entities.LogItem;
import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.dao.LogItemDAO;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;
import com.tysanclan.site.projectewok.entities.filter.LogItemFilter;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@Dataset(SenateElectionWithOnlyASingleCandidatePopulator.KEY)
public class SenateElectionWhenOnlyOneSenatorAndOneAlreadyActiveTest extends TysanPageTester {

	@Test
	public void testIfElectionIsNotRestartedEarly() {
		SenateElectionDAO senateElectionDAO = getBean(SenateElectionDAO.class);
		DemocracyService democracyService = getBean(DemocracyService.class);
		LogItemDAO logItemDAO = getBean(LogItemDAO.class);

		Date originalStart = democracyService.getCurrentSenateElection().getStart();

		assertThat(senateElectionDAO.countAll(), equalTo(1L));

		democracyService.checkSenatorElections();

		assertThat(senateElectionDAO.countAll(), equalTo(1L));

		Date newStart = democracyService.getCurrentSenateElection().getStart();

		LogItemFilter filter = new LogItemFilter();
		filter.category("Democracy");
		filter.message(DemocracyService.Messages.Log.RESTART_SENATE_DUE_TO_CANDIDATE_LACK);

		assertNotNull(logItemDAO.getUniqueByFilter(filter));

		assertThat(newStart, not(equalTo(originalStart)));


	}
}
