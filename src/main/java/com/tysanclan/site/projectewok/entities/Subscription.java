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
package com.tysanclan.site.projectewok.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;

@Entity
public class Subscription extends BaseDomainObject {
	public class DueFilter implements Predicate<SubscriptionPayment> {
		@Override
		public boolean apply(SubscriptionPayment input) {
			return input.isPaid();
		}
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Subscription")
	@SequenceGenerator(name = "Subscription", sequenceName = "SEQ_ID_Subscription", allocationSize = 1, initialValue = 1)
	private Long id;

	@Column(nullable = false)
	private BigDecimal amount;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@Index(name = "IDX_SUBSCRIPTION_USER")
	private User subscriber;

	@Column(nullable = false)
	private Date start;

	@Column(nullable = false)
	private ExpensePeriod interval;

	@OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SubscriptionPayment> payments;

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setInterval(ExpensePeriod interval) {
		this.interval = interval;
	}

	public ExpensePeriod getInterval() {
		return interval;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}

	public Date getStart() {
		return start;
	}

	public User getSubscriber() {
		return subscriber;
	}

	public List<SubscriptionPayment> getPayments() {
		if (payments == null)
			payments = new ArrayList<SubscriptionPayment>(0);

		return payments;
	}

	public void setPayments(List<SubscriptionPayment> payments) {
		this.payments = payments;
	}

	@Transient
	public int countPaymentsDue() {
		return ImmutableList.copyOf(
				Iterables.filter(getPayments(), new DueFilter())).size();
	}
}
