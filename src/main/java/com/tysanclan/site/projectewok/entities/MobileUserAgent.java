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

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_MUA_IDENT", columnList = "identifier"), //
		@Index(name = "IDX_MUA_MOBILE", columnList = "mobile") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class MobileUserAgent implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MobileUserAgent")
	@SequenceGenerator(name = "MobileUserAgent", sequenceName="SEQ_ID_MobileUserAgent", allocationSize=1)
	private Long id;

	@Column(nullable = false, length = 255)
	private String identifier;

	@Column(nullable = true)
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
