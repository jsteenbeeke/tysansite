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
public class PasswordRequest extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PasswordRequest")
	@SequenceGenerator(name = "PasswordRequest", sequenceName = "SEQ_ID_PasswordRequest")
	private Long id;

	@Column
	private String key;

	@Column
	private Date requested;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_PASSWORDREQUEST_USER")
	private User user;

	// $P$

	/**
	 * Creates a new PasswordRequest object
	 */
	public PasswordRequest() {
		// $H$
	}

	/**
	 * Returns the ID of this PasswordRequest
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this PasswordRequest
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Key of this PasswordRequest
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Sets the Key of this PasswordRequest
	 * 
	 * @param key
	 *            The Key of this PasswordRequest
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return The Requested of this PasswordRequest
	 */
	public Date getRequested() {
		return this.requested;
	}

	/**
	 * Sets the Requested of this PasswordRequest
	 * 
	 * @param requested
	 *            The Requested of this PasswordRequest
	 */
	public void setRequested(Date requested) {
		this.requested = requested;
	}

	/**
	 * @return The User of this PasswordRequest
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this PasswordRequest
	 * 
	 * @param user
	 *            The User of this PasswordRequest
	 */
	public void setUser(User user) {
		this.user = user;
	}

	// $GS$
}
