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
package com.tysanclan.site.projectewok.pages.member;

import java.math.BigDecimal;
import java.util.Locale;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.SubscriptionPayment;
import com.tysanclan.site.projectewok.entities.SubscriptionPayment.UnpaidFilter;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionPaymentDAO;
import com.tysanclan.site.projectewok.model.DollarSignModel;

@TysanMemberSecured
public class SubscriptionPaymentPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private SubscriptionPaymentDAO paymentDAO;

	public SubscriptionPaymentPage() {
		super("Subscription payments due");

		if (roleService.getTreasurer() == null
				|| roleService.getTreasurer().getPaypalAddress() == null) {
			OverviewPage p = new OverviewPage();
			p.error("No Treasurer, or Treasurer has not setup Paypal");

			throw new RestartResponseAtInterceptPageException(p);
		}

		add(new ListView<SubscriptionPayment>("requests",
				ModelMaker.wrap(ImmutableList.copyOf(Iterables.filter(getUser()
						.getPayments(), new UnpaidFilter())))) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<SubscriptionPayment> item) {
				SubscriptionPayment payment = item.getModelObject();

				DateTime dt = new DateTime(payment.getDate());
				String dateTimeStr = dt.toString("dd MMMM yyyy", Locale.US);

				item.add(new Label("header", "Subscription Payment - "
						+ dateTimeStr));
				item.add(new Label("amount", new DollarSignModel(
						new Model<BigDecimal>(payment.getSubscription()
								.getAmount()))));

				item.add(new HiddenField<String>(
						"paypalAddress",
						new Model<String>(
								roleService.getTreasurer() != null ? roleService
										.getTreasurer().getPaypalAddress()
										: null)).add(AttributeModifier.replace(
						"name", "business")));
				item.add(new HiddenField<String>("itemname", new Model<String>(
						"The Tysan Clan - Subscription Payment - "
								+ dateTimeStr)));

				item.add(new HiddenField<String>("itemdesc", new Model<String>(
						"The Tysan Clan - Subscription Payment - "
								+ dateTimeStr)).add(AttributeModifier.replace(
						"name", "item_name"))); //
				item.add(new HiddenField<String>("amount2", new Model<String>(
						payment.getSubscription().getAmount().toString()))
						.add(AttributeModifier.replace("name", "amount")));
				item.add(new HiddenField<String>("returnUrl",
						new Model<String>(
								"https://www.tysanclan.com/processSubscriptionPayment/"
										+ payment.getId()
										+ "/"
										+ paymentDAO
												.getConfirmationKey(payment)
										+ "/")).add(AttributeModifier.replace(
						"name", "return")));
			}
		});
	}
}
