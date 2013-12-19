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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.components.DateLabel;
import com.tysanclan.site.projectewok.components.DatePickerPanel;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Donation;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.User.CaseInsensitiveUserComparator;
import com.tysanclan.site.projectewok.entities.dao.DonationDAO;
import com.tysanclan.site.projectewok.entities.dao.ExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.PaidExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.DonationFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.ExpenseFilter;
import com.tysanclan.site.projectewok.model.DollarSignModel;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.util.FinancialTimeline;

/**
 * @author Jeroen Steenbeeke
 */
public class CaretakerFinancePage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	private static final List<Integer> oneToAHundred;
	private static final List<Integer> zeroToNinetyNine;

	static {
		List<Integer> _oneToAHundred = new LinkedList<Integer>();
		List<Integer> _zeroToNinetyNine = new LinkedList<Integer>();

		for (int i = 1; i <= 100; i++) {
			_oneToAHundred.add(i);
			_zeroToNinetyNine.add(i - 1);
		}

		oneToAHundred = Collections.unmodifiableList(_oneToAHundred);
		zeroToNinetyNine = Collections.unmodifiableList(_zeroToNinetyNine);
	}

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private FinanceService financeService;

	@SpringBean
	private UserService userService;

	@SpringBean
	private DonationDAO donationDAO;

	@SpringBean
	private ExpenseDAO expenseDAO;

	@SpringBean
	private SubscriptionDAO subscriptionDAO;

	@SpringBean
	private PaidExpenseDAO paidExpenseDAO;

	public CaretakerFinancePage(User user) {
		super("Clan Finances");

		if (!roleService.getTreasurer().equals(user)) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		ExpenseFilter efilter = new ExpenseFilter();
		efilter.addOrderBy("start", true);

		add(new ListView<Expense>("expenses", ModelMaker.wrap(expenseDAO
				.findByFilter(efilter))) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Expense> item) {
				Expense expense = item.getModelObject();

				item.add(new Label("expense", expense.getName()));
				item.add(new Label("amount", new DollarSignModel(
						new Model<BigDecimal>(expense.getAmount()))));
				item.add(new Label("regularity", expense.getPeriod().toString()));
				item.add(new DateLabel("start", expense.getStart()));
				if (expense.getEnd() != null) {
					item.add(new DateLabel("end", expense.getEnd()));
				} else {
					item.add(new IconLink.Builder(
							"images/icons/money_delete.png",
							new DefaultClickResponder<Expense>(ModelMaker
									.wrap(expense)) {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick() {
									financeService.setExpenseEnded(
											getModelObject(), new Date());

									setResponsePage(new CaretakerFinancePage(
											getUser()));
								}

							}).newInstance("end"));
				}

				item.add(new IconLink.Builder("images/icons/money.png",
						new DefaultClickResponder<Expense>(ModelMaker
								.wrap(expense)) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								setResponsePage(new EditExpensePage(
										getModelObject()));
							}

						}).newInstance("edit"));

			}

		});

		DonationFilter filter = new DonationFilter();
		filter.addOrderBy("donationTime", true);

		add(new ListView<Donation>("donations", ModelMaker.wrap(donationDAO
				.findByFilter(filter))) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Donation> item) {
				Donation donation = item.getModelObject();

				if (donation.getDonator() == null) {
					item.add(new Label("donor", "Anonymous"));
				} else {
					item.add(new MemberListItem("donor", donation.getDonator()));
				}
				item.add(new DateLabel("date", donation.getDonationTime()));
				item.add(new Label("amount", new DollarSignModel(
						new Model<BigDecimal>(donation.getAmount()))));
				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<Donation>(ModelMaker
								.wrap(donation)) {
							private static final long serialVersionUID = 1L;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								financeService.removeDonation(getUser(),
										getModelObject());

								setResponsePage(new CaretakerFinancePage(
										getUser()));
							}
						}).newInstance("delete"));

			}

		});

		add(createDonationForm());

		add(createExpenseForm());

		FinancialTimeline t = new FinancialTimeline(expenseDAO, donationDAO,
				subscriptionDAO, paidExpenseDAO);

		add(new Label("currentReserve", new DollarSignModel(
				new Model<BigDecimal>(t.getReservesToday()))));

		add(new Label(
				"exhaustion",
				t.getExhaustionPoint() == null ? "for the foreseeable future (at least 3 years)"
						: t.getExhaustionPoint()));
	}

	/**
	 	 */
	private Form<Expense> createExpenseForm() {
		Form<Expense> expenseForm = new Form<Expense>("addexpenseform") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<String> regularityDropDown = (DropDownChoice<String>) get("regularity");
				DropDownChoice<Integer> currencyMajorDropDown = (DropDownChoice<Integer>) get("currencymajor");
				DropDownChoice<Integer> currencyMinorDropDown = (DropDownChoice<Integer>) get("currencyminor");
				TextField<String> expenseNameField = (TextField<String>) get("expensename");
				DatePickerPanel startDatePicker = (DatePickerPanel) get("startdate");

				String regularity = regularityDropDown.getModelObject();
				Integer major = currencyMajorDropDown.getModelObject();
				Integer minor = currencyMinorDropDown.getModelObject();
				String expenseName = expenseNameField.getModelObject();
				Date startDate = startDatePicker.getSelectedDate();

				BigDecimal amount = new BigDecimal(100 * major + minor)
						.divide(new BigDecimal(100));

				ExpensePeriod period = ExpensePeriod.valueOf(regularity
						.toUpperCase());

				Expense expense = financeService.createExpense(expenseName,
						amount, period, startDate);

				if (expense != null) {
					info("Expense succesfully added");
				} else {
					error("Failed to add expense");
				}

				setResponsePage(new CaretakerFinancePage(getUser()));
			}
		};

		DropDownChoice<Integer> currencyMajorExpense = new DropDownChoice<Integer>(
				"currencymajor", new Model<Integer>(1), oneToAHundred);
		expenseForm.add(currencyMajorExpense);
		DropDownChoice<Integer> currencyMinorExpense = new DropDownChoice<Integer>(
				"currencyminor", new Model<Integer>(0), zeroToNinetyNine);
		expenseForm.add(currencyMinorExpense);

		TextField<String> expenseName = new TextField<String>("expensename",
				new Model<String>(""));

		expenseForm.add(expenseName);

		List<String> regularityChoices = Arrays.asList(new String[] {
				"Monthly", "Quarterly", "Semiannually", "Annually",
				"Biannually" });

		DropDownChoice<String> expenseRegularity = new DropDownChoice<String>(
				"regularity", new Model<String>("Annually"), regularityChoices);

		expenseRegularity.setNullValid(false);

		expenseForm.add(new DatePickerPanel("startdate"));

		expenseForm.add(expenseRegularity);
		return expenseForm;
	}

	/**
	 	 */
	private Form<Donation> createDonationForm() {
		Form<Donation> donationForm = new Form<Donation>("donationform") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<User> userSelect = (DropDownChoice<User>) get("userselect");
				DropDownChoice<Integer> currencyMajor = (DropDownChoice<Integer>) get("currencymajor");
				DropDownChoice<Integer> currencyMinor = (DropDownChoice<Integer>) get("currencyminor");
				DatePickerPanel donationDatePicker = (DatePickerPanel) get("donationdate");

				BigDecimal amount = new BigDecimal(100
						* currencyMajor.getModelObject()
						+ currencyMinor.getModelObject())
						.divide(new BigDecimal(100));

				Donation donation = financeService.createDonation(
						userSelect.getModelObject(), amount,
						donationDatePicker.getSelectedDate());
				if (donation != null) {
					info("Donation succesfully added");
				} else {
					error("Failed to add donation");
				}

				setResponsePage(new CaretakerFinancePage(getUser()));

			}

		};

		List<User> members = userService.getMembers();
		members.remove(getUser());
		Collections.sort(members, new CaseInsensitiveUserComparator());

		DropDownChoice<User> userSelect = new DropDownChoice<User>(
				"userselect", ModelMaker.wrap((User) null, true),
				ModelMaker.wrapChoices(members));
		donationForm.add(userSelect);

		DropDownChoice<Integer> currencyMajor = new DropDownChoice<Integer>(
				"currencymajor", new Model<Integer>(1), oneToAHundred);
		donationForm.add(currencyMajor);
		DropDownChoice<Integer> currencyMinor = new DropDownChoice<Integer>(
				"currencyminor", new Model<Integer>(0), zeroToNinetyNine);
		donationForm.add(currencyMinor);

		donationForm.add(new DatePickerPanel("donationdate"));
		return donationForm;
	}
}
