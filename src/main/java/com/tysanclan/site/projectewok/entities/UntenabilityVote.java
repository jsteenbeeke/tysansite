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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_UntenabilityVote_Regulation", columnList = "regulation_id") })
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class UntenabilityVote extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UntenabilityVote")
	@SequenceGenerator(name = "UntenabilityVote", sequenceName="SEQ_ID_UntenabilityVote", allocationSize=1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Regulation regulation;

	@Column
	private Date start;

	@OneToMany(mappedBy = "vote", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Set<UntenabilityVoteChoice> choices;

	// $P$

	/**
	 * Creates a new UntenabilityVote object
	 */
	public UntenabilityVote() {
		this.choices = new HashSet<UntenabilityVoteChoice>();
		// $H$
	}

	/**
	 * Returns the ID of this UntenabilityVote
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this UntenabilityVote
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Regulation of this UntenabilityVote
	 */
	public Regulation getRegulation() {
		return this.regulation;
	}

	/**
	 * Sets the Regulation of this UntenabilityVote
	 *
	 * @param regulation
	 *            The Regulation of this UntenabilityVote
	 */
	public void setRegulation(Regulation regulation) {
		this.regulation = regulation;
	}

	/**
	 * @return The Start of this UntenabilityVote
	 */
	public Date getStart() {
		return this.start;
	}

	/**
	 * Sets the Start of this UntenabilityVote
	 *
	 * @param start
	 *            The Start of this UntenabilityVote
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * @return The Choices of this UntenabilityVote
	 */
	public Set<UntenabilityVoteChoice> getChoices() {
		return this.choices;
	}

	/**
	 * Sets the Choices of this UntenabilityVote
	 *
	 * @param choices
	 *            The Choices of this UntenabilityVote
	 */
	public void setChoices(Set<UntenabilityVoteChoice> choices) {
		this.choices = choices;
	}

	// $GS$
}
