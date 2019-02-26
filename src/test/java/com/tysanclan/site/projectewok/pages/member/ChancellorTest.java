package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.pages.member.senate.AddRegulationPage;
import com.tysanclan.site.projectewok.pages.member.senate.ModifyRegulationPage;
import com.tysanclan.site.projectewok.pages.member.senate.RepealRegulationPage;
import org.junit.Test;

public class ChancellorTest extends AbstractClickThroughTester {

	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.CHANCELLOR);
	}

	@Test
	public void testGroupRequests() {
		overview();
		getTester().clickLink("chancellorpanel:grouprequest:label:link");
		getTester().assertRenderedPage(GroupRequestApprovalPage.class);
	}

	@Test
	public void testRoleManagement() {
		overview();
		getTester().clickLink("chancellorpanel:roles");
		getTester().assertRenderedPage(RolesManagementPage.class);
	}

	@Test
	public void testCommitteePage() {
		overview();
		getTester().clickLink("chancellorpanel:committee");
		getTester().assertRenderedPage(CommitteePage.class);
	}

	@Test
	public void testNominateTruthsayer() {
		overview();
		getTester().clickLink("chancellorpanel:truthsayernomination");
		getTester().assertRenderedPage(TruthsayerNominationPage.class);
	}

	@Test
	public void testForumManagement() {
		overview();
		getTester().clickLink("chancellorpanel:forummanagement");
		getTester().assertRenderedPage(ForumManagementPage.class);
	}

	@Test
	public void testStepdown() {
		overview();
		getTester().clickLink("chancellorpanel:stepdown");
		getTester().assertRenderedPage(ChancellorStepDownPage.class);
	}

	@Test
	public void testAddRegulation() {
		overview();
		getTester().clickLink("chancellorpanel:addregulation");
		getTester().assertRenderedPage(AddRegulationPage.class);
	}

	@Test
	public void testModifyRegulation() {
		overview();
		getTester().clickLink("chancellorpanel:modifyregulation");
		getTester().assertRenderedPage(ModifyRegulationPage.class);
	}

	@Test
	public void testRepealRegulation() {
		overview();
		getTester().clickLink("chancellorpanel:repealregulation");
		getTester().assertRenderedPage(RepealRegulationPage.class);
	}

	@Test
	public void testRealmManagement() {
		overview();
		getTester().clickLink("chancellorpanel:realms");
		getTester().assertRenderedPage(RealmManagementPage.class);

		getTester().clickLink("realms:0:edit:link");
		getTester().assertRenderedPage(EditRealmSupervisorPage.class);
	}

	@Test
	public void testGameManagement() {
		overview();
		getTester().clickLink("chancellorpanel:games");
		getTester().assertRenderedPage(GameManagementPage.class);

		getTester().clickLink("games:0:edit:link");
		getTester().assertRenderedPage(EditGameSupervisorPage.class);
	}

	@Test
	public void testInactiveKeyRole() {
		overview();
		getTester().clickLink("chancellorpanel:inactivekeyrole:label:link");
		getTester().assertRenderedPage(InactiveKeyRoleTransferPage.class);
	}

	@Test
	public void testComplaints() {
		overview();
		getTester().clickLink("chancellorpanel:complaints:label:link");
		getTester().assertRenderedPage(ChancellorTruthsayerComplaintPage.class);
	}

}
