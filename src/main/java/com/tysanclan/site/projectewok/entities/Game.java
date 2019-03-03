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
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_GAME_COORDINATOR", columnList = "coordinator_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Game extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Game")
	@SequenceGenerator(name = "Game", sequenceName = "SEQ_ID_Game", allocationSize = 1)
	private Long id;

	@Column
	private String name;

	@Column(nullable = false)
	private boolean active;

	@Column
	@Lob
	private byte[] image;

	@ManyToOne(fetch = FetchType.LAZY)
	private User coordinator;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "REALM_GAME", inverseJoinColumns = @JoinColumn(name = "realm_id"), joinColumns = @JoinColumn(name = "game_id"))
	@OrderBy("name")
	private List<Realm> realms;

	@OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
	@OrderBy("name")
	private List<GamingGroup> groups;

	@OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
	private List<UserGameRealm> players;

	// $P$

	/**
	 *
	 */
	public Game() {
		this.realms = new LinkedList<Realm>();
		// $H$
	}

	/**
	 * @return the id
	 */
	@Override
	public final Serializable getDomainObjectId() {
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
	 * @return the image
	 */
	public byte[] getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(byte[] image) {
		this.image = image;
	}

	/**
	 * @return the coordinator
	 */
	public User getCoordinator() {
		return coordinator;
	}

	/**
	 * @param coordinator
	 *            the coordinator to set
	 */
	public void setCoordinator(User coordinator) {
		this.coordinator = coordinator;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return The Realms of this Game
	 */
	public List<Realm> getRealms() {
		return this.realms;
	}

	/**
	 * Sets the Realms of this Game
	 *
	 * @param realms
	 *            The Realms of this Game
	 */
	public void setRealms(List<Realm> realms) {
		this.realms = realms;
	}

	/**
	 * @return the groups
	 */
	public List<GamingGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(List<GamingGroup> groups) {
		this.groups = groups;
	}

	/**
	 * @return the players
	 */
	public List<UserGameRealm> getPlayers() {
		return players;
	}

	/**
	 * @param players
	 *            the players to set
	 */
	public void setPlayers(List<UserGameRealm> players) {
		this.players = players;
	}

	// $GS$

}
