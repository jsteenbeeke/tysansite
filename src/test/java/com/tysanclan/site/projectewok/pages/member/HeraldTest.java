package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.pages.member.admin.HeraldTransferPage;
import com.tysanclan.site.projectewok.pages.member.admin.MumbleServerAdminPage;
import com.tysanclan.site.projectewok.pages.member.admin.RequestPaymentPage;
import org.junit.Test;

public class HeraldTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdWithRole(Role.RoleType.HERALD);
	}

	@Test
	public void testPaypal() {
		overview();
		wicket().clickLink("heraldpanel:paypal:label:link");
		wicket().assertRenderedPage(PaypalSettingsPage.class);
	}

	@Test
	public void testRequestPayment() {
		overview();
		wicket().clickLink("heraldpanel:requestPayment");
		wicket().assertRenderedPage(RequestPaymentPage.class);
	}

	@Test
	public void testTransfer() {
		overview();
		wicket().clickLink("heraldpanel:transfer");
		wicket().assertRenderedPage(HeraldTransferPage.class);
	}

	@Test
	public void testMumble() {
		overview();
		wicket().clickLink("heraldpanel:mumble");
		wicket().assertRenderedPage(MumbleServerAdminPage.class);
	}
}
