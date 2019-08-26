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
		wicket().clickLink("senatorpanel:truthsayers:label:link");
		wicket().assertRenderedPage(TruthsayerVotePage.class);
	}

	@Test
	public void testComplaintsPage() {
		overview();
		wicket().clickLink("senatorpanel:complaints:label:link");
		wicket().assertRenderedPage(SenateTruthsayerComplaintPage.class);
	}

	@Test
	public void testRegulationVotes() {
		overview();
		wicket().clickLink("senatorpanel:regulations:label:link");
		wicket().assertRenderedPage(RegulationModificationPage.class);
	}

	@Test
	public void testAddRegulation() {
		overview();
		wicket().clickLink("senatorpanel:addregulation");
		wicket().assertRenderedPage(AddRegulationPage.class);
	}

	@Test
	public void testModifyRegulation() {
		overview();
		wicket().clickLink("senatorpanel:modifyregulation");
		wicket().assertRenderedPage(ModifyRegulationPage.class);
	}

	@Test
	public void testRepealRegulation() {
		overview();
		wicket().clickLink("senatorpanel:repealregulation");
		wicket().assertRenderedPage(RepealRegulationPage.class);
	}

	@Test
	public void testStepdown() {
		overview();
		wicket().clickLink("senatorpanel:stepdown");
		wicket().assertRenderedPage(SenatorStepDownPage.class);
	}
}
