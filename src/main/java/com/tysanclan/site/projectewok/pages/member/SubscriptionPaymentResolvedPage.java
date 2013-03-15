package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.entities.SubscriptionPayment;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionPaymentDAO;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;

public class SubscriptionPaymentResolvedPage extends WebPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private SubscriptionPaymentDAO subscriptionPaymentDAO;

	@SpringBean
	private FinanceService financeService;

	public SubscriptionPaymentResolvedPage(PageParameters params) {

		Long paymentId = params.get("paymentId").toOptionalLong();
		String requestConfirmation = params.get("confirmationKey")
				.toOptionalString();

		if (paymentId == null || requestConfirmation == null) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		SubscriptionPayment payment = getPayment(paymentId);

		if (payment == null)
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);

		String key = subscriptionPaymentDAO.getConfirmationKey(payment);

		if (!requestConfirmation.equals(key))
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);

		if (financeService.paySubscriptionDue(payment)) {
			throw new RestartResponseAtInterceptPageException(
					new SubscriptionPaymentPage());
		}

		throw new RestartResponseAtInterceptPageException(
				AccessDeniedPage.class);
	}

	private SubscriptionPayment getPayment(Long paymentId) {
		return subscriptionPaymentDAO.get(paymentId);
	}
}
