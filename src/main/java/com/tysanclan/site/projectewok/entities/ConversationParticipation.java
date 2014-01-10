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
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.site.projectewok.util.SerializableFunction;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class ConversationParticipation extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	private static final SerializableFunction<ConversationParticipation, User> TO_USER = new SerializableFunction<ConversationParticipation, User>() {
		private static final long serialVersionUID = 1L;

		@Override
		@Nullable
		public User apply(@Nullable ConversationParticipation input) {
			return input != null ? input.getUser() : null;
		}
	};

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ConversationParticipation")
	@SequenceGenerator(name = "ConversationParticipation", sequenceName = "SEQ_ID_ConversationParticipation")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_ConversationParticipation_Conversation")
	private Conversation conversation;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_ConversationParticipation_User")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_ConversationParticipation_Folder")
	private MessageFolder messageFolder;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "READMESSAGES", joinColumns = @JoinColumn(name = "participation_id"), inverseJoinColumns = @JoinColumn(name = "message_id"))
	private Set<Message> readMessages;

	// $P$

	/**
	 * Creates a new ConversationParticipation object
	 */
	public ConversationParticipation() {
		this.readMessages = new HashSet<Message>();
		// $H$
	}

	/**
	 * Returns the ID of this ConversationParticipation
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this ConversationParticipation
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Conversation of this ConversationParticipation
	 */
	public Conversation getConversation() {
		return this.conversation;
	}

	/**
	 * Sets the Conversation of this ConversationParticipation
	 * 
	 * @param conversation
	 *            The Conversation of this ConversationParticipation
	 */
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	/**
	 * @return The User of this ConversationParticipation
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this ConversationParticipation
	 * 
	 * @param user
	 *            The User of this ConversationParticipation
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The MessageFolder of this ConversationParticipation
	 */
	public MessageFolder getMessageFolder() {
		return this.messageFolder;
	}

	/**
	 * Sets the MessageFolder of this ConversationParticipation
	 * 
	 * @param messageFolder
	 *            The MessageFolder of this ConversationParticipation
	 */
	public void setMessageFolder(MessageFolder messageFolder) {
		this.messageFolder = messageFolder;
	}

	/**
	 * @return The ReadMessages of this ConversationParticipation
	 */
	public Set<Message> getReadMessages() {
		return this.readMessages;
	}

	/**
	 * Sets the ReadMessages of this ConversationParticipation
	 * 
	 * @param readMessages
	 *            The ReadMessages of this ConversationParticipation
	 */
	public void setReadMessages(Set<Message> readMessages) {
		this.readMessages = readMessages;
	}

	public static SerializableFunction<ConversationParticipation, User> toUserFunction() {
		return TO_USER;
	}

	// $GS$
}
