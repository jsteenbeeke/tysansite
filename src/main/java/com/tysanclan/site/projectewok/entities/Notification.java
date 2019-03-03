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

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_NOTIFICATION_USER", columnList = "user_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Notification implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Notification")
	@SequenceGenerator(name = "Notification", sequenceName = "SEQ_ID_Notification")
	private Long id;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	// $P$

	/**
	 * Creates a new Notification object
	 */
	public Notification() {
		// $H$
	}

	/**
	 * Returns the ID of this Notification
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Notification
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Message of this Notification
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Sets the Message of this Notification
	 *
	 * @param message
	 *            The Message of this Notification
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return The User of this Notification
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this Notification
	 *
	 * @param user
	 *            The User of this Notification
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
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	// $GS$
}
