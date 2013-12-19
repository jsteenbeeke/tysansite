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
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.DatePickerPanel;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;

/**
 * @author Jeroen Steenbeeke
 */
public class EditExpensePage extends TysanPage {
	private static final long serialVersionUID = 1L;

	private static final List<Integer> oneToAThousand;
	private static final List<Integer> zeroToNinetyNine;

	static {
		List<Integer> _oneToAThousand = new LinkedList<Integer>();
		List<Integer> _zeroToNinetyNine = new LinkedList<Integer>();

		for (int i = 1; i <= 1000; i++) {
			_oneToAThousand.add(i);
			_zeroToNinetyNine.add(i - 1);
		}

		oneToAThousand = Collections.unmodifiableList(_oneToAThousand);
		zeroToNinetyNine = Collections.unmodifiableList(_zeroToNinetyNine);
	}

	@SpringBean
	private RoleService roleService;

	/**
	 * 
	 */
	public EditExpensePage(Expense expense) {
		super("Clan Finances");

		if (!getUser().equals(roleService.getTreasurer())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		final TextField<String> nameTextField = new TextField<String>("name",
				new Model<String>(expense.getName()));
		final DatePickerPanel startPicker = new DatePickerPanel("start",
				expense.getStart());
		final DatePickerPanel endPicker = new DatePickerPanel("end",
				expense.getEnd());

		List<String> regularityChoices = Arrays.asList(new String[] {
				"Monthly", "Quarterly", "Semiannually", "Annually",
				"Biannually" });

		final DropDownChoice<String> expenseRegularity = new DropDownChoice<String>(
				"regularity",
				new Model<String>(expense.getPeriod().toString()),
				regularityChoices);

		BigDecimal whole = expense.getAmount().setScale(0, RoundingMode.DOWN);
		BigDecimal fraction = expense.getAmount().subtract(whole)
				.multiply(new BigDecimal(100));

		final DropDownChoice<Integer> currencyMajorExpense = new DropDownChoice<Integer>(
				"currencymajor", new Model<Integer>(whole.intValue()),
				oneToAThousand);
		final DropDownChoice<Integer> currencyMinorExpense = new DropDownChoice<Integer>(
				"currencyminor", new Model<Integer>(fraction.intValue()),
				zeroToNinetyNine);

		Form<Expense> expenseForm = new Form<Expense>("expenseForm",
				ModelMaker.wrap(expense)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private FinanceService financeService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				String regularity = expenseRegularity.getModelObject();
				Integer major = currencyMajorExpense.getModelObject();
				Integer minor = currencyMinorExpense.getModelObject();
				String expenseName = nameTextField.getModelObject();
				Date startDate = startPicker.getSelectedDate();
				Date endDate = endPicker.getSelectedDate();

				BigDecimal amount = new BigDecimal(100 * major + minor)
						.divide(new BigDecimal(100));

				ExpensePeriod period = ExpensePeriod.valueOf(regularity
						.toUpperCase());

				Expense x = getModelObject();

				financeService.updateExpense(x, expenseName, amount, period,
						startDate, endDate);

				setResponsePage(new CaretakerFinancePage(getUser()));
			}

		};

		expenseForm.add(nameTextField);
		expenseForm.add(startPicker);
		expenseForm.add(endPicker);
		expenseForm.add(expenseRegularity);
		expenseForm.add(currencyMajorExpense);
		expenseForm.add(currencyMinorExpense);

		add(expenseForm);

		add(new Link<Void>("back") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new CaretakerFinancePage(getUser()));

			}
		});

	}
}
