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
		@Index(columnList = "vote_id", name = "IDX_AcceptanceVoteVerdict_vote_index"),
		//
		@Index(columnList = "caster_id", name = "IDX_AcceptanceVoteVerdict_caster_index")
		//
})
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class AcceptanceVoteVerdict extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AcceptanceVoteVerdict")
	@SequenceGenerator(name = "AcceptanceVoteVerdict", sequenceName="SEQ_ID_AcceptanceVoteVerdict", allocationSize=1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private AcceptanceVote vote;

	@Column
	private boolean inFavor;

	@ManyToOne(fetch = FetchType.LAZY)
	private User caster;

	// $P$

	/**
	 * Creates a new AcceptanceVoteVerdict object
	 */
	public AcceptanceVoteVerdict() {
		// $H$
	}

	/**
	 * Returns the ID of this AcceptanceVoteVerdict
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this AcceptanceVoteVerdict
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Vote of this AcceptanceVoteVerdict
	 */
	public AcceptanceVote getVote() {
		return this.vote;
	}

	/**
	 * Sets the Vote of this AcceptanceVoteVerdict
	 *
	 * @param vote
	 *            The Vote of this AcceptanceVoteVerdict
	 */
	public void setVote(AcceptanceVote vote) {
		this.vote = vote;
	}

	/**
	 * @return The InFavor of this AcceptanceVoteVerdict
	 */
	public boolean isInFavor() {
		return this.inFavor;
	}

	/**
	 * Sets the InFavor of this AcceptanceVoteVerdict
	 *
	 * @param inFavor
	 *            The InFavor of this AcceptanceVoteVerdict
	 */
	public void setInFavor(boolean inFavor) {
		this.inFavor = inFavor;
	}

	/**
	 * @return The Caster of this AcceptanceVoteVerdict
	 */
	public User getCaster() {
		return this.caster;
	}

	/**
	 * Sets the Caster of this AcceptanceVoteVerdict
	 *
	 * @param caster
	 *            The Caster of this AcceptanceVoteVerdict
	 */
	public void setCaster(User caster) {
		this.caster = caster;
	}

	// $GS$
}
