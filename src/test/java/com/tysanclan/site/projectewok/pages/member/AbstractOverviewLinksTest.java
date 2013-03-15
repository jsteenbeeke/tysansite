package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.Page;
import org.junit.After;
import org.junit.Before;

import com.tysanclan.site.projectewok.TysanPageTester;

public abstract class AbstractOverviewLinksTest extends TysanPageTester {
	private final Long testUser;

	protected AbstractOverviewLinksTest(Long testUser) {
		this.testUser = testUser;
	}

	@Before
	public void setToOverviewPage() {
		logIn(testUser);

		getTester().startPage(OverviewPage.class);
		getTester().assertRenderedPage(OverviewPage.class);
	}

	@After
	public void logoutAfterTest() {
		logOut();
	}

	protected final void verifyIconLink(String id, Class<? extends Page> target) {
		verifyLink(id + ":label:link", target);
	}

	protected final void verifyLink(String id, Class<? extends Page> target) {
		getTester().assertEnabled(id);
		getTester().assertVisible(id);

		getTester().clickLink(id, false);
		getTester().assertRenderedPage(target);
	}
}
