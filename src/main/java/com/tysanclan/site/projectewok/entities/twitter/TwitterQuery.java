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
public class TwitterQuery implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TwitterQuery")
	@SequenceGenerator(name = "TwitterQuery", sequenceName = "SEQ_ID_TwitterQuery")
	private Long id;

	@Column
	private String queryString;

	// $P$

	/**
	 * Creates a new TwitterQuery object
	 */
	public TwitterQuery() {
		// $H$
	}

	/**
	 * Returns the ID of this TwitterQuery
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this TwitterQuery
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The QueryString of this TwitterQuery
	 */
	public String getQueryString() {
		return this.queryString;
	}

	/**
	 * Sets the QueryString of this TwitterQuery
	 * @param queryString The QueryString of this TwitterQuery
	 */
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	// $GS$
}
