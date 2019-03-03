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

/**
 * @author Jeroen Steenbeeke
 */
@javax.persistence.Entity
@Table(indexes = { //
		@Index(name = "IDX_REGULATIONCHANGEVOTE_CHANGE", columnList = "regulationChange_id"),
		@Index(name = "IDX_REGULATIONCHANGEVOTE_CASTER", columnList = "senator_id") })
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class RegulationChangeVote extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RegulationChangeVote")
	@SequenceGenerator(name = "RegulationChangeVote", sequenceName="SEQ_ID_RegulationChangeVote", allocationSize=1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private RegulationChange regulationChange;

	@ManyToOne(fetch = FetchType.LAZY)
	private User senator;

	@Column
	private boolean inFavor;

	// $P$

	/**
	 * Creates a new RegulationChangeVote object
	 */
	public RegulationChangeVote() {
		// $H$
	}

	/**
	 * Returns the ID of this RegulationChangeVote
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this RegulationChangeVote
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The RegulationChange of this RegulationChangeVote
	 */
	public RegulationChange getRegulationChange() {
		return this.regulationChange;
	}

	/**
	 * Sets the RegulationChange of this RegulationChangeVote
	 *
	 * @param regulationChange
	 *            The RegulationChange of this RegulationChangeVote
	 */
	public void setRegulationChange(RegulationChange regulationChange) {
		this.regulationChange = regulationChange;
	}

	/**
	 * @return The Senator of this RegulationChangeVote
	 */
	public User getSenator() {
		return this.senator;
	}

	/**
	 * Sets the Senator of this RegulationChangeVote
	 *
	 * @param senator
	 *            The Senator of this RegulationChangeVote
	 */
	public void setSenator(User senator) {
		this.senator = senator;
	}

	/**
	 * @return The InFavor of this RegulationChangeVote
	 */
	public boolean isInFavor() {
		return this.inFavor;
	}

	/**
	 * Sets the InFavor of this RegulationChangeVote
	 *
	 * @param inFavor
	 *            The InFavor of this RegulationChangeVote
	 */
	public void setInFavor(boolean inFavor) {
		this.inFavor = inFavor;
	}

	// $GS$
}
