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

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public abstract class Election extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	/**
	 * Interface for determining whether or not a candidate is eligible for a
	 * given position
	 * 
	 * @author Jeroen Steenbeeke
	 */
	public static interface Eligibility {
		/**
		 * Determines if the given user is eligible for the position
		 * 
		 * @param user
		 *            The user to check
		 * @return {@code true} if the user is eligible for the position,
		 *         {@code false} otherwise
		 */
		boolean isEligible(User user);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Election")
	@SequenceGenerator(name = "Election", sequenceName = "SEQ_ID_Election", allocationSize = 1)
	private Long id;

	@Column
	private Date start;

	@OneToMany(mappedBy = "election", fetch = FetchType.LAZY)
	private Set<CompoundVote> votes;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Election_Candidates", joinColumns = @JoinColumn(name = "candidate_id"), inverseJoinColumns = @JoinColumn(name = "election_id"))
	private Set<User> candidates;

	// $P$

	/**
	 * Creates a new Election object
	 */
	public Election() {
		this.votes = new HashSet<CompoundVote>();
		this.candidates = new HashSet<User>();
		// $H$
	}

	/**
	 * Returns the ID of this Election
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Election
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Start of this Election
	 */
	public Date getStart() {
		return this.start;
	}

	/**
	 * Sets the Start of this Election
	 * 
	 * @param start
	 *            The Start of this Election
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * @return The Votes of this Election
	 */
	public Set<CompoundVote> getVotes() {
		return this.votes;
	}

	/**
	 * Sets the Votes of this Election
	 * 
	 * @param votes
	 *            The Votes of this Election
	 */
	public void setVotes(Set<CompoundVote> votes) {
		this.votes = votes;
	}

	/**
	 * @return The Candidates of this Election
	 */
	public Set<User> getCandidates() {
		return this.candidates;
	}

	/**
	 * Sets the Candidates of this Election
	 * 
	 * @param candidates
	 *            The Candidates of this Election
	 */
	public void setCandidates(Set<User> candidates) {
		this.candidates = candidates;
	}

	public boolean isNominationOpen() {
		Calendar earliestStart = DateUtil.getCalendarInstance();
		earliestStart.add(Calendar.WEEK_OF_YEAR, -1);
		Date earlyStart = earliestStart.getTime();

		if (earlyStart.before(getStart())) {
			return true;
		}

		return false;
	}

	public abstract Eligibility getEligibility();

	// $GS$
}
