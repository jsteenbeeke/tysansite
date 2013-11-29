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

import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class UpdateMark implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UpdateMark")
	@SequenceGenerator(name = "UpdateMark", sequenceName = "SEQ_ID_UpdateMark")
	private Long id;

	@Column
	private String className;

	@Column
	private String methodName;

	@Column
	private String version;

	// $P$

	/**
	 * Creates a new UpdateMark object
	 */
	public UpdateMark() {
		// $H$
	}

	/**
	 * Returns the ID of this UpdateMark
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this UpdateMark
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The ClassName of this UpdateMark
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * Sets the ClassName of this UpdateMark
	 * 
	 * @param className
	 *            The ClassName of this UpdateMark
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName
	 *            the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return The Version of this UpdateMark
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * Sets the Version of this UpdateMark
	 * 
	 * @param version
	 *            The Version of this UpdateMark
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	// $GS$
}
