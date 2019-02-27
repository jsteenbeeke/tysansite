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

import java.math.BigDecimal;

import com.tysanclan.site.projectewok.entities.dao.PaymentRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.PaymentRequestFilter;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.PaymentRequest;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.model.DollarSignModel;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;

public class RequestPaymentPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private FinanceService financeService;

	@SpringBean
	private UserDAO userDAO;

	@SpringBean
	private PaymentRequestDAO requestDAO;

	public RequestPaymentPage(User _user) {
		super("Request Payment");

		User user = userDAO.load(_user.getId()).getOrElseThrow(IllegalStateException::new);

		if (!user.equals(roleService.getHerald())
				&& !user.equals(roleService.getSteward())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		PaymentRequestFilter filter = new PaymentRequestFilter();
		filter.requester(user);
		filter.id().orderBy(true);

		add(
				new ListView<PaymentRequest>("pending", requestDAO.findByFilter(filter).toJavaList()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<PaymentRequest> item) {
						PaymentRequest request = item.getModelObject();
						item.add(new Label("description", request.getItem()));
						item.add(new Label("amount", new DollarSignModel(
								new Model<BigDecimal>(request.getAmount()))));
						item.add(new IconLink.Builder(
								"images/icons/money_delete.png",
								new DefaultClickResponder<PaymentRequest>(
										ModelMaker.wrap(request)) {

									private static final long serialVersionUID = 1L;

									@Override
									public void onClick() {
										financeService
												.cancelRequest(getModelObject());

										setResponsePage(new RequestPaymentPage(
												getUser()));
									}

								}).newInstance("delete"));

					}

				});

		final TextField<String> itemDescriptionField = new TextField<String>(
				"description", new Model<String>());
		itemDescriptionField.setRequired(true);

		final TextField<String> amountField = new TextField<String>("amount",
				new Model<String>("0.00"));
		amountField.setRequired(true);

		Form<PaymentRequest> requestForm = new Form<PaymentRequest>(
				"requestForm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				String description = itemDescriptionField.getModelObject();
				String amountStr = amountField.getModelObject();

				if (!amountStr.matches("^\\d+(\\.\\d{1,2})?$")) {
					error("Please enter a valid amount, such as 15, 3.2 or 9.95");
					return;
				}

				financeService.requestPayment(getUser(), description,
						new BigDecimal(amountStr));

				setResponsePage(new RequestPaymentPage(getUser()));

			}

		};

		requestForm.add(itemDescriptionField);
		requestForm.add(amountField);

		add(requestForm);

	}
}
