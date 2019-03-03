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
		@Index(name = "IDX_PENALTYPOINT_USER", columnList = "user_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class PenaltyPoint extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PenaltyPoint")
	@SequenceGenerator(name = "PenaltyPoint", sequenceName = "SEQ_ID_PenaltyPoint")
	private Long id;

	@Column
	private Date given;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	// $P$

	/**
	 * Creates a new PenaltyPoint object
	 */
	public PenaltyPoint() {
		// $H$
	}

	/**
	 * Returns the ID of this PenaltyPoint
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this PenaltyPoint
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Given of this PenaltyPoint
	 */
	public Date getGiven() {
		return this.given;
	}

	/**
	 * Sets the Given of this PenaltyPoint
	 *
	 * @param given
	 *            The Given of this PenaltyPoint
	 */
	public void setGiven(Date given) {
		this.given = given;
	}

	/**
	 * @return The User of this PenaltyPoint
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this PenaltyPoint
	 *
	 * @param user
	 *            The User of this PenaltyPoint
	 */
	public void setUser(User user) {
		this.user = user;
	}

	// $GS$
}
