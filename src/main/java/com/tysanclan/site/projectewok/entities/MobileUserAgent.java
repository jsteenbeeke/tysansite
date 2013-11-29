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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class MobileUserAgent implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MobileUserAgent")
	@SequenceGenerator(name = "MobileUserAgent", sequenceName = "SEQ_ID_MobileUserAgent")
	private Long id;

	@Column(nullable = false, length = 255)
	@Index(name = "IDX_MUA_IDENT")
	private String identifier;

	@Column(nullable = true)
	@Index(name = "IDX_MUA_MOBILE")
	private Boolean mobile;

	// $P$

	/**
	 * Creates a new MobileUserAgent object
	 */
	public MobileUserAgent() {
		// $H$
	}

	/**
	 * Returns the ID of this MobileUserAgent
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this MobileUserAgent
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Identifier of this MobileUserAgent
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * Sets the Identifier of this MobileUserAgent
	 * @param identifier The Identifier of this MobileUserAgent
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Boolean getMobile() {
		return mobile;
	}

	public void setMobile(Boolean mobile) {
		this.mobile = mobile;
	}

	// $GS$
}
