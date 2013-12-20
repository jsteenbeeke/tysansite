package com.tysanclan.site.projectewok.pages.member;

import org.junit.Test;

import com.tysanclan.site.projectewok.pages.member.senate.AddRegulationPage;
import com.tysanclan.site.projectewok.pages.member.senate.ModifyRegulationPage;
import com.tysanclan.site.projectewok.pages.member.senate.RegulationModificationPage;
import com.tysanclan.site.projectewok.pages.member.senate.RepealRegulationPage;
import com.tysanclan.site.projectewok.pages.member.senate.SenateTruthsayerComplaintPage;
import com.tysanclan.site.projectewok.pages.member.senate.TruthsayerVotePage;

public class SenatorTest extends AbstractClickThroughTester {

	public SenatorTest() {
		super(3L);
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
