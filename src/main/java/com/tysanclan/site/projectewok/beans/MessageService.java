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
package com.tysanclan.site.projectewok.beans;

import java.util.Date;
import java.util.List;

import com.tysanclan.site.projectewok.entities.Conversation;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.Message;
import com.tysanclan.site.projectewok.entities.MessageFolder;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface MessageService {
	public ConversationParticipation createConversation(User sender,
			List<User> participants, String title, String text);

	public ConversationParticipation createConversation(User sender,
			List<User> participants, String title, String text,
			MessageFolder folder);

	public boolean setImported(Conversation conversation, Date date);

	public void markAsRead(ConversationParticipation participation,
			Message object);

	public Message respondToConversation(Conversation conversation, User user,
			String message);

	public void ceaseParticipation(ConversationParticipation participation);
}
