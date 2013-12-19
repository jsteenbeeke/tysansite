package com.tysanclan.site.projectewok.pages.member;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tysanclan.site.projectewok.TysanPageTester;

public abstract class AbstractClickThroughTester extends TysanPageTester {
	private final long userId;

	protected AbstractClickThroughTester(long userId) {
		super();
		this.userId = userId;
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

	protected void overview() {
		getTester().startPage(OverviewPage.class);
		getTester().assertRenderedPage(OverviewPage.class);
	}

	@After
	public void cleanup() {
		logOut();
	}
}
