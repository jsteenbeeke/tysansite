package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.pages.member.admin.*;
import org.junit.Test;

public class StewardTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdWithRole(Role.RoleType.STEWARD);
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
