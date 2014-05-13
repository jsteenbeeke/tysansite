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
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
@Index(name = "IDX_CompoundVote_Caster", columnList = "caster_id"), //
		@Index(name = "IDX_CompoundVote_Election", columnList = "election_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class CompoundVote extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CompoundVote")
	@SequenceGenerator(name = "CompoundVote", sequenceName = "SEQ_ID_CompoundVote", allocationSize = 1)
	private Long id;

	@OneToMany(mappedBy = "compoundVote", fetch = FetchType.LAZY)
	private Set<CompoundVoteChoice> choices;

	@ManyToOne(fetch = FetchType.LAZY)
	private User caster;

	@ManyToOne(fetch = FetchType.LAZY)
	private Election election;

	// $P$

	/**
	 * Creates a new CompoundVote object
	 */
	public CompoundVote() {
		// $H$
	}

	/**
	 * Returns the ID of this CompoundVote
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this CompoundVote
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Choices of this CompoundVote
	 */
	public Set<CompoundVoteChoice> getChoices() {
		return this.choices;
	}

	/**
	 * Sets the Choices of this CompoundVote
	 * 
	 * @param choices
	 *            The Choices of this CompoundVote
	 */
	public void setChoices(Set<CompoundVoteChoice> choices) {
		this.choices = choices;
	}

	/**
	 * @return The Caster of this CompoundVote
	 */
	public User getCaster() {
		return this.caster;
	}

	/**
	 * Sets the Caster of this CompoundVote
	 * 
	 * @param caster
	 *            The Caster of this CompoundVote
	 */
	public void setCaster(User caster) {
		this.caster = caster;
	}

	/**
	 * @return the election
	 */
	public Election getElection() {
		return election;
	}

	/**
	 * @param election
	 *            the election to set
	 */
	public void setElection(Election election) {
		this.election = election;
	}

	// $GS$

}
