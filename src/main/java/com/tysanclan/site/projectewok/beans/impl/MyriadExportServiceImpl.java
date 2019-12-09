package com.tysanclan.site.projectewok.beans.impl;

import com.jeroensteenbeeke.lux.TypedResult;
import com.tysanclan.site.myriad.importformat.*;
import com.tysanclan.site.projectewok.beans.MyriadExportService;
import com.tysanclan.site.projectewok.entities.Conversation;
import com.tysanclan.site.projectewok.entities.ConversationParticipation;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ConversationDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Service
class MyriadExportServiceImpl implements MyriadExportService {
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ConversationDAO conversationDAO;

	@Override
	public TypedResult<File> export() {
		return TypedResult.attempt(() -> {
			File export = File.createTempFile("ewok", ".xml");
			MIDataSet dataSet = new MIDataSet();

			dataSet.setUsers(new LinkedList<>());
			dataSet.setConversations(new LinkedList<>());

			for (User user : userDAO.findAll()) {
				MIUser miUser = new MIUser();
				miUser.setId(user.getId());
				miUser.setUsername(user.getUsername());
				miUser.setPassword(user.getArgon2hash());
				miUser.setMail(user.getEMail());
				miUser.setRank(MIRank.valueOf(user.getRank().name()));

				dataSet.getUsers().add(miUser);
			}

			for (Conversation conversation : conversationDAO.findAll()) {
				MIConversation miConversation = new MIConversation();
				miConversation.setParticipants(
					conversation.getParticipants().stream()
								.map(ConversationParticipation::getUser)
								.map(User::getId)
								.collect(Collectors.toList())
				);
				miConversation.setMessages(conversation.getMessages()
													   .stream()
													   .map(m -> {
														   MIMessage msg = new MIMessage();
														   msg.setTimeSent(m.getSendTime().getTime());
														   msg.setSender(m.getSender().getId());
														   msg.setContents(m.getContent());
														   return msg;
													   }).collect(Collectors.toList())
				);
			}

			JAXB.marshal(dataSet, export);
			return export;
		});
	}
}
