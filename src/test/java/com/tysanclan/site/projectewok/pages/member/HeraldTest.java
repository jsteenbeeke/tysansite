package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.pages.member.admin.HeraldTransferPage;
import com.tysanclan.site.projectewok.pages.member.admin.MumbleServerAdminPage;
import com.tysanclan.site.projectewok.pages.member.admin.RequestPaymentPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HeraldTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdOfRank(Rank.SENIOR_MEMBER);
	}

	@Before
	public void makeHerald() {
		assignRole(2L);
	}

	@After
	public void unmakeHerald() {
		clearRole(2L);
	}

	@Test
	public void testPaypal() {
		overview();
		getTester().clickLink("heraldpanel:paypal:label:link");
		getTester().assertRenderedPage(PaypalSettingsPage.class);
	}

	@Test
	public void testRequestPayment() {
		overview();
		getTester().clickLink("heraldpanel:requestPayment");
		getTester().assertRenderedPage(RequestPaymentPage.class);
	}

	@Test
	public void testTransfer() {
		overview();
		getTester().clickLink("heraldpanel:transfer");
		getTester().assertRenderedPage(HeraldTransferPage.class);
	}

	@Test
	public void testMumble() {
		overview();
		getTester().clickLink("heraldpanel:mumble");
		getTester().assertRenderedPage(MumbleServerAdminPage.class);
	}
}
