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
package com.tysanclan.site.projectewok.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;

import nl.topicus.wqplot.data.BaseSeries;
import nl.topicus.wqplot.data.DateNumberSeries;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.hyperion.util.MapUtil;
import com.tysanclan.site.projectewok.entities.Donation;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.PaidExpense;
import com.tysanclan.site.projectewok.entities.Subscription;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.DonationDAO;
import com.tysanclan.site.projectewok.entities.dao.ExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.PaidExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.DonationFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.PaidExpenseFilter;

public class FinancialTimeline {

	private static class SerializableEntry<K, V> implements Serializable,
			Entry<K, V> {
		private static final long serialVersionUID = 1L;

		private K k;

		private V v;

		public SerializableEntry(K k, V v) {
			super();
			this.k = k;
			this.v = v;
		}

		@Override
		public K getKey() {
			return k;
		}

		@Override
		public V getValue() {

			return v;
		}

		@Override
		public V setValue(V value) {
			// Immutable
			return v;
		}
	}

	private static class EntryCopy implements
			Function<Entry<DateTime, BigDecimal>, Entry<DateTime, BigDecimal>> {
		public Entry<DateTime, BigDecimal> apply(
				Entry<DateTime, BigDecimal> input) {
			return new SerializableEntry<DateTime, BigDecimal>(input.getKey(),
					input.getValue());
		}
	}

	public static class SpendingComparator implements
			Comparator<Entry<DateTime, BigDecimal>> {

		@Override
		public int compare(Entry<DateTime, BigDecimal> o1,
				Entry<DateTime, BigDecimal> o2) {
			return o1.getKey().compareTo(o2.getKey());
		}
	}

	public static class ReserveToBigDecimalFunction implements
			EntryTransformer<User, Reserve, BigDecimal> {
		@Override
		public BigDecimal transformEntry(User key, Reserve value) {
			return value.getAmount();
		}
	}

	public static class SumFunction implements
			Function<Collection<BigDecimal>, BigDecimal> {
		private static final SumFunction INSTANCE = new SumFunction();

		@Override
		public BigDecimal apply(Collection<BigDecimal> input) {
			BigDecimal result = BigDecimal.ZERO;

			for (BigDecimal next : input) {
				result = result.add(next);
			}

			return result;
		}
	}

	private static class Contribution {
		private final User user;

		private final BigDecimal amount;

		/**
		 * @param user
		 * @param amount
		 */
		public Contribution(User user, BigDecimal amount) {
			super();
			this.user = user;
			this.amount = amount;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public User getUser() {
			return user;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((amount == null) ? 0 : amount.hashCode());
			result = prime * result + ((user == null) ? 0 : user.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Contribution other = (Contribution) obj;
			if (amount == null) {
				if (other.amount != null)
					return false;
			} else if (!amount.equals(other.amount))
				return false;
			if (user == null) {
				if (other.user != null)
					return false;
			} else if (!user.equals(other.user))
				return false;
			return true;
		}

	}

	private static class Reserve implements Comparable<Reserve> {
		private final DateTime time;

		private final User user;

		private final BigDecimal amount;

		/**
		 * @param time
		 * @param user
		 * @param amount
		 */
		public Reserve(DateTime time, User user, BigDecimal amount) {
			super();
			this.time = time;
			this.user = user;
			this.amount = amount;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((amount == null) ? 0 : amount.hashCode());
			result = prime * result + ((time == null) ? 0 : time.hashCode());
			result = prime * result + ((user == null) ? 0 : user.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Reserve other = (Reserve) obj;
			if (amount == null) {
				if (other.amount != null)
					return false;
			} else if (!amount.equals(other.amount))
				return false;
			if (time == null) {
				if (other.time != null)
					return false;
			} else if (!time.equals(other.time))
				return false;
			if (user == null) {
				if (other.user != null)
					return false;
			} else if (!user.equals(other.user))
				return false;
			return true;
		}

		public DateTime getTime() {
			return time;
		}

		public User getUser() {
			return user;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		@Override
		public int compareTo(Reserve o) {
			User u1 = getUser();
			User u2 = o.getUser();

			int c1 = getTime().compareTo(o.getTime());
			int c2 = getAmount().compareTo(o.getAmount());
			int c3 = compareUsernames(u1, u2);

			return c1 == 0 ? (c2 == 0 ? c3 : c2) : c1;
		}

		private int compareUsernames(User u1, User u2) {
			if (u1 == null && u2 == null) {
				return 0;
			} else {
				if (u1 == null) {
					return -1;
				} else if (u2 == null) {
					return 1;
				} else {
					return u1.getUsername().compareTo(u2.getUsername());
				}
			}

		}

	}

	private static class CashReserve {

		private Map<DateTime, List<Reserve>> reserves = new TreeMap<DateTime, List<Reserve>>();

		private Map<DateTime, List<BigDecimal>> spendings = new TreeMap<DateTime, List<BigDecimal>>();

		private Map<DateTime, Map<User, BigDecimal>> buffer = new HashMap<DateTime, Map<User, BigDecimal>>();

		public Map<User, BigDecimal> getReservesAt(DateTime time) {
			if (buffer.containsKey(time))
				return buffer.get(time);

			ListMultimap<User, Reserve> result = ArrayListMultimap.create();

			// Add donations as active reserves
			for (Entry<DateTime, List<Reserve>> e : reserves.entrySet()) {
				for (Reserve r : e.getValue()) {
					if (!e.getKey().isAfter(time)) {
						result.put(r.getUser(), r);
					}
				}
			}

			SortedSet<Entry<DateTime, BigDecimal>> entries = Sets
					.newTreeSet(new SpendingComparator());

			Map<DateTime, BigDecimal> transformed = Maps
					.transformEntries(
							spendings,
							new EntryTransformer<DateTime, List<BigDecimal>, BigDecimal>() {
								private SumFunction function = new SumFunction();

								@Override
								public BigDecimal transformEntry(DateTime key,
										List<BigDecimal> value) {
									return function.apply(value);
								}
							});

			entries.addAll(ImmutableList.copyOf(Iterables.transform(
					transformed.entrySet(), new EntryCopy())));

			// Loop through actual expenses and subtract from existing reserves
			spendings: for (Entry<DateTime, BigDecimal> e : entries) {
				if (!e.getKey().isAfter(time)) {
					BigDecimal open = e.getValue();

					while (open.compareTo(BigDecimal.ZERO) > 0
							&& !result.isEmpty()) {
						Entry<User, Reserve> first = null;
						boolean found = false;

						Iterator<Entry<User, Reserve>> iterator = result
								.entries().iterator();
						while (iterator.hasNext()) {
							first = iterator.next();

							if (!first.getValue().getTime().isAfter(e.getKey())) {
								found = true;
								break;
							}
						}

						if (!found) {
							continue spendings;
						}

						result.remove(first.getKey(), first.getValue());

						Reserve reserve = first.getValue();

						BigDecimal amount = reserve.getAmount();

						if (open.compareTo(amount) > 0) {
							open = open.subtract(amount);
						} else {
							BigDecimal remainder = amount.subtract(open);
							result.put(
									first.getKey(),
									new Reserve(reserve.getTime(), reserve
											.getUser(), remainder));
							open = BigDecimal.ZERO;
						}
					}
				}
			}

			Multimap<User, BigDecimal> aggregated = Multimaps.transformEntries(
					result, new ReserveToBigDecimalFunction());
			Map<User, BigDecimal> results = Maps.transformValues(
					aggregated.asMap(), SumFunction.INSTANCE);

			buffer.put(time, results);

			return results;
		}

		public void addReserve(DateTime dt, User user, BigDecimal amount) {
			MapUtil.listAdd(reserves, dt, new Reserve(dt, user, amount));
		}

		public void spend(DateTime dt, BigDecimal amount) {
			MapUtil.listAdd(spendings, dt, amount);
		}

	}

	private CashReserve reserve;

	private Map<DateTime, List<BigDecimal>> mutations = new TreeMap<DateTime, List<BigDecimal>>();

	private Map<DateTime, BigDecimal> cashFlow = new TreeMap<DateTime, BigDecimal>();

	private Multimap<Long, Contribution> contributionsByExpense = LinkedListMultimap
			.create();

	private Multimap<DateTime, Contribution> contributionsByDate = ArrayListMultimap
			.create();

	private ExpenseDAO expenseDAO;

	private DonationDAO donationDAO;

	private SubscriptionDAO subscriptionDAO;

	private PaidExpenseDAO paidExpenseDAO;

	private String exhaustionPoint = null;

	public FinancialTimeline(ExpenseDAO expenseDAO, DonationDAO donationDAO,
			SubscriptionDAO subscriptionDAO, PaidExpenseDAO paidExpenseDAO) {
		this.expenseDAO = expenseDAO;
		this.donationDAO = donationDAO;
		this.subscriptionDAO = subscriptionDAO;
		this.paidExpenseDAO = paidExpenseDAO;

		init();
	}

	private void init() {
		reserve = new CashReserve();

		DonationFilter df = new DonationFilter();
		df.addOrderBy("donationTime", true);
		for (Donation d : donationDAO.findByFilter(df)) {
			DateTime dt = new DateTime(d.getDonationTime());
			MapUtil.listAdd(mutations, dt, d.getAmount());
			reserve.addReserve(dt, d.getDonator(), d.getAmount());
		}

		PaidExpenseFilter pef = new PaidExpenseFilter();
		pef.addOrderBy("paymentDate", true);

		for (PaidExpense expense : paidExpenseDAO.findByFilter(pef)) {
			DateTime dt = new DateTime(expense.getPaymentDate());
			MapUtil.listAdd(mutations, dt, expense.getAmount().negate());
			reserve.spend(dt, expense.getAmount());
		}

		BigDecimal current = BigDecimal.ZERO;
		DateTime last = null;

		for (Entry<DateTime, List<BigDecimal>> e : mutations.entrySet()) {
			if (!e.getKey().equals(last)) {
				if (current.compareTo(BigDecimal.ZERO) < 0) {
					current = BigDecimal.ZERO;
				}
				if (last != null) {
					cashFlow.put(last, current);
				}
			}

			last = e.getKey();

			current = current.add(SumFunction.INSTANCE.apply(e.getValue()));

		}

		if (last != null) {
			cashFlow.put(last, current);
		}

		expenses: for (PaidExpense expense : paidExpenseDAO.findByFilter(pef)) {
			DateTime dt = new DateTime(expense.getPaymentDate());

			BigDecimal toFulfill = expense.getAmount();

			Map<User, BigDecimal> reservesAtTime = Maps
					.newLinkedHashMap(reserve.getReservesAt(dt));

			for (Entry<User, BigDecimal> e : reservesAtTime.entrySet()) {
				if (e.getValue().compareTo(toFulfill) >= 0) {
					Contribution c = new Contribution(e.getKey(), toFulfill);

					contributionsByDate.put(dt, c);
					contributionsByExpense.put(expense.getId(), c);

					continue expenses;
				} else {
					Contribution c = new Contribution(e.getKey(), e.getValue());

					contributionsByDate.put(dt, c);
					contributionsByExpense.put(expense.getId(), c);

					toFulfill = toFulfill.subtract(e.getValue());
				}
			}

			if (toFulfill.compareTo(BigDecimal.ZERO) > 0) {
				Contribution c = new Contribution(expense.getPaidBy(),
						toFulfill);
				contributionsByDate.put(dt, c);
				contributionsByExpense.put(expense.getId(), c);

			}
		}

		Map<DateTime, List<BigDecimal>> futureMutations = new TreeMap<DateTime, List<BigDecimal>>();

		DateTime now = new DateTime();
		DateTime threeYearsFromNow = now.plusYears(3);
		List<Expense> expenses = expenseDAO.getActiveExpenses();
		for (Expense e : expenses) {
			DateTime c = now;

			DateTime eStart = new DateTime(e.getStart());
			DateTime eEnd = e.getEnd() != null ? new DateTime(e.getEnd())
					: null;

			while (!c.isAfter(threeYearsFromNow)
					&& (e.getEnd() == null || !c.isAfter(eEnd))) {
				if (!c.isBefore(eStart)) {
					MapUtil.listAdd(futureMutations, c, e.getAmount().negate());
				}

				c = new DateTime(e.getPeriod().nextDate(c.toDate()));
			}
		}

		List<Subscription> subscriptions = subscriptionDAO.findAll();
		for (Subscription s : subscriptions) {
			DateTime c = now;

			DateTime sStart = new DateTime(s.getStart());

			while (!c.isAfter(threeYearsFromNow)) {
				if (!c.isBefore(sStart)) {
					MapUtil.listAdd(futureMutations, c, s.getAmount());
				}

				c = new DateTime(s.getInterval().nextDate(c.toDate()));
			}
		}

		for (Entry<DateTime, List<BigDecimal>> e : futureMutations.entrySet()) {
			current = current.add(SumFunction.INSTANCE.apply(e.getValue()));

			if (current.compareTo(BigDecimal.ZERO) < 0) {
				exhaustionPoint = "until "
						+ e.getKey().toString("dd MMMM yyyy");
				break;
			}
		}

	}

	public String getExhaustionPoint() {
		return exhaustionPoint;
	}

	public IModel<List<BaseSeries<String, BigDecimal>>> getReservesAt(
			DateTime time) {
		BaseSeries<String, BigDecimal> series = new BaseSeries<String, BigDecimal>();

		Map<User, BigDecimal> reservesAt = reserve.getReservesAt(time);

		for (Entry<User, BigDecimal> e : reservesAt.entrySet()) {
			series.addEntry(e.getKey() != null ? e.getKey().getUsername()
					: "Anonymous", e.getValue());
		}

		return listOf(series);

	}

	public IModel<List<BaseSeries<String, BigDecimal>>> getParticipation(
			DateTime start, DateTime end) {
		BaseSeries<String, BigDecimal> series = new BaseSeries<String, BigDecimal>();

		Multimap<String, BigDecimal> amounts = HashMultimap.create();

		for (Entry<DateTime, Contribution> e : contributionsByDate.entries()) {
			if (!e.getKey().isBefore(start) && !e.getKey().isAfter(end)) {
				amounts.put(e.getValue().getUser() != null ? e.getValue()
						.getUser().getUsername() : "Anonymous", e.getValue()
						.getAmount());
			}
		}

		Map<String, BigDecimal> sums = Maps.transformValues(amounts.asMap(),
				SumFunction.INSTANCE);

		for (Entry<String, BigDecimal> e : sums.entrySet()) {
			series.addEntry(e.getKey(), e.getValue());
		}

		return listOf(series);
	}

	public IModel<List<DateNumberSeries<BigDecimal>>> getCashFlow(
			DateTime start, DateTime end) {

		DateNumberSeries<BigDecimal> series = new DateNumberSeries<BigDecimal>();

		for (Entry<DateTime, BigDecimal> e : cashFlow.entrySet()) {
			if (!e.getKey().isBefore(start) && !e.getKey().isAfter(end)) {
				series.addEntry(e.getKey().toDate(), e.getValue());
			}
		}

		return listOf(series);
	}

	private <T extends Number> IModel<List<DateNumberSeries<T>>> listOf(
			DateNumberSeries<T> series) {
		List<DateNumberSeries<T>> res = new ArrayList<DateNumberSeries<T>>(1);

		res.add(series);

		return new ListModel<DateNumberSeries<T>>(res);
	}

	private <K, V extends Number> IModel<List<BaseSeries<K, V>>> listOf(
			BaseSeries<K, V> series) {
		List<BaseSeries<K, V>> res = new ArrayList<BaseSeries<K, V>>(1);

		res.add(series);

		return new ListModel<BaseSeries<K, V>>(res);
	}

	public BigDecimal getReservesToday() {

		BigDecimal r = BigDecimal.ZERO;
		for (BigDecimal v : reserve.getReservesAt(new DateTime()).values()) {
			r = r.add(v);
		}
		return r;
	}

}
