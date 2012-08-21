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
package com.tysanclan.site.projectewok.entities.twitter;

import javax.persistence.*;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class TwitterFollowing implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TwitterFollowing")
	@SequenceGenerator(name = "TwitterFollowing", sequenceName = "SEQ_ID_TwitterFollowing")
	private Long id;

	@Column(unique = true)
	private long userId;

	@Column
	private String username;

	// $P$

	/**
	 * Creates a new TwitterFollowing object
	 */
	public TwitterFollowing() {
		// $H$
	}

	/**
	 * Returns the ID of this TwitterFollowing
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this TwitterFollowing
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The UserId of this TwitterFollowing
	 */
	public long getUserId() {
		return this.userId;
	}

	/**
	 * Sets the UserId of this TwitterFollowing
	 * 
	 * @param userId
	 *            The UserId of this TwitterFollowing
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	// $GS$
}
