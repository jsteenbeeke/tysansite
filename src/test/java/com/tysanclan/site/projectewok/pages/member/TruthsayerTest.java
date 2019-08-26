package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.pages.member.justice.ForumUserManagementPage;
import com.tysanclan.site.projectewok.pages.member.justice.ImpeachmentInitiationPage;
import com.tysanclan.site.projectewok.pages.member.justice.TruthSayerEditUserPage;
import com.tysanclan.site.projectewok.pages.member.justice.UntenabilityPage;
import org.junit.Test;

public class TruthsayerTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.TRUTHSAYER);
	}

	@Test
	public void testForumUserLink() {
		overview();
		wicket().clickLink("truthsayerpanel:forumuserlink");
		wicket().assertRenderedPage(ForumUserManagementPage.class);
	}

	@Test
	public void testImpeachment() {
		overview();
		wicket().clickLink("truthsayerpanel:impeachlink");
		wicket().assertRenderedPage(ImpeachmentInitiationPage.class);
	}

	@Test
	public void testStepDown() {
		overview();
		wicket().clickLink("truthsayerpanel:stepdown");
		wicket().assertRenderedPage(TruthsayerStepDownPage.class);
	}

	@Test
	public void testUntenability() {
		overview();
		wicket().clickLink("truthsayerpanel:untenable");
		wicket().assertRenderedPage(UntenabilityPage.class);
	}

	@Test
	public void testEditUser() {
		overview();
		wicket().clickLink("truthsayerpanel:truthsayerEditUserPage");
		wicket().assertRenderedPage(TruthSayerEditUserPage.class);
	}

}
