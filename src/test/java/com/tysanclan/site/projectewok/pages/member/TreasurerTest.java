package com.tysanclan.site.projectewok.pages.member;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tysanclan.site.projectewok.pages.member.admin.CaretakerFinancePage;
import com.tysanclan.site.projectewok.pages.member.admin.PaymentRequestApprovalPage;
import com.tysanclan.site.projectewok.pages.member.admin.TreasurerTransferPage;

public class TreasurerTest extends AbstractClickThroughTester {
	public TreasurerTest() {
		super(1L);
	}

	@Before
	public void makeTreasurer() {
		assignRole(1L);
	}

	@After
	public void unmakeTreasurer() {
		clearRole(1L);
	}

	@Test
	public void testFinances() {
		overview();
		getTester().clickLink("treasurerpanel:finances");
		getTester().assertRenderedPage(CaretakerFinancePage.class);
	}

	@Test
	public void testPaymentRequest() {
		overview();
		getTester().clickLink("treasurerpanel:paymentRequests:label:link");
		getTester().assertRenderedPage(PaymentRequestApprovalPage.class);
	}

	@Test
	public void testSettings() {
		overview();
		getTester().clickLink("treasurerpanel:paypal:label:link");
		getTester().assertRenderedPage(PaypalSettingsPage.class);
	}

	@Test
	public void testTransfer() {
		overview();
		getTester().clickLink("treasurerpanel:transfer");
		getTester().assertRenderedPage(TreasurerTransferPage.class);
	}

}
