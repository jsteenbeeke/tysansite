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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Conversation extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Conversation")
	@SequenceGenerator(name = "Conversation", sequenceName = "SEQ_ID_Conversation")
	private Long id;

	@OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<ConversationParticipation> participants;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "conversation", fetch = FetchType.LAZY)
	@OrderBy("id asc")
	private List<Message> messages;

	@Column
	private String title;

	@Column
	private Date lastResponse;

	// $P$

	/**
	 * Creates a new Conversation object
	 */
	public Conversation() {
		this.participants = new HashSet<ConversationParticipation>();
		this.messages = new LinkedList<Message>();
		// $H$
	}

	/**
	 * Returns the ID of this Conversation
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Conversation
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The PArticipants of this Conversation
	 */
	public Set<ConversationParticipation> getParticipants() {
		return this.participants;
	}

	public void setParticipants(Set<ConversationParticipation> participants) {
		this.participants = participants;
	}

	/**
	 * @return The Messages of this Conversation
	 */
	public List<Message> getMessages() {
		return this.messages;
	}

	/**
	 * Sets the Messages of this Conversation
	 * 
	 * @param messages
	 *            The Messages of this Conversation
	 */
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	/**
	 * @return The Title of this Conversation
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the Title of this Conversation
	 * 
	 * @param title
	 *            The Title of this Conversation
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public Date getLastResponse() {
		return lastResponse;
	}

	public void setLastResponse(Date lastResponse) {
		this.lastResponse = lastResponse;
	}

	// $GS$
}
