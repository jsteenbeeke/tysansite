package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.pages.member.admin.CaretakerFinancePage;
import com.tysanclan.site.projectewok.pages.member.admin.PaymentRequestApprovalPage;
import com.tysanclan.site.projectewok.pages.member.admin.TreasurerTransferPage;
import org.junit.Test;

public class TreasurerTest extends AbstractClickThroughTester {
	@Override
	protected long determineUserId() {
		return userIdWithRole(Role.RoleType.TREASURER);
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
