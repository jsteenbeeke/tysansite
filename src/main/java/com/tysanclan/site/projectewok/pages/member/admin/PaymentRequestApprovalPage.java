/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.PaymentRequest;
import com.tysanclan.site.projectewok.entities.dao.PaymentRequestDAO;
import com.tysanclan.site.projectewok.model.DollarSignModel;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

public class PaymentRequestApprovalPage extends AbstractMemberPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private PaymentRequestDAO requestDAO;

	public PaymentRequestApprovalPage() {
		super("Payment requests");

		if (!getUser().equals(roleService.getTreasurer())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "'content'");

		add(accordion);

		accordion.add(new ListView<PaymentRequest>("requests", ModelMaker
				.wrap(requestDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<PaymentRequest> item) {
				PaymentRequest request = item.getModelObject();

				item.add(new Label("header", request.getItem()));
				item.add(new MemberListItem("requester", request.getRequester()));
				item.add(new Label("amount", new DollarSignModel(
						new Model<BigDecimal>(request.getAmount()))));

				item.add(new Label("desc", request.getItem()));

				item.add(new HiddenField<String>("paypalAddress",
						new Model<String>(request.getRequester()
								.getPaypalAddress())).add(AttributeModifier
						.replace("name", "business")));
				item.add(new HiddenField<String>("itemname", new Model<String>(
						request.getItem())));

				item.add(new HiddenField<String>("itemdesc", new Model<String>(
						"The Tysan Clan - " + request.getItem()))
						.add(AttributeModifier.replace("name", "item_name"))); //
				item.add(new HiddenField<String>("amount2", new Model<String>(
						request.getAmount().toString())).add(AttributeModifier
						.replace("name", "amount")));
				item.add(new HiddenField<String>("returnUrl",
						new Model<String>(
								"https://www.tysanclan.com/processPaymentRequest/"
										+ request.getId()
										+ "/"
										+ requestDAO
												.getConfirmationKey(request)
										+ "/")).add(AttributeModifier.replace(
						"name", "return")));
			}
		});
	}
}
