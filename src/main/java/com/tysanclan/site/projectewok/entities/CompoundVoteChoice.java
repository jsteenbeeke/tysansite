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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class CompoundVoteChoice extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CompoundVoteChoice")
	@SequenceGenerator(name = "CompoundVoteChoice", sequenceName = "SEQ_ID_CompoundVoteChoice", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_CompoundVoteChoice_votesFor")
	private User votesFor;

	@Column
	private int score;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_CompoundVoteChoice_compoundVote")
	private CompoundVote compoundVote;

	// $P$

	/**
	 * Creates a new CompoundVoteChoice object
	 */
	public CompoundVoteChoice() {
	}

	/**
	 * Returns the ID of this CompoundVoteChoice
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this CompoundVoteChoice
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the votesFor
	 */
	public User getVotesFor() {
		return votesFor;
	}

	/**
	 * @param votesFor
	 *            the votesFor to set
	 */
	public void setVotesFor(User votesFor) {
		this.votesFor = votesFor;
	}

	/**
	 * @return the compoundVote
	 */
	public CompoundVote getCompoundVote() {
		return compoundVote;
	}

	/**
	 * @param compoundVote
	 *            the compoundVote to set
	 */
	public void setCompoundVote(CompoundVote compoundVote) {
		this.compoundVote = compoundVote;
	}

	/**
	 * @return The Score of this CompoundVoteChoice
	 */
	public int getScore() {
		return this.score;
	}

	/**
	 * Sets the Score of this CompoundVoteChoice
	 * 
	 * @param score
	 *            The Score of this CompoundVoteChoice
	 */
	public void setScore(int score) {
		this.score = score;
	}

	// $GS$

}
