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
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_JOINVERDICT_USER", columnList = "user_id"), //
		@Index(name = "IDX_JOINVERDICT_APPLICATION", columnList = "application_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class JoinVerdict extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JoinVerdict")
	@SequenceGenerator(name = "JoinVerdict", sequenceName = "SEQ_ID_JoinVerdict")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column
	private boolean inFavor;

	@ManyToOne(fetch = FetchType.LAZY)
	private JoinApplication application;

	// $P$

	/**
	 * Creates a new JoinVerdict object
	 */
	public JoinVerdict() {
		// $H$
	}

	/**
	 * Returns the ID of this JoinVerdict
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this JoinVerdict
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The User of this JoinVerdict
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this JoinVerdict
	 * 
	 * @param user
	 *            The User of this JoinVerdict
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The InFavor of this JoinVerdict
	 */
	public boolean isInFavor() {
		return this.inFavor;
	}

	/**
	 * Sets the InFavor of this JoinVerdict
	 * 
	 * @param inFavor
	 *            The InFavor of this JoinVerdict
	 */
	public void setInFavor(boolean inFavor) {
		this.inFavor = inFavor;
	}

	/**
	 * @return The Application of this JoinVerdict
	 */
	public JoinApplication getApplication() {
		return this.application;
	}

	/**
	 * Sets the Application of this JoinVerdict
	 * 
	 * @param application
	 *            The Application of this JoinVerdict
	 */
	public void setApplication(JoinApplication application) {
		this.application = application;
	}

	// $GS$
}
