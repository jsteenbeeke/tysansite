package com.tysanclan.site.projectewok.pages.member;

import java.util.List;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.Result;
import org.apache.wicket.util.tester.WicketTesterHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.tysanclan.site.projectewok.TysanPageTester;
import com.tysanclan.site.projectewok.pages.member.justice.StartTrialPage;

public abstract class AbstractClickThroughTester extends TysanPageTester {
	private final long userId;

	private boolean allowTrial;

	protected AbstractClickThroughTester(long userId) {
		this(userId, true);
	}

	protected AbstractClickThroughTester(long userId, boolean allowTrial) {
		super();
		this.userId = userId;
		this.allowTrial = allowTrial;
	}

	@Before
	public void setup() {
		logIn(userId);

	}

	@Test
	public void testMessages() {
		overview();
		getTester().clickLink("basicpanel:messages:label:link");
		getTester().assertRenderedPage(MessageListPage.class);
	}

	@Test
	public void testNotifications() {
		overview();
		getTester().clickLink("basicpanel:notification:label:link");
		getTester().assertRenderedPage(NotificationsPage.class);
	}

	@Test
	public void testPreferences() {
		overview();
		getTester().clickLink("basicpanel:preferences");
		getTester().assertRenderedPage(MemberPreferencesPage.class);
	}

	@Test
	public void testPastElections() {
		overview();
		getTester().clickLink("basicpanel:pastelections");
		getTester().assertRenderedPage(PastElectionsPage.class);
	}

	@Test
	public void testLogPage() {
		overview();
		getTester().clickLink("basicpanel:log");
		getTester().assertRenderedPage(LogPage.class);
	}

	@Test
	public void testJoinGroup() {
		overview();
		getTester().clickLink("basicpanel:joingroup:label:link");
		getTester().assertRenderedPage(JoinGroupPage.class);
	}

	@Test
	public void testFinancesPage() {
		overview();
		getTester().clickLink("basicpanel:finance");
		getTester().assertRenderedPage(FinancePage.class);
	}

	@Test
	public void testClanStats() {
		overview();
		getTester().clickLink("basicpanel:clanstats");
		getTester().assertRenderedPage(ClanStatisticsPage.class);
	}

	@Test
	public void testCalendarPage() {
		overview();
		getTester().clickLink("basicpanel:calendar:label:link");
		getTester().assertRenderedPage(CalendarPage.class);
	}

	@Test
	public void testTrial() {
		if (allowTrial) {
			overview();
			getTester().clickLink("basicpanel:trial");
			getTester().assertRenderedPage(StartTrialPage.class);
		}
	}

	@Test
	public void testComplaintPage() {
		if (allowTrial) {
			overview();
			getTester().clickLink("basicpanel:complaint");
			getTester().assertRenderedPage(TruthsayerComplaintPage.class);
		}
	}

	@Test
	public void testAIMPage() {
		overview();
		getTester().clickLink("basicpanel:aim");
		getTester().assertRenderedPage(AIMOverviewPage.class);
	}

	@Test
	public void testGameAccounts() {
		overview();
		getTester().clickLink("basicpanel:accounts:label:link");
		getTester().assertRenderedPage(EditAccountsPage.class);
	}

	@Test
	public void testGallery() {
		overview();
		getTester().clickLink("basicpanel:gallery");
		getTester().assertRenderedPage(EditUserGalleryPage.class);
	}

	@Test
	public void testRequestAchievement() {
		overview();
		getTester().clickLink("basicpanel:requestAchievement");
		getTester().assertRenderedPage(RequestAchievementPage.class);
	}

	@Test
	public void testBugLink() {
		overview();
		getTester().clickLink("basicpanel:bugs:label:link");
		getTester().assertRenderedPage(BugOverviewPage.class);
	}

	@Test
	public void testNewBugLink() {
		overview();
		getTester().clickLink("basicpanel:newbug:label:link");
		getTester().assertRenderedPage(ReportBugPage.class);
	}

	@Test
	public void testFeaturesLink() {
		overview();
		getTester().clickLink("basicpanel:features:label:link");
		getTester().assertRenderedPage(FeatureOverviewPage.class);
	}

	@Test
	public void testNewFeatureLink() {
		overview();
		getTester().clickLink("basicpanel:newfeature:label:link");
		getTester().assertRenderedPage(RequestFeaturePage.class);
	}

	protected void overview() {
		getTester().startPage(OverviewPage.class);
		getTester().assertRenderedPage(OverviewPage.class);
	}

	protected List<String> generateLinks() {
		return generateLinks(null);
	}

	protected List<String> generateLinks(String prefix) {
		overview();
		final List<String> paths = Lists.newArrayList();
		final List<String> links = Lists.newArrayList();

		for (WicketTesterHelper.ComponentData obj : WicketTesterHelper
				.getComponentData(getTester().getLastRenderedPage())) {
			final String path = obj.path;
			if (prefix == null || path.startsWith(prefix)) {
				paths.add(obj.path);
			}
		}

		for (String path : paths) {
			Result result = getTester().isComponent(path, Link.class);
			if (!result.wasFailed()) {
				links.add(path);
			}
		}

		return links;
	}

	@After
	public void cleanup() {
		logOut();
	}

	/*
	 * 
	 * 
	 * 
	 * @Test public void dumpLinks() throws IOException { PrintWriter pw = new
	 * PrintWriter(new FileWriter("generated.java"));
	 * 
	 * pw.println("");
	 * 
	 * for (String link : generateLinks()) { if (link.endsWith("icon:link"))
	 * continue;
	 * 
	 * pw.println("\t@Test"); pw.printf( "\tpublic void test%s() {",
	 * Joiner.on("").join( Iterables.transform(
	 * Lists.newArrayList(link.split(":")),
	 * StringUtil.capitalizeFirstFunction()))); pw.println();
	 * pw.println("\t\toverview();"); pw.println("\t\tgetTester().clickLink(\""
	 * + link + "\");"); pw.println("\t\tgetTester().assertRenderedPage();");
	 * pw.println("\t}"); pw.println(""); }
	 * 
	 * pw.flush(); pw.close(); }
	 */
}
