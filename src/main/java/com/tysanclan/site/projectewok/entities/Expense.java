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
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.StringUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Expense extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	public static class ExpensePeriodRenderer implements
			IChoiceRenderer<ExpensePeriod> {
		private static final long serialVersionUID = 1L;

		private final boolean lowercased;

		public ExpensePeriodRenderer(boolean lowercased) {
			this.lowercased = lowercased;
		}

		@Override
		public Object getDisplayValue(ExpensePeriod object) {

			return lowercased ? object.getOmschrijving() : object
					.getOmschrijving().toLowerCase();
		}

		@Override
		public String getIdValue(ExpensePeriod object, int index) {
			return object.name();
		}
	}

	public static enum ExpensePeriod {
		MONTHLY("Every month") {
			@Override
			public Date nextDate(Date curr) {
				Calendar cal = DateUtil.getCalendarInstance();
				cal.setTime(curr);

				cal.add(Calendar.MONTH, 1);

				return cal.getTime();
			}
		},
		QUARTERLY("Every 3 months") {
			@Override
			public Date nextDate(Date curr) {
				Calendar cal = DateUtil.getCalendarInstance();
				cal.setTime(curr);

				cal.add(Calendar.MONTH, 3);

				return cal.getTime();
			}
		},
		SEMIANNUALLY("Every 6 months") {
			@Override
			public Date nextDate(Date curr) {
				Calendar cal = DateUtil.getCalendarInstance();
				cal.setTime(curr);

				cal.add(Calendar.MONTH, 6);

				return cal.getTime();
			}
		},
		ANNUALLY("Every year") {
			@Override
			public Date nextDate(Date curr) {
				Calendar cal = DateUtil.getCalendarInstance();
				cal.setTime(curr);

				cal.add(Calendar.YEAR, 1);

				return cal.getTime();
			}
		},
		BIANNUALLY("Every two years") {
			@Override
			public Date nextDate(Date curr) {
				Calendar cal = DateUtil.getCalendarInstance();
				cal.setTime(curr);

				cal.add(Calendar.YEAR, 2);

				return cal.getTime();
			}
		};

		private ExpensePeriod(String omschrijving) {
			this.omschrijving = omschrijving;
		}

		private final String omschrijving;

		public String getOmschrijving() {
			return omschrijving;
		}

		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return StringUtil.combineStrings(name().substring(0, 1)
					.toUpperCase(), name().substring(1).toLowerCase());
		}

		public abstract Date nextDate(Date curr);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Expense")
	@SequenceGenerator(name = "Expense", sequenceName = "SEQ_ID_Expense", allocationSize = 1)
	private Long id;

	@Column
	private BigDecimal amount;

	@Column
	@Enumerated
	private ExpensePeriod period;

	@Column
	private String name;

	@Column
	private Date start;

	@Column(name = "expense_end")
	private Date end;

	@Column
	private Date lastPayment;

	// $P$

	/**
	 * Creates a new Expense object
	 */
	public Expense() {
		// $H$
	}

	/**
	 * Returns the ID of this Expense
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Expense
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Amount of this Expense
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * Sets the Amount of this Expense
	 * 
	 * @param amount
	 *            The Amount of this Expense
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the period
	 */
	public ExpensePeriod getPeriod() {
		return period;
	}

	/**
	 * @param period
	 *            the period to set
	 */
	public void setPeriod(ExpensePeriod period) {
		this.period = period;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Start of this Expense
	 */
	public Date getStart() {
		return this.start;
	}

	/**
	 * Sets the Start of this Expense
	 * 
	 * @param start
	 *            The Start of this Expense
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * @return The End of this Expense
	 */
	public Date getEnd() {
		return this.end;
	}

	/**
	 * Sets the End of this Expense
	 * 
	 * @param end
	 *            The End of this Expense
	 */
	public void setEnd(Date end) {
		this.end = end;
	}

	/**
	 * @return The LastPayment of this Expense
	 */
	public Date getLastPayment() {
		return this.lastPayment;
	}

	/**
	 * Sets the LastPayment of this Expense
	 * 
	 * @param lastPayment
	 *            The LastPayment of this Expense
	 */
	public void setLastPayment(Date lastPayment) {
		this.lastPayment = lastPayment;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return amount.toString() + " for " + name;
	}

	// $GS$

}
