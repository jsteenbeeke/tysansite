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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class GroupCreationRequest extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupCreationRequest")
	@SequenceGenerator(name = "GroupCreationRequest", sequenceName = "SEQ_ID_GroupCreationRequest")
	private Long id;

	@Column
	private String name;

	@Column
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String motivation;

	@Column
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_GROUPCREATIONREQUEST_REQUESTER")
	private User requester;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_GROUPCREATIONREQUEST_GAME")
	private Game game;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_GROUPCREATIONREQUEST_REALM")
	private Realm realm;

	// $P$

	/**
	 * Creates a new GroupCreationRequest object
	 */
	public GroupCreationRequest() {
		// $H$
	}

	/**
	 * Returns the ID of this GroupCreationRequest
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this GroupCreationRequest
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Name of this GroupCreationRequest
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this GroupCreationRequest
	 * 
	 * @param name
	 *            The Name of this GroupCreationRequest
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Motivation of this GroupCreationRequest
	 */
	public String getMotivation() {
		return this.motivation;
	}

	/**
	 * Sets the Motivation of this GroupCreationRequest
	 * 
	 * @param motivation
	 *            The Motivation of this GroupCreationRequest
	 */
	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	/**
	 * @return The Description of this GroupCreationRequest
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the Description of this GroupCreationRequest
	 * 
	 * @param description
	 *            The Description of this GroupCreationRequest
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return The Requester of this GroupCreationRequest
	 */
	public User getRequester() {
		return this.requester;
	}

	/**
	 * Sets the Requester of this GroupCreationRequest
	 * 
	 * @param requester
	 *            The Requester of this GroupCreationRequest
	 */
	public void setRequester(User requester) {
		this.requester = requester;
	}

	/**
	 * @return The Game of this GroupCreationRequest
	 */
	public Game getGame() {
		return this.game;
	}

	/**
	 * Sets the Game of this GroupCreationRequest
	 * 
	 * @param game
	 *            The Game of this GroupCreationRequest
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * @return the realm
	 */
	public Realm getRealm() {
		return realm;
	}

	/**
	 * @param realm
	 *            the realm to set
	 */
	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	// $GS$
}
