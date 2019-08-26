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
		wicket().clickLink("stewardpanel:bugmasters");
		wicket().assertRenderedPage(StewardManageBugMastersPage.class);
	}

	@Test
	public void testNotifications() {
		overview();
		wicket().clickLink("stewardpanel:notifications");
		wicket().assertRenderedPage(SiteWideNotificationPage.class);
	}

	@Test
	public void testAllowedGames() {
		overview();
		wicket().clickLink("stewardpanel:allowedGames");
		wicket().assertRenderedPage(GameRealmAllowAccountTypePage.class);
	}

	@Test
	public void testPaypal() {
		overview();
		wicket().clickLink("stewardpanel:paypal:label:link");
		wicket().assertRenderedPage(PaypalSettingsPage.class);
	}

	@Test
	public void testRequestPaymentPage() {
		overview();
		wicket().clickLink("stewardpanel:requestPayment");
		wicket().assertRenderedPage(RequestPaymentPage.class);
	}

	@Test
	public void testStewardpanelTransfer() {
		overview();
		wicket().clickLink("stewardpanel:transfer");
		wicket().assertRenderedPage(StewardTransferPage.class);
	}
}
