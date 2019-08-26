package com.tysanclan.site.projectewok.pages;

import com.tysanclan.site.projectewok.TysanPageTester;
import org.apache.wicket.Page;
import org.junit.Test;

public class PublicPagesTest extends TysanPageTester {

	@Test
	public void testNewsPage() {
		testPage(NewsPage.class);
	}

	@Test
	public void testAboutPage() {
		testPage(AboutPage.class);
	}

	@Test
	public void testJoinPage() {
		testPage(JoinOverviewPage.class);
	}

	@Test
	public void testHistoryPage() {
		testPage(HistoryPage.class);
	}

	@Test
	public void testCharterPage() {
		testPage(CharterPage.class);
	}

	@Test
	public void testRegulationsPage() {
		testPage(RegulationPage.class);
	}

	@Test
	public void testRosterPage() {
		testPage(RosterPage.class);
	}

	@Test
	public void testGroupPage() {
		testPage(GroupsPage.class);
	}

	@Test
	public void testForumPublicView() {
		testPage(ForumOverviewPage.class);
		wicket().startPage(ForumOverviewPage.class);
		wicket().assertRenderedPage(ForumOverviewPage.class);
		wicket().clickLink("forums:categories:1:forums:2:forumlink:link");
		wicket().assertRenderedPage(ForumPage.class);
		wicket().clickLink("threads:threads:1:postlink:link");
		wicket().assertRenderedPage(ForumThreadPage.class);

	}

	public void testPage(Class<? extends Page> pageClass) {
		wicket().startPage(pageClass);
		wicket().assertRenderedPage(pageClass);
	}
}
