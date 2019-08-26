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
		wicket().clickLink("treasurerpanel:finances");
		wicket().assertRenderedPage(CaretakerFinancePage.class);
	}

	@Test
	public void testPaymentRequest() {
		overview();
		wicket().clickLink("treasurerpanel:paymentRequests:label:link");
		wicket().assertRenderedPage(PaymentRequestApprovalPage.class);
	}

	@Test
	public void testSettings() {
		overview();
		wicket().clickLink("treasurerpanel:paypal:label:link");
		wicket().assertRenderedPage(PaypalSettingsPage.class);
	}

	@Test
	public void testTransfer() {
		overview();
		wicket().clickLink("treasurerpanel:transfer");
		wicket().assertRenderedPage(TreasurerTransferPage.class);
	}

}
