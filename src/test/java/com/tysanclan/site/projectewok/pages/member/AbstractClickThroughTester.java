package com.tysanclan.site.projectewok.pages.member;

import com.google.common.collect.Lists;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.TysanPageTester;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.GroupDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.GroupFilter;
import com.tysanclan.site.projectewok.pages.member.justice.StartTrialPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.Result;
import org.apache.wicket.util.tester.WicketTesterHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public abstract class AbstractClickThroughTester extends TysanPageTester {
	private static final Random random = new Random();

	private long userId;

	private boolean allowTrial;

	protected AbstractClickThroughTester() {
		this(true);
	}

	protected AbstractClickThroughTester(boolean allowTrial) {
		super();
		this.allowTrial = allowTrial;
	}

	@Override
	protected void setupAfterRequestStarted() {
		userId = determineUserId();
	}

	protected abstract long determineUserId();

	protected static long userIdOfRank(Rank rank) {
		UserDAO userDAO = TysanApplication.get().getApplicationContext().getBean(UserDAO.class);
		List<User> byRank = userDAO.findByRank(rank);

		if (byRank.isEmpty()) {
			throw new IllegalStateException();
		}

		User user = byRank.get(random.nextInt(byRank.size()));


		return user.getId();

	}

	protected static long userIdOfGroupMember() {
		GroupDAO groupDAO = TysanApplication.get().getApplicationContext().getBean(GroupDAO.class);

		Group group = groupDAO.findAll().getOrElseThrow(IllegalStateException::new);

		return group.getGroupMembers().get(0).getId();

	}

	protected static long userIdOfGroupLeader(Group.JoinPolicy joinPolicy) {
		GroupDAO groupDAO = TysanApplication.get().getApplicationContext().getBean(GroupDAO.class);

		GroupFilter filter = new GroupFilter();
		filter.joinPolicy(joinPolicy);

		Group group = groupDAO.findByFilter(filter).getOrElseThrow(IllegalStateException::new);

		return group.getLeader().getId();

	}

	protected static long userIdWithRole(Role.RoleType roleType) {
		RoleService roleService = TysanApplication.get().getApplicationContext().getBean(RoleService.class);

		return roleService.getRoleByType(roleType).getAssignedTo().getId();
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
	public void testSkypePage() {
		overview();
		getTester().clickLink("basicpanel:skype");
		getTester().assertRenderedPage(SkypeOverviewPage.class);
	}

	@Test
	public void testGameAccounts() {
		overview();
		getTester().clickLink("basicpanel:accounts:label:link");
		getTester().assertRenderedPage(EditAccountsPage.class);
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

	// @Test
	// public void dumpLinks() throws IOException {
	// PrintStream pw = System.out;
	//
	// pw.println("");
	//
	// for (String link : generateLinks()) {
	// if (link.endsWith("icon:link"))
	// continue;
	//
	// pw.println("\t@Test");
	// pw.printf(
	// "\tpublic void test%s() {",
	// Joiner.on("").join(
	// Iterables.transform(
	// Lists.newArrayList(link.split(":")),
	// StringUtil.capitalizeFirstFunction())));
	// pw.println();
	// pw.println("\t\toverview();");
	// pw.println("\t\tgetTester().clickLink(\"" + link + "\");");
	// pw.println("\t\tgetTester().assertRenderedPage();");
	// pw.println("\t}");
	// pw.println("");
	// }
	//
	// pw.flush();
	// pw.close();
	// }

}
