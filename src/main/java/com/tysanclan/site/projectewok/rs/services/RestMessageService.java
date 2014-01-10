package com.tysanclan.site.projectewok.rs.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.context.annotation.Scope;

import com.google.common.collect.Lists;
import com.tysanclan.rest.api.data.RestConversation;
import com.tysanclan.rest.api.data.RestMessage;
import com.tysanclan.rest.api.data.RestUser;
import com.tysanclan.rest.api.services.MessageService;
import com.tysanclan.site.projectewok.entities.Conversation;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.Message;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ConversationDAO;
import com.tysanclan.site.projectewok.entities.dao.ConversationParticipationDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ConversationFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.ConversationParticipationFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.rs.HttpStatusException;

@Named
@Scope("request")
public class RestMessageService extends BaseTokenVerifiedService implements
		MessageService {
	@Inject
	private ConversationParticipationDAO conversationParticipationDAO;

	@Inject
	private ConversationDAO conversationDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private com.tysanclan.site.projectewok.beans.MessageService messageService;

	public void setConversationDAO(ConversationDAO conversationDAO) {
		this.conversationDAO = conversationDAO;
	}

	public void setMessageService(
			com.tysanclan.site.projectewok.beans.MessageService messageService) {
		this.messageService = messageService;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setConversationParticipationDAO(
			ConversationParticipationDAO conversationParticipationDAO) {
		this.conversationParticipationDAO = conversationParticipationDAO;
	}

	@Override
	@GET
	@Produces("text/json")
	public List<RestConversation> getConversations(
			@QueryParam("t") String tokenString) {
		User user = verifyToken(tokenString);

		ConversationFilter filter = new ConversationFilter();
		filter.addParticipant(user);

		return Lists.transform(conversationDAO.findByFilter(filter),
				com.tysanclan.site.projectewok.entities.Conversation
						.toRestFunction());
	}

	@Override
	@PUT
	@Produces("text/json")
	public RestConversation createConversation(
			@QueryParam("t") String tokenString,
			@FormParam("recipients") List<RestUser> recipients,
			@FormParam("title") String title, @FormParam("body") String body) {
		User user = verifyToken(tokenString);

		List<User> rcv = Lists.newArrayListWithExpectedSize(recipients.size());
		for (RestUser u : recipients) {
			UserFilter filter = new UserFilter();
			filter.setUsername(u.getUsername());

			User rp = userDAO.getUniqueByFilter(filter);

			if (rp == null) {
				throw new HttpStatusException(400, "Invalid recipient");
			}

			rcv.add(rp);
		}

		ConversationParticipation convo = messageService.createConversation(
				user, rcv, title, body);

		return convo != null ? Conversation.toRestFunction().apply(
				convo.getConversation()) : null;
	}

	@Override
	@PUT
	@Path("/{id}")
	@Produces("text/json")
	public RestMessage respondToConversation(
			@QueryParam("t") String tokenString,
			@PathParam("id") long conversationId, @FormParam("body") String text) {
		User user = verifyToken(tokenString);

		Conversation conversation = conversationDAO.get(conversationId);

		if (conversation == null)
			throw new HttpStatusException(404, "Invalid conversation");

		Message response = messageService.respondToConversation(conversation,
				user, text);

		if (response == null) {
			throw new HttpStatusException(403, "Invalid conversation");
		}

		return Message.toRestFunction().apply(response);
	}

	@Override
	@DELETE
	@Path("/{id}")
	public void leaveConversation(@QueryParam("t") String tokenString,
			@PathParam("id") long conversationId) {
		User user = verifyToken(tokenString);

		Conversation conversation = conversationDAO.get(conversationId);

		if (conversation == null)
			throw new HttpStatusException(404, "Invalid conversation");

		ConversationParticipationFilter filter = new ConversationParticipationFilter();
		filter.setUser(user);
		filter.setConversation(conversation);

		ConversationParticipation convPart = conversationParticipationDAO
				.getUniqueByFilter(filter);

		if (convPart == null)
			throw new HttpStatusException(404,
					"Use does not participate in conversation");

		messageService.ceaseParticipation(convPart);

	}
}
