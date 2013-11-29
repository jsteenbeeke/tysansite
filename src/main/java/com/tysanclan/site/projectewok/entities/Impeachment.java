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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Impeachment extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Impeachment")
	@SequenceGenerator(name = "Impeachment", sequenceName = "SEQ_ID_Impeachment")
	private Long id;

	@OneToMany(mappedBy = "impeachment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private Set<ImpeachmentVote> votes;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_IMPEACHMENT_CHANCELLOR")
	private User chancellor;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_IMPEACHMENT_INITIATOR")
	private User initiator;

	@Column
	private Date start;

	// $P$

	/**
	 * Creates a new Impeachment object
	 */
	public Impeachment() {
		this.votes = new HashSet<ImpeachmentVote>();
		// $H$
	}

	/**
	 * Returns the ID of this Impeachment
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Impeachment
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Votes of this Impeachment
	 */
	public Set<ImpeachmentVote> getVotes() {
		return this.votes;
	}

	/**
	 * Sets the Votes of this Impeachment
	 * 
	 * @param votes
	 *            The Votes of this Impeachment
	 */
	public void setVotes(Set<ImpeachmentVote> votes) {
		this.votes = votes;
	}

	/**
	 * @return The Chancellor of this Impeachment
	 */
	public User getChancellor() {
		return this.chancellor;
	}

	/**
	 * Sets the Chancellor of this Impeachment
	 * 
	 * @param chancellor
	 *            The Chancellor of this Impeachment
	 */
	public void setChancellor(User chancellor) {
		this.chancellor = chancellor;
	}

	/**
	 * @return The Initiator of this Impeachment
	 */
	public User getInitiator() {
		return this.initiator;
	}

	/**
	 * Sets the Initiator of this Impeachment
	 * 
	 * @param initiator
	 *            The Initiator of this Impeachment
	 */
	public void setInitiator(User initiator) {
		this.initiator = initiator;
	}

	/**
	 * @return The Start of this Impeachment
	 */
	public Date getStart() {
		return this.start;
	}

	/**
	 * Sets the Start of this Impeachment
	 * 
	 * @param start
	 *            The Start of this Impeachment
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	// $GS$
}
