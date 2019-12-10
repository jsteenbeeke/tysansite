package com.tysanclan.site.projectewok.beans.impl;

import com.jeroensteenbeeke.lux.TypedResult;
import com.tysanclan.site.myriad.importformat.*;
import com.tysanclan.site.projectewok.beans.MyriadExportService;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.dao.*;
import io.vavr.collection.Array;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Collectors;

@Service
class MyriadExportServiceImpl implements MyriadExportService {
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ConversationDAO conversationDAO;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private ForumCategoryDAO forumCategoryDAO;

	@Autowired
	private LogItemDAO logItemDAO;

	@Override
	public TypedResult<File> export() {
		return TypedResult.attempt(() -> {
			File export = File.createTempFile("ewok", ".xml");
			MIDataSet dataSet = new MIDataSet();

			dataSet.setUsers(new LinkedList<>());
			dataSet.setConversations(new LinkedList<>());
			dataSet.setGroups(new LinkedList<>());
			dataSet.setForumCategories(new LinkedList<>());

			for (User user : userDAO.findAll()) {
				MIUser miUser = new MIUser();
				miUser.setId(user.getId());
				miUser.setUsername(user.getUsername());
				miUser.setPassword(user.getArgon2hash());
				miUser.setMail(user.getEMail());
				miUser.setRank(MIRank.valueOf(user.getRank().name()));
				miUser.setGroups(new ArrayList<>());

				for (Group group : user.getGroups()) {
					miUser.getGroups().add(group.getId());
				}

				dataSet.getUsers().add(miUser);
			}

			for (Conversation conversation : conversationDAO.findAll()) {
				MIConversation miConversation = new MIConversation();
				miConversation.setTitle(conversation.getTitle());
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

			for (Group group : groupDAO.findAll()) {
				MIGroup mig = new MIGroup();
				mig.setId(group.getId());
				mig.setName(group.getName());
				mig.setDescription(group.getDescription());

				dataSet.getGroups().add(mig);
			}

			for (ForumCategory category : forumCategoryDAO.findAll().sortBy(ForumCategory::getId)) {
				MIForumCategory mic = new MIForumCategory();
				mic.setName(category.getName());
				mic.setForums(new LinkedList<>());

				for (Forum forum : category.getForums()) {
					MIForum mif = new MIForum();
					mif.setName(forum.getName());
					mif.setDescription(forum.getDescription());
					forum.asGroupForum().peek(gf -> mif.setGroup(gf.getGroup().getId()));
					mif.setForumThreads(new LinkedList<>());

					for (ForumThread thread : Array
						.ofAll(forum.getThreads())
						.sorted(Comparator
									.comparing((ForumThread t) -> Option.of(t.getLastPost()).getOrElse(t.getPostTime()))
									.reversed())) {
						MIForumThread mit = new MIForumThread();
						mit.setShadow(thread.isShadow());
						mit.setSticky(thread.isPostSticky());
						mit.setTitle(thread.getTitle());
						mit.setPoster(Option.of(thread.getPoster()).map(User::getId).getOrNull());
						mit.setForumPosts(new LinkedList<>());

						for (ForumPost post : Array.ofAll(thread.getPosts()).sortBy(ForumPost::getTime)) {
							MIForumPost mip = new MIForumPost();
							mip.setPoster(Option.of(post.getPoster()).map(User::getId).getOrNull());
							mip.setPostTime(post.getTime().getTime());
							mip.setShadow(post.isShadow());
							mip.setContent(post.getContent());

							mit.getForumPosts().add(mip);
						}


						mif.getForumThreads().add(mit);
					}


					mic.getForums().add(mif);
				}

				dataSet.getForumCategories().add(mic);
			}


			JAXB.marshal(dataSet, export);
			return export;
		});
	}
}
