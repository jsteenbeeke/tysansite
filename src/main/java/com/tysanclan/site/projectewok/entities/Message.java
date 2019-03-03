/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.entities;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.rest.api.data.RestMessage;
import com.tysanclan.site.projectewok.util.SerializableFunction;
import org.hibernate.annotations.Cache;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_MESSAGE_SENDER", columnList = "sender_id"), //
		@Index(name = "IDX_MESSAGE_CONVERSATION", columnList = "conversation_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Message extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	private static final SerializableFunction<Message, RestMessage> TO_REST = new SerializableFunction<Message, RestMessage>() {
		private static final long serialVersionUID = 1L;

		@Override
		@Nullable
		public RestMessage apply(@Nullable Message input) {
			if (input != null) {
				return new RestMessage(input.getSendTime(), input.getContent(),
						User.toRestFunction().apply(input.getSender()));
			}

			return null;
		}
	};

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Message")
	@SequenceGenerator(name = "Message", sequenceName = "SEQ_ID_Message", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User sender;

	@Column
	@Lob
	private String content;

	@Column
	private Date sentTime;

	@ManyToOne(fetch = FetchType.LAZY)
	private Conversation conversation;

	// $P$

	/**
	 * Creates a new Message object
	 */
	public Message() {
		// $H$
	}

	/**
	 * Returns the ID of this Message
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this Message
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The Sender of this Message
	 */
	public User getSender() {
		return this.sender;
	}

	/**
	 * Sets the Sender of this Message
	 *
	 * @param sender
	 *            The Sender of this Message
	 */
	public void setSender(User sender) {
		this.sender = sender;
	}

	/**
	 * @return The Content of this Message
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Sets the Content of this Message
	 *
	 * @param content
	 *            The Content of this Message
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return The SendTime of this Message
	 */
	public Date getSendTime() {
		return this.sentTime;
	}

	/**
	 * Sets the SendTime of this Message
	 *
	 * @param sendTime
	 *            The SendTime of this Message
	 */
	public void setSendTime(Date sendTime) {
		this.sentTime = sendTime;
	}

	/**
	 * @return The Conversation of this Message
	 */
	public Conversation getConversation() {
		return this.conversation;
	}

	/**
	 * Sets the Conversation of this Message
	 *
	 * @param conversation
	 *            The Conversation of this Message
	 */
	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public static SerializableFunction<Message, RestMessage> toRestFunction() {
		return TO_REST;
	}

	// $GS$
}
