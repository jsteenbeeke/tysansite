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

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(name = "TROLE", indexes = { //
		@Index(name = "IDX_ROLE_ASSIGNEDTO", columnList = "assignedTo_id") //
})
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Role extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	public static enum RoleType {
		NORMAL {
			@Override
			public boolean isReassignable() {
				return true;
			}
		}, STEWARD, TREASURER, HERALD;

		public boolean isReassignable() {

			return false;
		}

		@Override
		public String toString() {

			return name().charAt(0) + name().substring(1).toLowerCase();
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RoleSeq")
	@SequenceGenerator(name = "RoleSeq", sequenceName = "SEQ_ID_Role", allocationSize = 1)
	private Long id;

	@Column
	private String name;

	@Column
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	private User assignedTo;

	@Column(nullable = false, columnDefinition = "varchar(255) default 'NORMAL' not null")
	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	// $P$

	public Role() {
		// $H$
	}

	/**
	 * @return the id
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the assignedTo
	 */
	public User getAssignedTo() {
		return assignedTo;
	}

	/**
	 * @param assignedTo
	 *            the assignedTo to set
	 */
	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	/**
	 * @return the reassignable
	 */
	@Transient
	public boolean isReassignable() {
		return getRoleType().isReassignable();
	}

	// $GS$

}
