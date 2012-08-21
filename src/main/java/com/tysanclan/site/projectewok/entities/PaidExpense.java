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

import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

@Entity
public class PaidExpense extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PaidExpense")
	@SequenceGenerator(name = "PaidExpense", sequenceName = "SEQ_ID_PaidExpense", allocationSize = 1, initialValue = 1)
	private Long id;

	@Column(nullable = false)
	private BigDecimal amount;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@Index(name = "IDX_PAIDEXPENSE_PAIDBY")
	private User paidBy;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Date paymentDate;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the paidBy
	 */
	public User getPaidBy() {
		return paidBy;
	}

	/**
	 * @param paidBy the paidBy to set
	 */
	public void setPaidBy(User paidBy) {
		this.paidBy = paidBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}
}
