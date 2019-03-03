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
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_REALM_OVERSEER", columnList = "overseer_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Realm implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Realm")
	@SequenceGenerator(name = "Realm", sequenceName="SEQ_ID_Realm", allocationSize=1)
	private Long id;

	@Column
	private String name;

	@Column
	private String channel;

	@ManyToOne(fetch = FetchType.LAZY)
	private User overseer;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "REALM_GAME", joinColumns = @JoinColumn(name = "realm_id"), inverseJoinColumns = @JoinColumn(name = "game_id"))
	@OrderBy("name")
	private List<Game> games;

	@OneToMany(mappedBy = "realm")
	@OrderBy("name")
	private List<GamingGroup> groups;

	@OneToMany(mappedBy = "realm")
	@OrderBy("channelName")
	private List<BattleNetChannel> channels;

	// $P$

	/**
	 * Creates a new Realm object
	 */
	public Realm() {
		this.games = new LinkedList<Game>();
		this.groups = new LinkedList<GamingGroup>();
		// $H$
	}

	/**
	 * Returns the ID of this Realm
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Realm
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Name of this Realm
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this Realm
	 *
	 * @param name
	 *            The Name of this Realm
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The Channel of this Realm
	 */
	public String getChannel() {
		return this.channel;
	}

	/**
	 * Sets the Channel of this Realm
	 *
	 * @param channel
	 *            The Channel of this Realm
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * @return The Overseer of this Realm
	 */
	public User getOverseer() {
		return this.overseer;
	}

	/**
	 * Sets the Overseer of this Realm
	 *
	 * @param overseer
	 *            The Overseer of this Realm
	 */
	public void setOverseer(User overseer) {
		this.overseer = overseer;
	}

	/**
	 * @return The Games of this Realm
	 */
	public List<Game> getGames() {
		return this.games;
	}

	/**
	 * Sets the Games of this Realm
	 *
	 * @param games
	 *            The Games of this Realm
	 */
	public void setGames(List<Game> games) {
		this.games = games;
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
	 * @return the channels
	 */
	public List<BattleNetChannel> getChannels() {
		return channels;
	}

	/**
	 * @param channels
	 *            the channels to set
	 */
	public void setChannels(List<BattleNetChannel> channels) {
		this.channels = channels;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	// $GS$
}
