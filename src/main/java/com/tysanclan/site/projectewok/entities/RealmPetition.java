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
		@Index(name = "IDX_REALMPETITION_REQUESTER", columnList = "requester_id"),
		//
		@Index(name = "IDX_REALMPETITION_GAME", columnList = "game_id"), //
		@Index(name = "IDX_REALMPETITION_REALM", columnList = "realm_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class RealmPetition implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RealmPetition")
	@SequenceGenerator(name = "RealmPetition", sequenceName="SEQ_ID_RealmPetition", allocationSize=1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User requester;

	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Realm realm;

	@Column(nullable = true)
	private String name;

	@Column(nullable = false)
	private Date start;

	@ManyToMany(fetch = FetchType.LAZY)
	private List<User> signatures;

	// $P$

	/**
	 * Creates a new RealmPetition object
	 */
	public RealmPetition() {
		this.signatures = new LinkedList<User>();
		// $H$
	}

	/**
	 * Returns the ID of this RealmPetition
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this RealmPetition
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Requester of this RealmPetition
	 */
	public User getRequester() {
		return this.requester;
	}

	/**
	 * Sets the Requester of this RealmPetition
	 *
	 * @param requester
	 *            The Requester of this RealmPetition
	 */
	public void setRequester(User requester) {
		this.requester = requester;
	}

	/**
	 * @return The Game of this RealmPetition
	 */
	public Game getGame() {
		return this.game;
	}

	/**
	 * Sets the Game of this RealmPetition
	 *
	 * @param game
	 *            The Game of this RealmPetition
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * @return The Name of this RealmPetition
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this RealmPetition
	 *
	 * @param name
	 *            The Name of this RealmPetition
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Signatures of this RealmPetition
	 */
	public List<User> getSignatures() {
		return this.signatures;
	}

	/**
	 * Sets the Signatures of this RealmPetition
	 *
	 * @param signatures
	 *            The Signatures of this RealmPetition
	 */
	public void setSignatures(List<User> signatures) {
		this.signatures = signatures;
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
