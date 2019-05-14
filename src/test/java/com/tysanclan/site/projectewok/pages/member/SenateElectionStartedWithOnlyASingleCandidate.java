package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.site.projectewok.TysanPageTester;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.beans.impl.senate.SenateElectionOneRemainingAndActivePopulator;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@Dataset(SenateElectionOneRemainingAndActivePopulator.KEY)
public class SenateElectionStartedWithOnlyASingleCandidate extends TysanPageTester {
	@Test
	public void testIfElectionIsTriggeredWhenOneIsAlreadyActive() {
		SenateElectionDAO senateElectionDAO = getBean(SenateElectionDAO.class);
		DemocracyService democracyService = getBean(DemocracyService.class);

		assertThat(senateElectionDAO.countAll(), equalTo(1L));

		democracyService.checkSenatorElections();

		assertThat(senateElectionDAO.countAll(), equalTo(1L));
	}

	@Test
	public void testIfSenateIsResetWhenOnlyASingleCandidateIsPresent() {
		SenateElectionDAO senateElectionDAO = getBean(SenateElectionDAO.class);
		DemocracyService democracyService = getBean(DemocracyService.class);

		Date originalStart = democracyService.getCurrentSenateElection().getStart();


		assertThat(senateElectionDAO.countAll(), equalTo(1L));

		democracyService.resolveSenatorElections();

		assertThat(senateElectionDAO.countAll(), equalTo(1L));

		Date newStart = democracyService.getCurrentSenateElection().getStart();

		assertThat(newStart, equalTo(originalStart));


	}
}
