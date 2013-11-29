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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.tysanclan.site.projectewok.components.accountpanel.Diablo2Panel;
import com.tysanclan.site.projectewok.components.accountpanel.LeagueOfLegendsPanel;
import com.tysanclan.site.projectewok.components.accountpanel.MineCraftPanel;
import com.tysanclan.site.projectewok.components.accountpanel.RealIdPanel;
import com.tysanclan.site.projectewok.components.accountpanel.StarCraft2Panel;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public abstract class GameAccount implements DomainObject {
	public static final long serialVersionUID = 1L;

	public static enum AccountType {
		REALID {
			@Override
			public String toString() {
				return "RealID Account";
			}

			@Override
			public Panel createAddForm(String id, UserGameRealm object) {

				return new RealIdPanel(id, object);
			}
		},
		STARCRAFT2 {
			@Override
			public String toString() {
				return "StarCraft 2 Character Account";
			}

			@Override
			public Panel createAddForm(String id, UserGameRealm object) {
				return new StarCraft2Panel(id, object);
			}
		},
		DIABLO2 {
			@Override
			public String toString() {
				return "Diablo 2 Account";
			}

			@Override
			public Panel createAddForm(String id, UserGameRealm object) {
				return new Diablo2Panel(id, object);
			}
		},
		MINECRAFT {
			@Override
			public String toString() {
				return "MineCraft Account";
			}

			@Override
			public Panel createAddForm(String id, UserGameRealm object) {
				return new MineCraftPanel(id, object);
			}
		},
		LEAGUE_OF_LEGENDS {
			@Override
			public String toString() {
				return "League of Legends account";
			}

			@Override
			public Panel createAddForm(String id, UserGameRealm object) {
				return new LeagueOfLegendsPanel(id, object);
			}
		};

		public abstract Panel createAddForm(String id, UserGameRealm object);

	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GameAccount")
	@SequenceGenerator(name = "GameAccount", sequenceName = "SEQ_ID_GameAccount")
	private Long id;

	@Column
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_GAMEACCOUNT_UGR")
	private UserGameRealm userGameRealm;

	// $P$

	/**
	 * Creates a new GameAccount object
	 */
	public GameAccount() {
		// $H$
	}

	/**
	 * Returns the ID of this GameAccount
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this GameAccount
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Name of this GameAccount
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the Name of this GameAccount
	 * 
	 * @param name
	 *            The Name of this GameAccount
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The UserGameRealm of this GameAccount
	 */
	public UserGameRealm getUserGameRealm() {
		return this.userGameRealm;
	}

	/**
	 * Sets the UserGameRealm of this GameAccount
	 * 
	 * @param userGameRealm
	 *            The UserGameRealm of this GameAccount
	 */
	public void setUserGameRealm(UserGameRealm userGameRealm) {
		this.userGameRealm = userGameRealm;
	}

	protected abstract String render();

	public abstract boolean isValid();

	public abstract AccountType getType();

	@Override
	public final String toString() {
		return render();
	}

	// $GS$
}
