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
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "log")
public class LogItem extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LogItem")
	@SequenceGenerator(name = "LogItem", sequenceName = "SEQ_ID_LogItem", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_LOGITEM_USER")
	private User user;

	@Column(nullable = false)
	private long logTime;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private String category;

	// $P$

	/**
	 * Creates a new LogItem object
	 */
	public LogItem() {
		// $H$
	}

	/**
	 * Returns the ID of this LogItem
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this LogItem
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The User of this LogItem
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this LogItem
	 * 
	 * @param user
	 *            The User of this LogItem
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public String getVisibleUsername() {
		return this.user != null ? user.getUsername() : "System";
	}

	/**
	 * @return The LogTime of this LogItem
	 */
	public long getLogTime() {
		return this.logTime;
	}

	/**
	 * Sets the LogTime of this LogItem
	 * 
	 * @param logTime
	 *            The LogTime of this LogItem
	 */
	public void setLogTime(long logTime) {
		this.logTime = logTime;
	}

	/**
	 * @return The Message of this LogItem
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Sets the Message of this LogItem
	 * 
	 * @param message
	 *            The Message of this LogItem
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return The Category of this LogItem
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * Sets the Category of this LogItem
	 * 
	 * @param category
	 *            The Category of this LogItem
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	// $GS$
}
