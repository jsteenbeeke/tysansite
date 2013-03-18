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
package com.tysanclan.site.projectewok.components;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriodRenderer;
import com.tysanclan.site.projectewok.entities.User;

public class ActivateSubscriptionPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private static final BigDecimal TWO = new BigDecimal(2);

	private IPageLink onSubmitLink;

	public ActivateSubscriptionPanel(String id, User user,
			IPageLink onSubmitLink) {
		super(id);
		this.onSubmitLink = onSubmitLink;

		if (user.getSubscription() != null) {
			setVisible(false);
		}

		final TextField<BigDecimal> amountField = new TextField<BigDecimal>(
				"amount", new Model<BigDecimal>(TWO), BigDecimal.class);
		amountField.add(RangeValidator.minimum(TWO));
		amountField.setRequired(true);

		final DropDownChoice<ExpensePeriod> expenseSelect = new DropDownChoice<ExpensePeriod>(
				"regularity", new Model<ExpensePeriod>(ExpensePeriod.MONTHLY),
				Arrays.asList(ExpensePeriod.values()),
				new ExpensePeriodRenderer(false));
		expenseSelect.setRequired(true);
		expenseSelect.setNullValid(false);

		Form<User> subscribeForm = new Form<User>("subscribeForm",
				ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private FinanceService financeService;

			@Override
			protected void onSubmit() {
				super.onSubmit();

				if (financeService.subscribe(getModelObject(),
						amountField.getModelObject(),
						expenseSelect.getModelObject())) {

					setResponsePage(ActivateSubscriptionPanel.this.onSubmitLink
							.getPage());
				} else {
					error("Subscription failed. Most likely you already have one and submitted this form twice");
				}
			}
		};

		subscribeForm.add(amountField);
		subscribeForm.add(expenseSelect);

		add(subscribeForm);

	}
}
