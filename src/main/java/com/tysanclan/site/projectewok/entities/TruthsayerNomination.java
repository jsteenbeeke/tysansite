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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Index(name = "IDX_TruthsayerNomination_User", columnList = "user_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class TruthsayerNomination extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TruthsayerNomination")
	@SequenceGenerator(name = "TruthsayerNomination", sequenceName = "SEQ_ID_TruthsayerNomination")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(nullable = true)
	private Date voteStart;

	@OneToMany(mappedBy = "nomination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<TruthsayerNominationVote> votes;

	// $P$

	/**
	 * Creates a new TruthsayerNomination object
	 */
	public TruthsayerNomination() {
		this.votes = new HashSet<TruthsayerNominationVote>();
		// $H$
	}

	/**
	 * Returns the ID of this TruthsayerNomination
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this TruthsayerNomination
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The User of this TruthsayerNomination
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this TruthsayerNomination
	 * 
	 * @param user
	 *            The User of this TruthsayerNomination
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The VoteStart of this TruthsayerNomination
	 */
	public Date getVoteStart() {
		return this.voteStart;
	}

	/**
	 * Sets the VoteStart of this TruthsayerNomination
	 * 
	 * @param voteStart
	 *            The VoteStart of this TruthsayerNomination
	 */
	public void setVoteStart(Date voteStart) {
		this.voteStart = voteStart;
	}

	/**
	 * @return The Votes of this TruthsayerNomination
	 */
	public Set<TruthsayerNominationVote> getVotes() {
		return this.votes;
	}

	/**
	 * Sets the Votes of this TruthsayerNomination
	 * 
	 * @param votes
	 *            The Votes of this TruthsayerNomination
	 */
	public void setVotes(Set<TruthsayerNominationVote> votes) {
		this.votes = votes;
	}

	// $GS$
}
