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
package com.tysanclan.site.projectewok.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.SortedMap;

import com.tysanclan.site.projectewok.entities.Donation;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;
import com.tysanclan.site.projectewok.entities.PaymentRequest;
import com.tysanclan.site.projectewok.entities.Subscription;
import com.tysanclan.site.projectewok.entities.SubscriptionPayment;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface FinanceService {
	public Expense createExpense(String name, BigDecimal amount,
			ExpensePeriod period, Date from);

	public Donation createDonation(User user, BigDecimal amount,
			Date donationTime);

	public void setExpenseEnded(Expense expense, Date date);

	public Set<Donation> getDonationsByPeriod(Date start, Date end);

	public SortedMap<Date, BigDecimal> calculateContributions(Date start,
			Date end);

	public SortedMap<Date, BigDecimal> getExpenses(Date start, Date end);

	void removeDonation(User remover, Donation donation);

	void updateExpense(Expense expense, String expenseName, BigDecimal amount,
			ExpensePeriod period, Date startDate, Date endDate);

	boolean fulfillPayment(PaymentRequest request);

	void cancelRequest(PaymentRequest request);

	PaymentRequest requestPayment(User requester, String itemRequested,
			BigDecimal amount);

	public boolean cancelSubscription(Subscription subscription);

	public boolean subscribe(User subscriber, BigDecimal amount,
			ExpensePeriod interval);

	public void transformExpensesToPaidExpenses();

	public void checkDue(Subscription s);

	public boolean paySubscriptionDue(SubscriptionPayment payment);

	public void checkSubscriptionsDue();

}
