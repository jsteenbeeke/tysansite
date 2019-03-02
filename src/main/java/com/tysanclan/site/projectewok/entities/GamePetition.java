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
import com.tysanclan.site.projectewok.util.DateUtil;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_GAMEPETITION_REQUESTER", columnList = "requester_id"),
		//
		@Index(name = "IDX_GAMEPETITION_REALM", columnList = "realm_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class GamePetition extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GamePetition")
	@SequenceGenerator(name = "GamePetition", sequenceName = "SEQ_ID_GamePetition")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User requester;

	@ManyToMany(fetch = FetchType.LAZY)
	@OrderBy("username")
	private List<User> signatures;

	@Column
	private String name;

	@Column
	private byte[] image;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Realm realm;

	@Column(nullable = true)
	private String newRealmName;

	@Column(nullable = false)
	private Date start;

	// $P$

	/**
	 * Creates a new GamePetition object
	 */
	public GamePetition() {
		this.signatures = new LinkedList<User>();
		this.start = new Date();
		// $H$
	}

	/**
	 * Returns the ID of this GamePetition
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this GamePetition
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Requester of this GamePetition
	 */
	public User getRequester() {
		return this.requester;
	}

	/**
	 * Sets the Requester of this GamePetition
	 *
	 * @param requester
	 *            The Requester of this GamePetition
	 */
	public void setRequester(User requester) {
		this.requester = requester;
	}

	/**
	 * @return The Signatures of this GamePetition
	 */
	public List<User> getSignatures() {
		return this.signatures;
	}

	/**
	 * Sets the Signatures of this GamePetition
	 *
	 * @param signatures
	 *            The Signatures of this GamePetition
	 */
	public void setSignatures(List<User> signatures) {
		this.signatures = signatures;
	}

	/**
	 * @return The Name of this GamePetition
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this GamePetition
	 *
	 * @param name
	 *            The Name of this GamePetition
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Image of this GamePetition
	 */
	public byte[] getImage() {
		return this.image;
	}

	/**
	 * Sets the Image of this GamePetition
	 *
	 * @param image
	 *            The Image of this GamePetition
	 */
	public void setImage(byte[] image) {
		this.image = image;
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

	/**
	 * @return the newRealmName
	 */
	public String getNewRealmName() {
		return newRealmName;
	}

	/**
	 * @param newRealmName
	 *            the newRealmName to set
	 */
	public void setNewRealmName(String newRealmName) {
		this.newRealmName = newRealmName;
	}

	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	@Transient
	public Date getExpires() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.setTime(getStart());
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		return cal.getTime();
	}

	// $GS$
}
