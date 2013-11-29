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
package com.tysanclan.site.projectewok.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Donation extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Donation")
	@SequenceGenerator(name = "Donation", sequenceName = "SEQ_ID_Donation", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_Donation_Donator")
	private User donator;

	@Column
	@Index(name = "IDX_Donation_Time")
	private Date donationTime;

	@Column
	private BigDecimal amount;

	// $P$

	/**
	 * Creates a new Donation object
	 */
	public Donation() {
		// $H$
	}

	/**
	 * Returns the ID of this Donation
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Donation
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Donator of this Donation
	 */
	public User getDonator() {
		return this.donator;
	}

	/**
	 * Sets the Donator of this Donation
	 * 
	 * @param donator
	 *            The Donator of this Donation
	 */
	public void setDonator(User donator) {
		this.donator = donator;
	}

	/**
	 * @return The DonationTime of this Donation
	 */
	public Date getDonationTime() {
		return this.donationTime;
	}

	/**
	 * Sets the DonationTime of this Donation
	 * 
	 * @param donationTime
	 *            The DonationTime of this Donation
	 */
	public void setDonationTime(Date donationTime) {
		this.donationTime = donationTime;
	}

	/**
	 * @return The Amount of this Donation
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * Sets the Amount of this Donation
	 * 
	 * @param amount
	 *            The Amount of this Donation
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return amount.toString() + " by "
				+ (donator != null ? donator.getUsername() : "Anonymous");
	}

	// $GS$
}
