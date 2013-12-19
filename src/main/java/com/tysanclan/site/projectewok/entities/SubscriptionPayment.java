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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Index;

import com.google.common.base.Predicate;
import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

@Entity
public class SubscriptionPayment extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	public static class UnpaidFilter implements Predicate<SubscriptionPayment> {
		@Override
		public boolean apply(SubscriptionPayment input) {
			return !input.isPaid();
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SubscriptionPayment")
	@SequenceGenerator(name = "SubscriptionPayment", sequenceName = "SEQ_ID_SubscriptionPayment", allocationSize = 1, initialValue = 1)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@Index(name = "IDX_SUBPAYMENT_SUB")
	private Subscription subscription;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@Index(name = "IDX_SUBPAYMENT_USER")
	private User user;

	@Column(nullable = false)
	private Date date;

	@Column(nullable = false)
	private boolean paid;

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

	/**
	 * @return the subscription
	 */
	public Subscription getSubscription() {
		return subscription;
	}

	/**
	 * @param subscription the subscription to set
	 */
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the paid
	 */
	public boolean isPaid() {
		return paid;
	}

	/**
	 * @param paid the paid to set
	 */
	public void setPaid(boolean paid) {
		this.paid = paid;
	}

}
