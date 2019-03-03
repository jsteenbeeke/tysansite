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
 * @author Jeroen Steenbeeke
 */
@Entity
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class UpdateMark implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UpdateMark")
	@SequenceGenerator(name = "UpdateMark", sequenceName="SEQ_ID_UpdateMark", allocationSize=1)
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
