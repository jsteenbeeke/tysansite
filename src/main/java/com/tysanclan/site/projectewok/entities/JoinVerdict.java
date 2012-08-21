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

import javax.persistence.*;

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
public class JoinVerdict extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JoinVerdict")
	@SequenceGenerator(name = "JoinVerdict", sequenceName = "SEQ_ID_JoinVerdict")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_JOINVERDICT_USER")
	private User user;

	@Column
	private boolean inFavor;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_JOINVERDICT_APPLICATION")
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
