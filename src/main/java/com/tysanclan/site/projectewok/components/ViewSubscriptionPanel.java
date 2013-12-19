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
package com.tysanclan.site.projectewok.components;

import java.math.BigDecimal;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;
import com.tysanclan.site.projectewok.entities.Subscription;
import com.tysanclan.site.projectewok.entities.User;

public class ViewSubscriptionPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private IPageLink onSubmitLink;

	public ViewSubscriptionPanel(String id, User user, IPageLink onSubmitLink) {
		super(id);

		this.onSubmitLink = onSubmitLink;

		if (user.getSubscription() == null) {
			setVisible(false);
		}

		ExpensePeriod period = ExpensePeriod.MONTHLY;
		BigDecimal amount = BigDecimal.ZERO;
		int due = 0;

		if (user.getSubscription() != null) {
			period = user.getSubscription().getInterval();
			amount = user.getSubscription().getAmount();
			due = user.getSubscription().countPaymentsDue();
		}

		add(new Label("amount", new Model<BigDecimal>(amount)));
		add(new Label("interval", period.getOmschrijving()));
		add(new Label("due", new Model<Integer>(due)));

		add(new Form<Subscription>("unsubscribe", ModelMaker.wrap(user
				.getSubscription())) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private FinanceService financeService;

			@Override
			protected void onSubmit() {
				super.onSubmit();

				if (!financeService.cancelSubscription(getModelObject())) {
					error("You have outstanding payments for this subscription, you cannot cancel until they are paid for");
					return;
				}

				setResponsePage(ViewSubscriptionPanel.this.onSubmitLink
						.getPage());
			}
		}.setVisible(due == 0));
	}
}
