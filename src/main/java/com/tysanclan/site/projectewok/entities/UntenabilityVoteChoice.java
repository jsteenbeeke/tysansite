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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class UntenabilityVoteChoice extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UntenabilityVoteChoice")
	@SequenceGenerator(name = "UntenabilityVoteChoice", sequenceName = "SEQ_ID_UntenabilityVoteChoice")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Index(name = "IDX_UntenabilityVoteChoice_Vote")
	private UntenabilityVote vote;

	@Column
	private boolean inFavor;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_UntenabilityVote_Caster")
	private User caster;

	// $P$

	/**
	 * Creates a new UntenabilityVoteChoice object
	 */
	public UntenabilityVoteChoice() {
		// $H$
	}

	/**
	 * Returns the ID of this UntenabilityVoteChoice
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this UntenabilityVoteChoice
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Vote of this UntenabilityVoteChoice
	 */
	public UntenabilityVote getVote() {
		return this.vote;
	}

	/**
	 * Sets the Vote of this UntenabilityVoteChoice
	 * 
	 * @param vote
	 *            The Vote of this UntenabilityVoteChoice
	 */
	public void setVote(UntenabilityVote vote) {
		this.vote = vote;
	}

	/**
	 * @return The InFavor of this UntenabilityVoteChoice
	 */
	public boolean isInFavor() {
		return this.inFavor;
	}

	/**
	 * Sets the InFavor of this UntenabilityVoteChoice
	 * 
	 * @param inFavor
	 *            The InFavor of this UntenabilityVoteChoice
	 */
	public void setInFavor(boolean inFavor) {
		this.inFavor = inFavor;
	}

	/**
	 * @return The Caster of this UntenabilityVoteChoice
	 */
	public User getCaster() {
		return this.caster;
	}

	/**
	 * Sets the Caster of this UntenabilityVoteChoice
	 * 
	 * @param caster
	 *            The Caster of this UntenabilityVoteChoice
	 */
	public void setCaster(User caster) {
		this.caster = caster;
	}

	// $GS$
}
