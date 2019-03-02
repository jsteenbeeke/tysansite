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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jeroen Steenbeeke
 */
@javax.persistence.Entity
@Table(indexes = { //
		@Index(name = "IDX_REGULATIONCHANGE_REGULATION", columnList = "regulation_id"),
		//
		@Index(name = "IDX_REGULATIONCHANGE_PROPOSER", columnList = "proposer_id") })
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class RegulationChange extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	public static enum ChangeType {
		ADD, MODIFY, REPEAL;

		@Override
		public String toString() {
			return name().substring(0, 1).toUpperCase() + name().substring(1)
					.toLowerCase();
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RegulationChange")
	@SequenceGenerator(name = "RegulationChange", sequenceName = "SEQ_ID_RegulationChange")
	private Long id;

	@Column
	private boolean veto;

	@Column
	private Date start;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Regulation regulation;

	@OneToMany(mappedBy = "regulationChange", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<RegulationChangeVote> votes;

	@Enumerated(EnumType.STRING)
	private ChangeType changeType;

	@Column(nullable = true)
	private String title;

	@Column(nullable = true)
	@Lob

	private String description;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User proposer;

	// $P$

	/**
	 * Creates a new RegulationChange object
	 */
	public RegulationChange() {
		this.votes = new HashSet<RegulationChangeVote>();
		// $H$
	}

	/**
	 * Returns the ID of this RegulationChange
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this RegulationChange
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Veto of this RegulationChange
	 */
	public boolean isVeto() {
		return this.veto;
	}

	/**
	 * Sets the Veto of this RegulationChange
	 *
	 * @param veto
	 *            The Veto of this RegulationChange
	 */
	public void setVeto(boolean veto) {
		this.veto = veto;
	}

	/**
	 * @return The Regulation of this RegulationChange
	 */
	public Regulation getRegulation() {
		return this.regulation;
	}

	/**
	 * Sets the Regulation of this RegulationChange
	 *
	 * @param regulation
	 *            The Regulation of this RegulationChange
	 */
	public void setRegulation(Regulation regulation) {
		this.regulation = regulation;
	}

	/**
	 * @return The Votes of this RegulationChange
	 */
	public Set<RegulationChangeVote> getVotes() {
		return this.votes;
	}

	/**
	 * Sets the Votes of this RegulationChange
	 *
	 * @param votes
	 *            The Votes of this RegulationChange
	 */
	public void setVotes(Set<RegulationChangeVote> votes) {
		this.votes = votes;
	}

	/**
	 * @return the changeType
	 */
	public ChangeType getChangeType() {
		return changeType;
	}

	/**
	 * @param changeType
	 *            the changeType to set
	 */
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the proposer
	 */
	public User getProposer() {
		return proposer;
	}

	/**
	 * @param proposer
	 *            the proposer to set
	 */
	public void setProposer(User proposer) {
		this.proposer = proposer;
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
