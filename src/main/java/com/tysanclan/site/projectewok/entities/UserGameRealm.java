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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "user_id",
		"game_id", "realm_id" }), indexes = { //
		@Index(name = "IDX_USERGAMEREALM_USER", columnList = "user_id"),
		@Index(name = "IDX_USERGAMEREALM_GAME", columnList = "game_id"),
		@Index(name = "IDX_USERGAMEREALM_REALM", columnList = "realm_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class UserGameRealm implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserGameRealm")
	@SequenceGenerator(name = "UserGameRealm", sequenceName = "SEQ_ID_UserGameRealm")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private Game game;

	@ManyToOne(fetch = FetchType.LAZY)
	private Realm realm;

	@OneToMany(mappedBy = "userGameRealm", fetch = FetchType.LAZY)
	@OrderBy("name")
	private List<GameAccount> accounts;

	// $P$

	/**
	 * Creates a new UserGameRealm object
	 */
	public UserGameRealm() {
		this.accounts = new LinkedList<GameAccount>();
		// $H$
	}

	/**
	 * Returns the ID of this UserGameRealm
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this UserGameRealm
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The User of this UserGameRealm
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this UserGameRealm
	 *
	 * @param user
	 *            The User of this UserGameRealm
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The Game of this UserGameRealm
	 */
	public Game getGame() {
		return this.game;
	}

	/**
	 * Sets the Game of this UserGameRealm
	 *
	 * @param game
	 *            The Game of this UserGameRealm
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * @return The Realm of this UserGameRealm
	 */
	public Realm getRealm() {
		return this.realm;
	}

	/**
	 * Sets the Realm of this UserGameRealm
	 *
	 * @param realm
	 *            The Realm of this UserGameRealm
	 */
	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	/**
	 * @return the accounts
	 */
	public List<GameAccount> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts
	 *            the accounts to set
	 */
	public void setAccounts(List<GameAccount> accounts) {
		this.accounts = accounts;
	}

	// $GS$
}
