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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

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
public class AcceptanceVote extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AcceptanceVote")
	@SequenceGenerator(name = "AcceptanceVote", sequenceName = "SEQ_ID_AcceptanceVote")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_AcceptanceVote_trialMember_index", columnNames = "trialMember")
	private User trialMember;

	@Column
	private Date start;

	@OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Set<AcceptanceVoteVerdict> verdicts;

	// $P$

	/**
	 * Creates a new AcceptanceVote object
	 */
	public AcceptanceVote() {
		this.verdicts = new HashSet<AcceptanceVoteVerdict>();
		// $H$
	}

	/**
	 * Returns the ID of this AcceptanceVote
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this AcceptanceVote
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The TrialMember of this AcceptanceVote
	 */
	public User getTrialMember() {
		return this.trialMember;
	}

	/**
	 * Sets the TrialMember of this AcceptanceVote
	 * 
	 * @param trialMember
	 *            The TrialMember of this AcceptanceVote
	 */
	public void setTrialMember(User trialMember) {
		this.trialMember = trialMember;
	}

	/**
	 * @return The Verdicts of this AcceptanceVote
	 */
	public Set<AcceptanceVoteVerdict> getVerdicts() {
		return this.verdicts;
	}

	/**
	 * Sets the Verdicts of this AcceptanceVote
	 * 
	 * @param verdicts
	 *            The Verdicts of this AcceptanceVote
	 */
	public void setVerdicts(Set<AcceptanceVoteVerdict> verdicts) {
		this.verdicts = verdicts;
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

	// $GS$
}
