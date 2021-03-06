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

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_EmailChangeConfirmation_USER", columnList = "user_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class EmailChangeConfirmation extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EmailChangeConfirmation")
	@SequenceGenerator(name = "EmailChangeConfirmation", sequenceName="SEQ_ID_EmailChangeConfirmation", allocationSize=1)
	private Long id;

	@Column
	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column
	private Date initialized;

	@Column
	private String activationKey;

	// $P$

	/**
	 * Creates a new EmailChangeConfirmation object
	 */
	public EmailChangeConfirmation() {
		// $H$
	}

	/**
	 * Returns the ID of this EmailChangeConfirmation
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this EmailChangeConfirmation
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Email of this EmailChangeConfirmation
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the Email of this EmailChangeConfirmation
	 *
	 * @param email
	 *            The Email of this EmailChangeConfirmation
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return The User of this EmailChangeConfirmation
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this EmailChangeConfirmation
	 *
	 * @param user
	 *            The User of this EmailChangeConfirmation
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The Initialized of this EmailChangeConfirmation
	 */
	public Date getInitialized() {
		return this.initialized;
	}

	/**
	 * Sets the Initialized of this EmailChangeConfirmation
	 *
	 * @param initialized
	 *            The Initialized of this EmailChangeConfirmation
	 */
	public void setInitialized(Date initialized) {
		this.initialized = initialized;
	}

	/**
	 * @return The ActivationKey of this EmailChangeConfirmation
	 */
	public String getActivationKey() {
		return this.activationKey;
	}

	/**
	 * Sets the ActivationKey of this EmailChangeConfirmation
	 *
	 * @param activationKey
	 *            The ActivationKey of this EmailChangeConfirmation
	 */
	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	// $GS$
}
