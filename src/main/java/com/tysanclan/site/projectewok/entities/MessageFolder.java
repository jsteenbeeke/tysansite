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
@Table(indexes = { //
		@Index(name = "IDX_MESSAGEFOLDER_OWNER", columnList = "owner_id"),
		@Index(name = "IDX_MESSAGEFOLDER_PARENT", columnList = "parent_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class MessageFolder extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MessageFolder")
	@SequenceGenerator(name = "MessageFolder", sequenceName = "SEQ_ID_MessageFolder", allocationSize = 1)
	private Long id;

	@Column
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private User owner;

	@ManyToOne(fetch = FetchType.LAZY)
	private MessageFolder parent;

	// $P$

	/**
	 * Creates a new MessageFolder object
	 */
	public MessageFolder() {
		// $H$
	}

	/**
	 * Returns the ID of this MessageFolder
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this MessageFolder
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Name of this MessageFolder
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this MessageFolder
	 *
	 * @param name
	 *            The Name of this MessageFolder
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Owner of this MessageFolder
	 */
	public User getOwner() {
		return this.owner;
	}

	/**
	 * Sets the Owner of this MessageFolder
	 *
	 * @param owner
	 *            The Owner of this MessageFolder
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @return The Parent of this MessageFolder
	 */
	public MessageFolder getParent() {
		return this.parent;
	}

	/**
	 * Sets the Parent of this MessageFolder
	 *
	 * @param parent
	 *            The Parent of this MessageFolder
	 */
	public void setParent(MessageFolder parent) {
		this.parent = parent;
	}
}
