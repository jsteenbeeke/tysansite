package com.tysanclan.site.projectewok.pages.member;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tysanclan.site.projectewok.pages.member.admin.GameRealmAllowAccountTypePage;
import com.tysanclan.site.projectewok.pages.member.admin.RequestPaymentPage;
import com.tysanclan.site.projectewok.pages.member.admin.SiteWideNotificationPage;
import com.tysanclan.site.projectewok.pages.member.admin.StewardManageBugMastersPage;
import com.tysanclan.site.projectewok.pages.member.admin.StewardTransferPage;

public class StewardTest extends AbstractClickThroughTester {
	public StewardTest() {
		super(1L);
	}

	@Before
	public void makeSteward() {
		assignRole(3L);
	}

	@After
	public void unmakeSteward() {
		clearRole(3L);
	}

	@Test
	public void testBugMasters() {
		overview();
		getTester().clickLink("stewardpanel:bugmasters");
		getTester().assertRenderedPage(StewardManageBugMastersPage.class);
	}

	@Test
	public void testNotifications() {
		overview();
		getTester().clickLink("stewardpanel:notifications");
		getTester().assertRenderedPage(SiteWideNotificationPage.class);
	}

	@Test
	public void testAllowedGames() {
		overview();
		getTester().clickLink("stewardpanel:allowedGames");
		getTester().assertRenderedPage(GameRealmAllowAccountTypePage.class);
	}

	@Test
	public void testPaypal() {
		overview();
		getTester().clickLink("stewardpanel:paypal:label:link");
		getTester().assertRenderedPage(PaypalSettingsPage.class);
	}

	@Test
	public void testRequestPaymentPage() {
		overview();
		getTester().clickLink("stewardpanel:requestPayment");
		getTester().assertRenderedPage(RequestPaymentPage.class);
	}

	@Test
	public void testStewardpanelTransfer() {
		overview();
		getTester().clickLink("stewardpanel:transfer");
		getTester().assertRenderedPage(StewardTransferPage.class);
	}
}
