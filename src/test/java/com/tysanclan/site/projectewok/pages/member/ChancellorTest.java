package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.beans.impl.byrank.SingleChancellorPopulator;
import com.tysanclan.site.projectewok.components.ChancellorPanel;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink;
import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.member.senate.AddRegulationPage;
import com.tysanclan.site.projectewok.pages.member.senate.ModifyRegulationPage;
import com.tysanclan.site.projectewok.pages.member.senate.RepealRegulationPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.junit.Test;

@Dataset(SingleChancellorPopulator.KEY)
public class ChancellorTest extends AbstractClickThroughTester {

	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.CHANCELLOR);
	}

	@Test
	public void testGroupRequests() {
		overview();
		wicket().clickLink("chancellorpanel:grouprequest:label:link");
		wicket().assertRenderedPage(GroupRequestApprovalPage.class);
	}

	@Test
	public void testRoleManagement() {
		overview();
		wicket().clickLink("chancellorpanel:roles");
		wicket().assertRenderedPage(RolesManagementPage.class);
	}

	@Test
	public void testCommitteePage() {
		overview();
		wicket().clickLink("chancellorpanel:committee");
		wicket().assertRenderedPage(CommitteePage.class);
	}

	@Test
	public void testNominateTruthsayer() {
		overview();
		wicket().clickLink("chancellorpanel:truthsayernomination");
		wicket().assertRenderedPage(TruthsayerNominationPage.class);
	}

	@Test
	public void testForumManagement() {
		overview();
		wicket().clickLink("chancellorpanel:forummanagement");
		wicket().assertRenderedPage(ForumManagementPage.class);
	}

	@Test
	public void testStepdown() {
		overview();
		wicket().clickLink("chancellorpanel:stepdown");
		wicket().assertRenderedPage(ChancellorStepDownPage.class);
	}

	@Test
	public void testAddRegulation() {
		overview();
		wicket().clickLink("chancellorpanel:addregulation");
		wicket().assertRenderedPage(AddRegulationPage.class);
	}

	@Test
	public void testModifyRegulation() {
		overview();
		wicket().clickLink("chancellorpanel:modifyregulation");
		wicket().assertRenderedPage(ModifyRegulationPage.class);
	}

	@Test
	public void testRepealRegulation() {
		overview();
		wicket().clickLink("chancellorpanel:repealregulation");
		wicket().assertRenderedPage(RepealRegulationPage.class);
	}

	@Test
	public void testRealmManagement() {
		overview();
		wicket().clickLink("chancellorpanel:realms");
		wicket().assertRenderedPage(RealmManagementPage.class);
		wicket().assertComponent("realms", ListView.class);
		wicket().assertComponent("realms:0:edit:link", Link.class);

		wicket().clickLink("realms:0:edit:link");
		wicket().assertRenderedPage(EditRealmSupervisorPage.class);
	}

	@Test
	public void testGameManagement() {
		overview();
		wicket().clickLink("chancellorpanel:games");
		wicket().assertRenderedPage(GameManagementPage.class);
		wicket().assertComponent("games", ListView.class);
		wicket().assertComponent("games:0:edit:link", Link.class);

		wicket().clickLink("games:0:edit:link");
		wicket().assertRenderedPage(EditGameSupervisorPage.class);
	}

	@Test
	public void testInactiveKeyRole() {
		RoleService roleService = TysanApplication.get().getApplicationContext()
				.getBean(RoleService.class);
		User herald = roleService.getHerald();
		roleService.removeRoles(herald);

		overview();
		wicket().assertComponent("chancellorpanel", ChancellorPanel.class);
		wicket().assertComponent("chancellorpanel:inactivekeyrole",
								 RequiresAttentionLink.class);

		wicket().clickLink("chancellorpanel:inactivekeyrole:label:link");
		wicket().assertRenderedPage(InactiveKeyRoleTransferPage.class);

		roleService.assignTo(herald.getId(),
				roleService.getRoleByType(Role.RoleType.HERALD).getId(),
				herald.getId());
	}

	@Test
	public void testComplaints() {
		overview();
		wicket().clickLink("chancellorpanel:complaints:label:link");
		wicket().assertRenderedPage(ChancellorTruthsayerComplaintPage.class);
	}

}
