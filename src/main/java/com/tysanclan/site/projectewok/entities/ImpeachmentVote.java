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

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_ImpeachmentVote_IMPEACHMENT", columnList = "impeachment_id"),
		@Index(name = "IDX_ImpeachmentVote_CASTER", columnList = "caster_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class ImpeachmentVote extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ImpeachmentVote")
	@SequenceGenerator(name = "ImpeachmentVote", sequenceName="SEQ_ID_ImpeachmentVote", allocationSize=1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Impeachment impeachment;

	@Column
	private boolean inFavor;

	@ManyToOne(fetch = FetchType.LAZY)
	private User caster;

	// $P$

	/**
	 * Creates a new ImpeachmentVote object
	 */
	public ImpeachmentVote() {
		// $H$
	}

	/**
	 * Returns the ID of this ImpeachmentVote
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this ImpeachmentVote
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Impeachment of this ImpeachmentVote
	 */
	public Impeachment getImpeachment() {
		return this.impeachment;
	}

	/**
	 * Sets the Impeachment of this ImpeachmentVote
	 *
	 * @param impeachment
	 *            The Impeachment of this ImpeachmentVote
	 */
	public void setImpeachment(Impeachment impeachment) {
		this.impeachment = impeachment;
	}

	/**
	 * @return The InFavor of this ImpeachmentVote
	 */
	public boolean isInFavor() {
		return this.inFavor;
	}

	/**
	 * Sets the InFavor of this ImpeachmentVote
	 *
	 * @param inFavor
	 *            The InFavor of this ImpeachmentVote
	 */
	public void setInFavor(boolean inFavor) {
		this.inFavor = inFavor;
	}

	/**
	 * @return The Caster of this ImpeachmentVote
	 */
	public User getCaster() {
		return this.caster;
	}

	/**
	 * Sets the Caster of this ImpeachmentVote
	 *
	 * @param caster
	 *            The Caster of this ImpeachmentVote
	 */
	public void setCaster(User caster) {
		this.caster = caster;
	}

	// $GS$
}
