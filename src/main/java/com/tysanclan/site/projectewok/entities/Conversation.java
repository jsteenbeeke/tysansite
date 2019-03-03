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

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.site.projectewok.util.SerializableFunction;
import org.hibernate.annotations.Cache;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Conversation extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Conversation")
	@SequenceGenerator(name = "Conversation", sequenceName="SEQ_ID_Conversation", allocationSize=1)
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

	private static SerializableFunction<Conversation, com.tysanclan.rest.api.data.RestConversation> TO_REST = new SerializableFunction<Conversation, com.tysanclan.rest.api.data.RestConversation>() {

		private static final long serialVersionUID = 1L;

		@Override
		@Nullable
		public com.tysanclan.rest.api.data.RestConversation apply(
				@Nullable Conversation input) {
			if (input != null) {
				List<com.tysanclan.rest.api.data.RestMessage> messages = Lists
						.transform(input.getMessages(),
								Message.toRestFunction());
				Set<com.tysanclan.rest.api.data.RestUser> participants = Sets
						.newHashSet(Iterables.transform(input.getParticipants(),
								Functions.compose(User.toRestFunction(),
										ConversationParticipation
												.toUserFunction())));

				com.tysanclan.rest.api.data.RestMessage last = messages
						.isEmpty() ? null : messages.get(messages.size() - 1);
				Date lastEntry = last != null ? last.getSentTime() : null;

				return new com.tysanclan.rest.api.data.RestConversation(
						input.getId(), input.getTitle(), lastEntry,
						participants, messages);
			}

			return null;
		}
	};

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

	public static SerializableFunction<Conversation, com.tysanclan.rest.api.data.RestConversation> toRestFunction() {
		return TO_REST;
	}

	// $GS$
}
