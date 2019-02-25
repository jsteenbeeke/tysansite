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
package com.tysanclan.site.projectewok.beans.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.entities.Conversation;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.Message;
import com.tysanclan.site.projectewok.entities.MessageFolder;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ConversationDAO;
import com.tysanclan.site.projectewok.entities.dao.ConversationParticipationDAO;
import com.tysanclan.site.projectewok.entities.dao.MessageDAO;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class MessageServiceImpl implements
		com.tysanclan.site.projectewok.beans.MessageService {
	@Autowired
	private MessageDAO messageDAO;

	@Autowired
	private ConversationParticipationDAO conversationParticipationDAO;

	@Autowired
	private ConversationDAO conversationDAO;

	public MessageServiceImpl() {
	}

	/**
	 * @return the messageDAO
	 */
	public MessageDAO getMessageDAO() {
		return messageDAO;
	}

	/**
	 * @param messageDAO
	 *            the messageDAO to set
	 */
	public void setMessageDAO(MessageDAO messageDAO) {
		this.messageDAO = messageDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MessageService#createConversation(com.tysanclan.site.projectewok.entities.User,
	 *      java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ConversationParticipation createConversation(User sender,
														List<User> participants, String title, String text) {
		return createConversation(sender, participants, title, text, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setImported(Conversation conversation, Date date) {
		Message message = conversation.getMessages().get(0);

		message.setSendTime(date);
		messageDAO.update(message);

		return true;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MessageService#createConversation(com.tysanclan.site.projectewok.entities.User,
	 *      java.util.List, java.lang.String, java.lang.String,
	 *      com.tysanclan.site.projectewok.entities.MessageFolder)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ConversationParticipation createConversation(User sender,
														List<User> participants, String title, String text,
														MessageFolder folder) {
		if (!participants.contains(sender) && sender != null) {
			participants.add(sender);
		}

		Conversation conversation = new Conversation();
		conversation.setTitle(BBCodeUtil.stripTags(title));
		conversationDAO.save(conversation);

		Message message = new Message();
		message.setContent(BBCodeUtil.stripTags(text));
		message.setConversation(conversation);
		message.setSender(sender);
		message.setSendTime(new Date());
		messageDAO.save(message);

		conversation.getMessages().add(message);
		conversation.setLastResponse(message.getSendTime());
		conversationDAO.update(conversation);

		ConversationParticipation result = null;

		for (User user : participants) {
			ConversationParticipation participation = new ConversationParticipation();
			if (user.equals(sender)) {
				participation.setMessageFolder(folder);
				participation.getReadMessages().add(message);
				result = participation;
			} else {
				participation.setMessageFolder(null);
			}
			participation.setConversation(conversation);
			participation.setUser(user);
			conversationParticipationDAO.save(participation);

			conversation.getParticipants().add(participation);
			conversationDAO.update(conversation);
		}

		return result;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MessageService#respondToConversation(com.tysanclan.site.projectewok.entities.Conversation,
	 *      com.tysanclan.site.projectewok.entities.User, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Message respondToConversation(Conversation conversation, User user,
										 String content) {
		Message message = new Message();
		message.setContent(content);
		message.setConversation(conversation);
		message.setSender(user);
		message.setSendTime(new Date());
		messageDAO.save(message);

		conversation.getMessages().add(message);
		conversation.setLastResponse(message.getSendTime());
		conversationDAO.update(conversation);

		return message;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MessageService#markAsRead(com.tysanclan.site.projectewok.entities.ConversationParticipation,
	 *      com.tysanclan.site.projectewok.entities.Message)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void markAsRead(ConversationParticipation participation,
						   Message object) {
		participation.getReadMessages().add(object);
		conversationParticipationDAO.update(participation);

	}

	/**
	 * @return the conversationParticipationDAO
	 */
	public ConversationParticipationDAO getConversationParticipationDAO() {
		return conversationParticipationDAO;
	}

	/**
	 * @param conversationParticipationDAO
	 *            the conversationParticipationDAO to set
	 */
	public void setConversationParticipationDAO(
			ConversationParticipationDAO conversationParticipationDAO) {
		this.conversationParticipationDAO = conversationParticipationDAO;
	}

	/**
	 * @return the conversationDAO
	 */
	public ConversationDAO getConversationDAO() {
		return conversationDAO;
	}

	/**
	 * @param conversationDAO
	 *            the conversationDAO to set
	 */
	public void setConversationDAO(ConversationDAO conversationDAO) {
		this.conversationDAO = conversationDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MessageService#ceaseParticipation(com.tysanclan.site.projectewok.entities.ConversationParticipation)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void ceaseParticipation(ConversationParticipation participation) {
		conversationParticipationDAO
				.load(participation.getId()).forEach(_participation -> {
			Conversation conversation = _participation.getConversation();

			conversation.getParticipants().remove(_participation);

			conversationParticipationDAO.delete(_participation);
			conversationDAO.update(conversation);

			if (conversation.getParticipants().isEmpty()) {
				conversationDAO.delete(conversation);
			}
		});
	}

}
