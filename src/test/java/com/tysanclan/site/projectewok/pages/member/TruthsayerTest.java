package com.tysanclan.site.projectewok.pages.member;

import org.junit.Test;

import com.tysanclan.site.projectewok.pages.member.justice.ForumUserManagementPage;
import com.tysanclan.site.projectewok.pages.member.justice.ImpeachmentInitiationPage;
import com.tysanclan.site.projectewok.pages.member.justice.TruthSayerEditUserPage;
import com.tysanclan.site.projectewok.pages.member.justice.UntenabilityPage;

public class TruthsayerTest extends AbstractClickThroughTester {

	public TruthsayerTest() {
		super(2L, false);
	}

	@Test
	public void testForumUserLink() {
		overview();
		getTester().clickLink("truthsayerpanel:forumuserlink");
		getTester().assertRenderedPage(ForumUserManagementPage.class);
	}

	@Test
	public void testImpeachment() {
		overview();
		getTester().clickLink("truthsayerpanel:impeachlink");
		getTester().assertRenderedPage(ImpeachmentInitiationPage.class);
	}

	@Test
	public void testStepDown() {
		overview();
		getTester().clickLink("truthsayerpanel:stepdown");
		getTester().assertRenderedPage(TruthsayerStepDownPage.class);
	}

	@Test
	public void testUntenability() {
		overview();
		getTester().clickLink("truthsayerpanel:untenable");
		getTester().assertRenderedPage(UntenabilityPage.class);
	}

	@Test
	public void testEditUser() {
		overview();
		getTester().clickLink("truthsayerpanel:truthsayerEditUserPage");
		getTester().assertRenderedPage(TruthSayerEditUserPage.class);
	}

}
