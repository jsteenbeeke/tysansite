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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.ActivateSubscriptionPanel;
import com.tysanclan.site.projectewok.components.IOnSubmitPageCreator;
import com.tysanclan.site.projectewok.components.ViewSubscriptionPanel;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.DonationDAO;
import com.tysanclan.site.projectewok.entities.dao.ExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.PaidExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionDAO;
import com.tysanclan.site.projectewok.model.DollarSignModel;
import com.tysanclan.site.projectewok.pages.CharterPage;
import com.tysanclan.site.projectewok.util.FinancialTimeline;
import com.tysanclan.site.projectewok.util.GraphUtil;
import io.vavr.collection.Seq;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class FinancePage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	private final static BigDecimal factor12 = new BigDecimal(12);
	private final static BigDecimal factor4 = new BigDecimal(4);
	private final static BigDecimal factor2 = new BigDecimal(2);

	@SpringBean
	private ExpenseDAO expenseDAO;

	@SpringBean
	private DonationDAO donationDAO;

	@SpringBean
	private PaidExpenseDAO paidExpenseDAO;

	@SpringBean
	private SubscriptionDAO subscriptionDAO;

	@SpringBean
	private RoleService roleService;

	/**
	 *
	 */
	public FinancePage() {
		super("Clan Finances");

		// Charts:
		// - cash flow
		// - annual expense breakdown
		// - cash reserves
		// - reserve expiration date
		// - donations
		// - contribution
		// - subscriptions
		Seq<Expense> expenses = expenseDAO.findAll();
		List<Expense> filtered = new LinkedList<Expense>();

		BigDecimal sum = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);

		DateTime now = new DateTime();
		DateTime oneMonthAgo = now.minusMonths(1);
		DateTime oneYearAgo = now.minusYears(1);
		DateTime year1997 = new DateTime(1997, 1, 1, 12, 0, 0, 0);

		for (Expense expense : expenses) {
			if (expense.getEnd() == null || expense.getEnd()
					.after(now.toDate())) {
				switch (expense.getPeriod()) {
					case MONTHLY:
						sum = sum.add(expense.getAmount().multiply(factor12));
						break;
					case QUARTERLY:
						sum = sum.add(expense.getAmount().multiply(factor4));
						break;
					case SEMIANNUALLY:
						sum = sum.add(expense.getAmount().multiply(factor2));
						break;
					case ANNUALLY:
						sum = sum.add(expense.getAmount());
						break;
					case BIANNUALLY:
						sum = sum.add(expense.getAmount()
								.divide(factor2, RoundingMode.HALF_UP));
						break;
				}

				filtered.add(expense);
			}
		}

		add(new ListView<Expense>("expenses", ModelMaker.wrap(filtered)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<Expense> item) {
				Expense expense = item.getModelObject();

				item.add(new Label("name", expense.getName()));
				String regularity;
				IModel<String> yearlyExpense;

				boolean amountPerTermVisible = true;

				switch (expense.getPeriod()) {
					case MONTHLY:
						regularity = "Monthly";
						yearlyExpense = new DollarSignModel(new Model<>(
								expense.getAmount().multiply(factor12)));
						break;
					case QUARTERLY:
						regularity = "Quarterly";
						yearlyExpense = new DollarSignModel(new Model<>(
								expense.getAmount().multiply(factor4)));
						break;
					case SEMIANNUALLY:
						regularity = "Semiannually";
						yearlyExpense = new DollarSignModel(new Model<>(
								expense.getAmount().multiply(factor2)));
						break;
					case BIANNUALLY:
						regularity = "Biannually";
						yearlyExpense = new DollarSignModel(new Model<>(
								expense.getAmount().divide(factor2,
										RoundingMode.HALF_UP)));
						break;
					default:
						amountPerTermVisible = false;
						regularity = "Annually";
						yearlyExpense = new DollarSignModel(
								new Model<>(expense.getAmount()));

				}

				item.add(new Label("regularity", regularity));

				item.add(new Label("amounteach",
						new DollarSignModel(new Model<>(expense.getAmount())))
						.setVisible(amountPerTermVisible));
				item.add(new Label("annualfee", yearlyExpense));
			}
		});

		add(new Label("annualcost", new DollarSignModel(new Model<>(sum))));

		FinancialTimeline timeline = new FinancialTimeline(expenseDAO,
				donationDAO, subscriptionDAO, paidExpenseDAO);

		// Finance overview charts

		add(GraphUtil
				.makeCashFlowLineChart("monthlychart", "Cash flow last month",
						timeline.getCashFlow(oneMonthAgo, now)));
		add(GraphUtil
				.makeCashFlowLineChart("yearlychart", "Cash flow last year",
						timeline.getCashFlow(oneYearAgo, now)));
		add(GraphUtil
				.makeCashFlowLineChart("alltimechart", "Cash flow all time",
						timeline.getCashFlow(year1997, now)));
		add(GraphUtil.makePieChart("monthlyparticipation",
				"Participation last month",
				timeline.getParticipation(oneMonthAgo, now)));
		add(GraphUtil
				.makePieChart("annualparticipation", "Participation last year",
						timeline.getParticipation(oneYearAgo, now)));
		add(GraphUtil
				.makePieChart("alltimeparticipation", "All time participation",
						timeline.getParticipation(year1997, now)));

		add(GraphUtil
				.makeReservesBarChart("reserves", "Cash reserves by donator",
						timeline.getReservesAt(now)));

		add(new BookmarkablePageLink<CharterPage>("charter",
				CharterPage.class));
		add(new WebMarkupContainer("un").add(AttributeModifier.replace("value",
				"Tysan Donation by " + getUser().getUsername())));
		User treasurer = roleService.getTreasurer();

		add(new HiddenField<>("paypalAddress", new Model<>(
				treasurer != null ? treasurer.getPaypalAddress() : "disable"))
				.add(AttributeModifier.replace("name", "business")));

		add(new ActivateSubscriptionPanel("activatesubscription", getUser(),
				new FinancePageLink()));
		add(new ViewSubscriptionPanel("viewsubscription", getUser(),
				new FinancePageLink()));
	}

	private static class FinancePageLink implements IOnSubmitPageCreator {
		private static final long serialVersionUID = 1L;

		@Override
		public Page createPage() {
			return new FinancePage();
		}

	}
}
