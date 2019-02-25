/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.beans.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import io.vavr.collection.Seq;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.beans.NotificationService;
import com.tysanclan.site.projectewok.entities.Donation;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;
import com.tysanclan.site.projectewok.entities.PaidExpense;
import com.tysanclan.site.projectewok.entities.PaymentRequest;
import com.tysanclan.site.projectewok.entities.Subscription;
import com.tysanclan.site.projectewok.entities.SubscriptionPayment;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.DonationDAO;
import com.tysanclan.site.projectewok.entities.dao.ExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.PaidExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.PaymentRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionDAO;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionPaymentDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.DonationFilter;
import com.tysanclan.site.projectewok.entities.filter.ExpenseFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class FinanceServiceImpl implements
		com.tysanclan.site.projectewok.beans.FinanceService {
	@Autowired
	private ExpenseDAO expenseDAO;

	@Autowired
	private PaidExpenseDAO paidExpenseDAO;

	@Autowired
	private DonationDAO donationDAO;
	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.RoleService roleService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private PaymentRequestDAO requestDAO;

	@Autowired
	private SubscriptionDAO subscriptionDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private SubscriptionPaymentDAO subscriptionPaymentDAO;

	public void setSubscriptionPaymentDAO(
			SubscriptionPaymentDAO subscriptionPaymentDAO) {
		this.subscriptionPaymentDAO = subscriptionPaymentDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setSubscriptionDAO(SubscriptionDAO subscriptionDAO) {
		this.subscriptionDAO = subscriptionDAO;
	}

	public void setPaidExpenseDAO(PaidExpenseDAO paidExpenseDAO) {
		this.paidExpenseDAO = paidExpenseDAO;
	}

	/**
	 * @param logService the logService to set
	 */
	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	/**
	 * @param roleService the roleService to set
	 */
	public void setRoleService(
			com.tysanclan.site.projectewok.beans.RoleService roleService) {
		this.roleService = roleService;
	}

	public void setNotificationService(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public void setRequestDAO(PaymentRequestDAO requestDAO) {
		this.requestDAO = requestDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Expense createExpense(String name, BigDecimal amount,
								 ExpensePeriod period, Date start) {
		Expense expense = new Expense();
		expense.setName(name);
		expense.setAmount(amount);
		expense.setPeriod(period);
		expense.setStart(start);
		expenseDAO.save(expense);

		return expense;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean paySubscriptionDue(SubscriptionPayment payment) {
		if (payment != null && !payment.isPaid()) {

			User treasurer = roleService.getTreasurer();

			notificationService.notifyUser(treasurer,
					"Subscriber donation of "
							+ payment.getSubscription().getAmount() + " by "
							+ payment.getUser()
							+ ", please verify at your Paypal account");

			payment.setPaid(true);
			subscriptionPaymentDAO.update(payment);

			createDonation(payment.getUser(), payment.getSubscription()
					.getAmount(), new Date());

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.FinanceService#createDonation(com.tysanclan.site.projectewok.entities.User,
	 * java.math.BigDecimal, java.util.Date)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Donation createDonation(User donator, BigDecimal amount,
								   Date donationTime) {
		Donation donation = new Donation();
		donation.setAmount(amount);
		donation.setDonationTime(donationTime);
		donation.setDonator(donator);
		donationDAO.save(donation);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		if (!donationTime.before(cal.getTime())) {
			logService.logUserAction(donator, "Finance",
					"User has made a donation");
		}

		return donation;
	}

	/**
	 * @param expenseDAO the expenseDAO to set
	 */
	public void setExpenseDAO(ExpenseDAO expenseDAO) {
		this.expenseDAO = expenseDAO;
	}

	/**
	 * @param donationDAO the donationDAO to set
	 */
	public void setDonationDAO(DonationDAO donationDAO) {
		this.donationDAO = donationDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.FinanceService#calculateContributions(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public SortedMap<Date, BigDecimal> calculateContributions(Date start,
															  Date end) {
		SortedMap<Date, BigDecimal> expenses = new TreeMap<Date, BigDecimal>();

		addDonationMutations(start, end, expenses);

		return expenses;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.FinanceService#setExpenseEnded(com.tysanclan.site.projectewok.entities.Expense,
	 * java.util.Date)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setExpenseEnded(Expense expense, Date date) {
		expenseDAO.load(expense.getId()).forEach(subject -> {
			subject.setEnd(DateUtil.getMidnightDate(date));
			expenseDAO.update(subject);
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.FinanceService#getExpenses(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public SortedMap<Date, BigDecimal> getExpenses(Date start, Date end) {
		SortedMap<Date, BigDecimal> mutations = new TreeMap<Date, BigDecimal>();
		SortedMap<Date, BigDecimal> expenses = new TreeMap<Date, BigDecimal>();

		addExpenseMutations(start, end, mutations);

		for (Entry<Date, BigDecimal> entry : mutations.entrySet()) {
			expenses.put(entry.getKey(), entry.getValue().abs());
		}

		return expenses;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.FinanceService#getDonationsByPeriod(java.util.Date,
	 * java.util.Date)
	 */
	@Override
	public Set<Donation> getDonationsByPeriod(Date _start, Date end) {
		Set<Donation> result = new HashSet<Donation>();

		Date start = _start;

		if (start == null) {
			start = DateUtil.tysanFoundation;
		}

		Seq<Donation> donations = donationDAO.findAll();
		for (Donation donation : donations) {
			if (!donation.getDonationTime().before(start)
					&& !donation.getDonationTime().after(end)) {
				result.add(donation);
			}
		}

		return result;
	}

	private void addExpenseMutations(Date _start, Date end,
									 SortedMap<Date, BigDecimal> mutations) {
		Date start = _start;

		ExpenseFilter filter = new ExpenseFilter();
		filter.end(end);

		if (start == null) {
			start = DateUtil.tysanFoundation;
		}

		filter.start(start);


		Seq<Expense> expenses = expenseDAO.findByFilter(filter);
		for (Expense expense : expenses) {
			for (Date date : getPaymentDates(expense)) {
				if (!date.before(start) && !date.after(end)) {
					if (mutations.containsKey(date)) {
						mutations.put(
								date,
								mutations.get(date).subtract(
										expense.getAmount()));
					} else {
						mutations.put(date, expense.getAmount().negate());
					}
				}
			}
		}
	}

	private void addDonationMutations(Date start, Date end,
									  SortedMap<Date, BigDecimal> mutations) {
		DonationFilter dFilter = new DonationFilter();
		dFilter.donationTime().between(start, end);
		Seq<Donation> donations = donationDAO.findByFilter(dFilter);
		for (Donation donation : donations) {
			Date date = donation.getDonationTime();
			if (mutations.containsKey(date)) {
				mutations.put(date,
						mutations.get(date).add(donation.getAmount()));
			} else {
				mutations.put(date, donation.getAmount());
			}
		}
	}

	private SortedSet<Date> getPaymentDates(Expense expense) {
		SortedSet<Date> result = new TreeSet<Date>();
		Calendar cal = Calendar.getInstance();

		Date start = expense.getStart() != null ? expense.getStart()
				: DateUtil.tysanFoundation;

		cal.setTime(expense.getLastPayment() != null ? expense.getLastPayment()
				: start);

		while (!cal.getTime().before(start)) {
			int field = Calendar.YEAR;
			if (expense.getPeriod().equals(ExpensePeriod.MONTHLY)) {
				field = Calendar.MONTH;
			}
			if (!result.contains(cal.getTime())) {
				result.add(cal.getTime());
			}
			cal.add(field, -1);
		}

		return result;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.FinanceService#removeDonation(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Donation)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeDonation(User remover, Donation donation) {
		if (remover.equals(roleService.getTreasurer())) {
			donationDAO.delete(donation);
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.FinanceService#updateExpense(com.tysanclan.site.projectewok.entities.Expense,
	 * java.lang.String, java.math.BigDecimal,
	 * com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod,
	 * java.util.Date, java.util.Date)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateExpense(Expense _expense, String expenseName,
							  BigDecimal amount, ExpensePeriod period, Date startDate,
							  Date endDate) {
		expenseDAO.load(_expense.getId()).forEach(expense -> {
			expense.setName(expenseName);
			expense.setAmount(amount);
			expense.setPeriod(period);
			expense.setStart(startDate);
			expense.setEnd(endDate);
			expenseDAO.update(expense);
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean fulfillPayment(PaymentRequest request) {
		if (request != null) {

			notificationService.notifyUser(request.getRequester(),
					"Payment of " + request.getItem() + " fulfilled");

			User paidBy = roleService.getTreasurer();

			PaidExpense expense = new PaidExpense();
			expense.setAmount(request.getAmount());
			expense.setPaidBy(paidBy);
			expense.setName(request.getItem());
			expense.setPaymentDate(new Date());
			paidExpenseDAO.save(expense);

			requestDAO.delete(request);

			return true;
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void cancelRequest(PaymentRequest request) {
		if (request != null) {
			requestDAO.delete(request);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean cancelSubscription(Subscription subscription) {
		if (subscription.countPaymentsDue() == 0) {
			subscriptionDAO.delete(subscription);
			return true;
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean subscribe(User subscriber, BigDecimal amount,
							 ExpensePeriod interval) {
		if (subscriber.getSubscription() == null) {
			Subscription subscription = new Subscription();
			subscription.setAmount(amount);
			subscription.setInterval(interval);
			subscription.setStart(new Date());
			subscription.setSubscriber(subscriber);
			subscriptionDAO.save(subscription);

			checkDue(subscription);

			return true;
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PaymentRequest requestPayment(User requester, String itemRequested,
										 BigDecimal amount) {
		PaymentRequest request = new PaymentRequest();
		request.setAmount(amount);
		request.setItem(itemRequested);
		request.setRequester(requester);
		requestDAO.save(request);

		return request;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void transformExpensesToPaidExpenses() {
		Date now = new Date();

		UserFilter filter = new UserFilter();
		filter.username("Prospero");

		userDAO.getUniqueByFilter(filter).forEach(prospero -> {

			for (Expense e : expenseDAO.findAll()) {
				Date d = e.getStart();

				while ((e.getEnd() == null || !d.after(e.getEnd()))
						&& !d.after(now)) {
					PaidExpense pe = new PaidExpense();
					pe.setAmount(e.getAmount());
					pe.setPaidBy(prospero);
					pe.setPaymentDate(d);
					pe.setName(e.getName());
					paidExpenseDAO.save(pe);

					d = e.getPeriod().nextDate(d);
				}
			}
		});

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkDue(Subscription _s) {
		subscriptionDAO.load(_s.getId()).forEach(s -> {

			Set<DateTime> dates = new TreeSet<DateTime>();

			for (SubscriptionPayment sp : s.getPayments()) {
				dates.add(new DateTime(sp.getDate()));
			}

			DateTime now = new DateTime();
			DateTime c = new DateTime(s.getStart());

			while (!c.isAfter(now)) {
				if (!dates.contains(c)) {
					SubscriptionPayment sp = new SubscriptionPayment();
					sp.setDate(c.toDate());
					sp.setPaid(false);
					sp.setSubscription(s);
					sp.setUser(s.getSubscriber());
					subscriptionPaymentDAO.save(sp);
				}

				c = new DateTime(s.getInterval().nextDate(c.toDate()));
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkSubscriptionsDue() {
		for (Subscription s : subscriptionDAO.findAll()) {
			checkDue(s);
		}
	}
}
