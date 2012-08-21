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

import java.util.Date;

import javax.persistence.Column;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage=org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region="main")
public class TwitterSearchResult implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TwitterSearchResult")
	@SequenceGenerator(name = "TwitterSearchResult", sequenceName = "SEQ_ID_TwitterSearchResult")
	private Long id;

	@Column
	private Date time;

	@Column
	private String userId;

	@Column
	private String message;

	@Column
	private Double latitude;

	@Column
	private Double longitude;

	// $P$

	/**
	 * Creates a new TwitterSearchResult object
	 */
	public TwitterSearchResult() {
		// $H$
	}

	/**
	 * Returns the ID of this TwitterSearchResult
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this TwitterSearchResult
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Time of this TwitterSearchResult
	 */
	public Date getTime() {
		return this.time;
	}

	/**
	 * Sets the Time of this TwitterSearchResult
	 * @param time The Time of this TwitterSearchResult
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return The UserId of this TwitterSearchResult
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * Sets the UserId of this TwitterSearchResult
	 * @param userId The UserId of this TwitterSearchResult
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return The Message of this TwitterSearchResult
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Sets the Message of this TwitterSearchResult
	 * @param message The Message of this TwitterSearchResult
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return The Latitude of this TwitterSearchResult
	 */
	public Double getLatitude() {
		return this.latitude;
	}

	/**
	 * Sets the Latitude of this TwitterSearchResult
	 * @param latitude The Latitude of this TwitterSearchResult
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return The Longitude of this TwitterSearchResult
	 */
	public Double getLongitude() {
		return this.longitude;
	}

	/**
	 * Sets the Longitude of this TwitterSearchResult
	 * @param longitude The Longitude of this TwitterSearchResult
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	// $GS$
}
