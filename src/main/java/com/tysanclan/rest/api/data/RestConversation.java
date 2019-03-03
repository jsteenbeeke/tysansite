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
package com.tysanclan.rest.api.data;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class RestConversation {
	private long id;

	private String title;

	private Date lastEntry;

	private Set<RestUser> participants;

	private List<RestMessage> messages;

	public RestConversation() {
	}

	public RestConversation(long id, String title, Date lastEntry, Set<RestUser> participants,
			List<RestMessage> messages) {
		this.id = id;
		this.title = title;
		this.lastEntry = lastEntry;
		this.participants = participants;
		this.messages = messages;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Date getLastEntry() {
		return lastEntry;
	}

	public Set<RestUser> getParticipants() {
		return participants;
	}

	public List<RestMessage> getMessages() {
		return messages;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLastEntry(Date lastEntry) {
		this.lastEntry = lastEntry;
	}

	public void setParticipants(Set<RestUser> participants) {
		this.participants = participants;
	}

	public void setMessages(List<RestMessage> messages) {
		this.messages = messages;
	}

}
