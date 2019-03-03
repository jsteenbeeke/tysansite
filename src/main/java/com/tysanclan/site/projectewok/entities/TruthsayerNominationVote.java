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
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_TruthsayerNominationVote_Senator", columnList = "senator_id"),
		@Index(name = "IDX_TruthsayerNominationVote_nomination", columnList = "nomination_id") })
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class TruthsayerNominationVote extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TruthsayerNominationVote")
	@SequenceGenerator(name = "TruthsayerNominationVote", sequenceName="SEQ_ID_TruthsayerNominationVote", allocationSize=1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User senator;

	@Column
	private boolean verdict;

	@ManyToOne(fetch = FetchType.LAZY)
	private TruthsayerNomination nomination;

	// $P$

	/**
	 * Creates a new TruthsayerNominationVote object
	 */
	public TruthsayerNominationVote() {
		// $H$
	}

	/**
	 * Returns the ID of this TruthsayerNominationVote
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this TruthsayerNominationVote
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Senator of this TruthsayerNominationVote
	 */
	public User getSenator() {
		return this.senator;
	}

	/**
	 * Sets the Senator of this TruthsayerNominationVote
	 *
	 * @param senator
	 *            The Senator of this TruthsayerNominationVote
	 */
	public void setSenator(User senator) {
		this.senator = senator;
	}

	/**
	 * @return The Verdict of this TruthsayerNominationVote
	 */
	public boolean getVerdict() {
		return this.verdict;
	}

	/**
	 * Sets the Verdict of this TruthsayerNominationVote
	 *
	 * @param verdict
	 *            The Verdict of this TruthsayerNominationVote
	 */
	public void setVerdict(boolean verdict) {
		this.verdict = verdict;
	}

	/**
	 * @return The Nomination of this TruthsayerNominationVote
	 */
	public TruthsayerNomination getNomination() {
		return this.nomination;
	}

	/**
	 * Sets the Nomination of this TruthsayerNominationVote
	 *
	 * @param nomination
	 *            The Nomination of this TruthsayerNominationVote
	 */
	public void setNomination(TruthsayerNomination nomination) {
		this.nomination = nomination;
	}

	// $GS$
}
