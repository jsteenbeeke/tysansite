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

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class AchievementProposalVote implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AchievementProposalVote")
	@SequenceGenerator(name = "AchievementProposalVote", sequenceName="SEQ_ID_AchievementProposalVote", allocationSize=1)
	private Long id;

	@Column
	private boolean inFavor;

	@ManyToOne
	private User senator;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	private AchievementProposal proposal;

	// $P$

	/**
	 * Creates a new AchievementProposalVote object
	 */
	public AchievementProposalVote() {
		// $H$
	}

	/**
	 * Returns the ID of this AchievementProposalVote
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this AchievementProposalVote
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The InFavor of this AchievementProposalVote
	 */
	public boolean isInFavor() {
		return this.inFavor;
	}

	/**
	 * Sets the InFavor of this AchievementProposalVote
	 * @param inFavor The InFavor of this AchievementProposalVote
	 */
	public void setInFavor(boolean inFavor) {
		this.inFavor = inFavor;
	}

	/**
	 * @return The Senator of this AchievementProposalVote
	 */
	public User getSenator() {
		return this.senator;
	}

	/**
	 * Sets the Senator of this AchievementProposalVote
	 * @param senator The Senator of this AchievementProposalVote
	 */
	public void setSenator(User senator) {
		this.senator = senator;
	}

	/**
	 * @return The Proposal of this AchievementProposalVote
	 */
	public AchievementProposal getProposal() {
		return this.proposal;
	}

	/**
	 * Sets the Proposal of this AchievementProposalVote
	 * @param proposal The Proposal of this AchievementProposalVote
	 */
	public void setProposal(AchievementProposal proposal) {
		this.proposal = proposal;
	}

	// $GS$
}
