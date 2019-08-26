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
		UserDAO userDAO = TysanApplication.get().getApplicationContext()
				.getBean(UserDAO.class);
		List<User> byRank = userDAO.findByRank(rank);

		if (byRank.isEmpty()) {
			throw new IllegalStateException();
		}

		User user = byRank.get(random.nextInt(byRank.size()));

		return user.getId();

	}

	protected static long userIdOfGroupMember() {
		GroupDAO groupDAO = TysanApplication.get().getApplicationContext()
				.getBean(GroupDAO.class);

		Group group = groupDAO.findAll()
				.getOrElseThrow(IllegalStateException::new);

		return group.getGroupMembers().get(0).getId();

	}

	protected static long userIdOfGroupLeader(Group.JoinPolicy joinPolicy) {
		GroupDAO groupDAO = TysanApplication.get().getApplicationContext()
				.getBean(GroupDAO.class);

		GroupFilter filter = new GroupFilter();
		filter.joinPolicy(joinPolicy);

		Group group = groupDAO.findByFilter(filter)
				.getOrElseThrow(IllegalStateException::new);

		return group.getLeader().getId();

	}

	protected static long userIdWithRole(Role.RoleType roleType) {
		RoleService roleService = TysanApplication.get().getApplicationContext()
				.getBean(RoleService.class);

		return roleService.getRoleByType(roleType).getAssignedTo().getId();
	}

	@Before
	public void setup() {
		logIn(userId);

	}

	@Test
	public void testMessages() {
		overview();
		wicket().clickLink("basicpanel:messages:label:link");
		wicket().assertRenderedPage(MessageListPage.class);
	}

	@Test
	public void testNotifications() {
		overview();
		wicket().clickLink("basicpanel:notification:label:link");
		wicket().assertRenderedPage(NotificationsPage.class);
	}

	@Test
	public void testPreferences() {
		overview();
		wicket().clickLink("basicpanel:preferences");
		wicket().assertRenderedPage(MemberPreferencesPage.class);
	}

	@Test
	public void testPastElections() {
		overview();
		wicket().clickLink("basicpanel:pastelections");
		wicket().assertRenderedPage(PastElectionsPage.class);
	}

	@Test
	public void testLogPage() {
		overview();
		wicket().clickLink("basicpanel:log");
		wicket().assertRenderedPage(LogPage.class);
	}

	@Test
	public void testJoinGroup() {
		overview();
		wicket().clickLink("basicpanel:joingroup:label:link");
		wicket().assertRenderedPage(JoinGroupPage.class);
	}

	@Test
	public void testFinancesPage() {
		overview();
		wicket().clickLink("basicpanel:finance");
		wicket().assertRenderedPage(FinancePage.class);
	}

	@Test
	public void testClanStats() {
		overview();
		wicket().clickLink("basicpanel:clanstats");
		wicket().assertRenderedPage(ClanStatisticsPage.class);
	}

	@Test
	public void testCalendarPage() {
		overview();
		wicket().clickLink("basicpanel:calendar:label:link");
		wicket().assertRenderedPage(CalendarPage.class);
	}

	@Test
	public void testTrial() {
		if (allowTrial) {
			overview();
			wicket().assertComponent("basicpanel:trial", Link.class);
			wicket().clickLink("basicpanel:trial");
			wicket().assertRenderedPage(StartTrialPage.class);
		}
	}

	@Test
	public void testComplaintPage() {
		if (allowTrial) {
			overview();
			wicket().assertComponent("basicpanel:complaint", Link.class);
			wicket().clickLink("basicpanel:complaint");
			wicket().assertRenderedPage(TruthsayerComplaintPage.class);
		}
	}

	@Test
	public void testSkypePage() {
		overview();
		wicket().clickLink("basicpanel:skype");
		wicket().assertRenderedPage(SkypeOverviewPage.class);
	}

	@Test
	public void testGameAccounts() {
		overview();
		wicket().clickLink("basicpanel:accounts:label:link");
		wicket().assertRenderedPage(EditAccountsPage.class);
	}

	@Test
	public void testRequestAchievement() {
		overview();
		wicket().clickLink("basicpanel:requestAchievement");
		wicket().assertRenderedPage(RequestAchievementPage.class);
	}

	@Test
	public void testBugLink() {
		overview();
		wicket().clickLink("basicpanel:bugs:label:link");
		wicket().assertRenderedPage(BugOverviewPage.class);
	}

	@Test
	public void testNewBugLink() {
		overview();
		wicket().clickLink("basicpanel:newbug:label:link");
		wicket().assertRenderedPage(ReportBugPage.class);
	}

	@Test
	public void testFeaturesLink() {
		overview();
		wicket().clickLink("basicpanel:features:label:link");
		wicket().assertRenderedPage(FeatureOverviewPage.class);
	}

	@Test
	public void testNewFeatureLink() {
		overview();
		wicket().clickLink("basicpanel:newfeature:label:link");
		wicket().assertRenderedPage(RequestFeaturePage.class);
	}

	protected void overview() {
		wicket().startPage(OverviewPage.class);
		wicket().assertRenderedPage(OverviewPage.class);
	}

	protected List<String> generateLinks() {
		return generateLinks(null);
	}

	protected List<String> generateLinks(String prefix) {
		overview();
		final List<String> paths = Lists.newArrayList();
		final List<String> links = Lists.newArrayList();

		for (WicketTesterHelper.ComponentData obj : WicketTesterHelper
				.getComponentData(wicket().getLastRenderedPage())) {
			final String path = obj.path;
			if (prefix == null || path.startsWith(prefix)) {
				paths.add(obj.path);
			}
		}

		for (String path : paths) {
			Result result = wicket().isComponent(path, Link.class);
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
