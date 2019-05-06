package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.impl.byrank.SingleSenatorPopulator;
import com.tysanclan.site.projectewok.pages.member.senate.*;
import org.junit.Test;

@Dataset(SingleSenatorPopulator.KEY)
public class SenatorTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.SENATOR);
	}

	@Test
	public void testTruthsayerApproval() {
		overview();
		getTester().clickLink("senatorpanel:truthsayers:label:link");
		getTester().assertRenderedPage(TruthsayerVotePage.class);
	}

	@Test
	public void testComplaintsPage() {
		overview();
		getTester().clickLink("senatorpanel:complaints:label:link");
		getTester().assertRenderedPage(SenateTruthsayerComplaintPage.class);
	}

	@Test
	public void testRegulationVotes() {
		overview();
		getTester().clickLink("senatorpanel:regulations:label:link");
		getTester().assertRenderedPage(RegulationModificationPage.class);
	}

	@Test
	public void testAddRegulation() {
		overview();
		getTester().clickLink("senatorpanel:addregulation");
		getTester().assertRenderedPage(AddRegulationPage.class);
	}

	@Test
	public void testModifyRegulation() {
		overview();
		getTester().clickLink("senatorpanel:modifyregulation");
		getTester().assertRenderedPage(ModifyRegulationPage.class);
	}

	@Test
	public void testRepealRegulation() {
		overview();
		getTester().clickLink("senatorpanel:repealregulation");
		getTester().assertRenderedPage(RepealRegulationPage.class);
	}

	@Test
	public void testStepdown() {
		overview();
		getTester().clickLink("senatorpanel:stepdown");
		getTester().assertRenderedPage(SenatorStepDownPage.class);
	}
}
