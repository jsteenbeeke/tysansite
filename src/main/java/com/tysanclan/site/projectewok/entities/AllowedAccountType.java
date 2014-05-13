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

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { @Index(columnList = "game_id", name = "IDX_ALLWDACC_GAME"),
		@Index(columnList = "realm_id", name = "IDX_ALLWDACC_REALM") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class AllowedAccountType extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AllowedAccountType")
	@SequenceGenerator(name = "AllowedAccountType", sequenceName = "SEQ_ID_AllowedAccountType")
	private Long id;

	@Enumerated
	private AccountType type;

	@ManyToOne
	private Game game;

	@ManyToOne
	private Realm realm;

	// $P$

	/**
	 * Creates a new AllowedAccountType object
	 */
	public AllowedAccountType() {
		// $H$
	}

	/**
	 * Returns the ID of this AllowedAccountType
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this AllowedAccountType
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public Game getGame() {
		return game;
	}

	public Realm getRealm() {
		return realm;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	// $GS$
}
