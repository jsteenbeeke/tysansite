/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.pages.member.admin;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.entities.PaymentRequest;
import com.tysanclan.site.projectewok.entities.dao.PaymentRequestDAO;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;

public class ProcessPaymentRequestPage extends WebPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private PaymentRequestDAO paymentRequestDAO;

	@SpringBean
	private FinanceService financeService;

	public ProcessPaymentRequestPage(PageParameters params) {
		Long requestId = params.get("requestId").toOptionalLong();
		String requestConfirmation = params.get("confirmationKey")
				.toOptionalString();

		if (requestId == null || requestConfirmation == null) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		PaymentRequest request = getRequest(requestId);

		if (request == null)
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);

		String key = paymentRequestDAO.getConfirmationKey(request);

		if (!requestConfirmation.equals(key))
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);

		if (financeService.fulfillPayment(request)) {
			throw new RestartResponseAtInterceptPageException(
					new PaymentRequestApprovalPage());
		}

		throw new RestartResponseAtInterceptPageException(
				AccessDeniedPage.class);
	}

	private PaymentRequest getRequest(Long requestId) {

		return paymentRequestDAO.get(requestId);
	}
}
