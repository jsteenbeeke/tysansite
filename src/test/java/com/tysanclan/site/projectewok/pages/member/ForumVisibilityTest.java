package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.tysanclan.site.projectewok.TysanPageTester;
import com.tysanclan.site.projectewok.beans.impl.ForumViewContextPopulator;
import com.tysanclan.site.projectewok.pages.ForumOverviewPage;
import com.tysanclan.site.projectewok.pages.forum.OverviewPage;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

@Dataset(ForumViewContextPopulator.KEY)
public class ForumVisibilityTest extends TysanPageTester {
	@Test
	public void testForumUser() {
		// Public context
		wicket().startPage(ForumOverviewPage.class);
		wicket().assertRenderedPage(ForumOverviewPage.class);

		wicket().assertContains("The latest and greatest");
		wicket().assertContains("A public forum");
		wicket().assertContainsNot("A members-only forum");
		wicket().assertContainsNot("Project Aardvark");

		FormTester formTester = wicket().newFormTester("topbar:loginform");
		formTester.setValue("username", "ForumSteve");
		formTester.setValue("password", "test");

		formTester.submit("submitter");

		wicket().assertRenderedPage(OverviewPage.class);
		wicket().clickLink("menu:forums");

		wicket().assertRenderedPage(ForumOverviewPage.class);

		wicket().assertContains("The latest and greatest");
		wicket().assertContains("A public forum");
		wicket().assertContainsNot("A members-only forum");
		wicket().assertContainsNot("Project Aardvark");

	}

	@Test
	public void testShadowUser() {
		// Public context
		wicket().startPage(ForumOverviewPage.class);
		wicket().assertRenderedPage(ForumOverviewPage.class);

		wicket().assertContains("The latest and greatest");
		wicket().assertContains("A public forum");
		wicket().assertContainsNot("A members-only forum");
		wicket().assertContainsNot("Project Aardvark");

		FormTester formTester = wicket().newFormTester("topbar:loginform");
		formTester.setValue("username", "BannedDoug");
		formTester.setValue("password", "test");

		formTester.submit("submitter");

		wicket().assertRenderedPage(OverviewPage.class);
		wicket().clickLink("menu:forums");

		wicket().assertRenderedPage(ForumOverviewPage.class);

		wicket().assertContains("The latest and greatest");
		wicket().assertContains("A public forum");
		wicket().assertContainsNot("A members-only forum");
		wicket().assertContainsNot("Project Aardvark");

	}

	@Test
	public void testMember() {
		// Public context
		wicket().startPage(ForumOverviewPage.class);
		wicket().assertRenderedPage(ForumOverviewPage.class);

		wicket().assertContains("The latest and greatest");
		wicket().assertContains("A public forum");
		wicket().assertContainsNot("A members-only forum");
		wicket().assertContainsNot("Project Aardvark");

		FormTester formTester = wicket().newFormTester("topbar:loginform");
		formTester.setValue("username", "MemberBob");
		formTester.setValue("password", "test");

		formTester.submit("submitter");

		wicket().assertRenderedPage(com.tysanclan.site.projectewok.pages.member.OverviewPage.class);
		wicket().clickLink("menu:forums");

		wicket().assertRenderedPage(ForumOverviewPage.class);

		wicket().assertContains("The latest and greatest");
		wicket().assertContains("A public forum");
		wicket().assertContains("A members-only forum");
		wicket().assertContainsNot("Project Aardvark");

	}

	@Test
	public void testGroupMember() {
		// Public context
		wicket().startPage(ForumOverviewPage.class);
		wicket().assertRenderedPage(ForumOverviewPage.class);

		wicket().assertContains("The latest and greatest");
		wicket().assertContains("A public forum");
		wicket().assertContainsNot("A members-only forum");
		wicket().assertContainsNot("Project Aardvark");

		FormTester formTester = wicket().newFormTester("topbar:loginform");
		formTester.setValue("username", "GroupMemberDave");
		formTester.setValue("password", "test");

		formTester.submit("submitter");

		wicket().assertRenderedPage(com.tysanclan.site.projectewok.pages.member.OverviewPage.class);
		wicket().clickLink("menu:forums");

		wicket().assertRenderedPage(ForumOverviewPage.class);

		wicket().assertContains("The latest and greatest");
		wicket().assertContains("A public forum");
		wicket().assertContains("A members-only forum");
		wicket().assertContains("Project Aardvark");

	}
}
