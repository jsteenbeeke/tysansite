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
import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_EVENT_ORGANIZER", columnList = "organizer_id"), //
		@Index(name = "IDX_EVENT_THREAD", columnList = "eventThread_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Event extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Event")
	@SequenceGenerator(name = "Event", sequenceName="SEQ_ID_Event", allocationSize=1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User organizer;

	@Column
	private Date date;

	@OneToOne(fetch = FetchType.LAZY)
	private ForumThread eventThread;

	// $P$

	/**
	 * Creates a new Event object
	 */
	public Event() {
		// $H$
	}

	/**
	 * Returns the ID of this Event
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Event
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Organizer of this Event
	 */
	public User getOrganizer() {
		return this.organizer;
	}

	/**
	 * Sets the Organizer of this Event
	 *
	 * @param organizer
	 *            The Organizer of this Event
	 */
	public void setOrganizer(User organizer) {
		this.organizer = organizer;
	}

	/**
	 * @return The Date of this Event
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * Sets the Date of this Event
	 *
	 * @param date
	 *            The Date of this Event
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the eventThread
	 */
	public ForumThread getEventThread() {
		return eventThread;
	}

	/**
	 * @param eventThread
	 *            the eventThread to set
	 */
	public void setEventThread(ForumThread eventThread) {
		this.eventThread = eventThread;
	}

	// $GS$
}
