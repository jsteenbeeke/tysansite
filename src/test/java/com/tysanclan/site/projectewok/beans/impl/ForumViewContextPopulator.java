package com.tysanclan.site.projectewok.beans.impl;

import com.jeroensteenbeeke.hyperion.annotation.Dataset;
import com.jeroensteenbeeke.hyperion.solstice.spring.IEntityPopulator;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
@Dataset(ForumViewContextPopulator.KEY)
public class ForumViewContextPopulator implements IEntityPopulator {
	public static final String KEY = "forum-view-context";

	@Autowired
	private UserService userService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private ForumService forumService;


	@Override
	public void createEntities() {
		Instant i = LocalDate.now().minusYears(5L).atStartOfDay().toInstant(ZoneOffset.UTC);

		User bannedDoug = userService.createUser("BannedDoug", "test", "j.steenbeeke+banneddoug@gmail.com");
		User forumSteve = userService.createUser("ForumSteve", "test", "j.steenbeeke+forumsteve@gmail.com");
		User memberBob = userService.createUser("MemberBob", "test", "j.steenbeeke+memberbob@gmail.com");
		User groupMemberDave = userService.createUser("GroupMemberDave", "test", "j.steenbeeke+groupmemberdave@gmail.com");

		userService.setUserImportData(bannedDoug.getId(), Rank.BANNED, i.getEpochSecond());
		userService.setUserImportData(forumSteve.getId(), Rank.FORUM, i.getEpochSecond());
		userService.setUserImportData(memberBob.getId(), Rank.CHANCELLOR, i.getEpochSecond());
		userService.setUserImportData(groupMemberDave.getId(), Rank.FULL_MEMBER, i.getEpochSecond());

		ForumCategory category = forumService.createCategory(memberBob, "Test", true);

		GroupCreationRequest groupRequest = groupService.createSocialGroupRequest(groupMemberDave, "Project Aardvark", "Secret test group", "Unit test");
		Group group = groupService.acceptRequest(memberBob, groupRequest);

		NewsForum news = forumService.createNewsForum("News", "The latest and greatest", true, category);

		Forum publicForum = forumService.createForum("Public", "A public forum", true, category, memberBob);
		Forum privateForum = forumService.createForum("Private", "A members-only forum", false, category, memberBob);
		forumService.setMembersOnly(memberBob, privateForum, true);

		GroupForum groupForum = forumService.createGroupForum("Project Aardvark", "Super Secret", category, group);

		forumService.createForumThread(publicForum, "You all suck", "Especially Prospero", bannedDoug);

		ForumThread publicThread = forumService.createForumThread(publicForum, "Well", "Things are rather quiet, aren't they?", forumSteve);
		forumService.replyToThread(publicThread, "Your mother!", bannedDoug);
		forumService.replyToThread(publicThread, "Thankfully", memberBob);
		forumService.replyToThread(publicThread, "Yeah now that Doug is gone", groupMemberDave);
		forumService.replyToThread(publicThread, "I'm not gone you dwarfsucker!", bannedDoug);

		ForumThread bobsBoast = forumService.createForumThread(news, "I regret to inform you...", "That I am awesome!", memberBob);

		forumService.replyToThread(bobsBoast, "No way man, you suck!", bannedDoug);
		forumService.replyToThread(bobsBoast, "Me too!", forumSteve);


		forumService.replyToThread(forumService.createForumThread(privateForum, "Geez", "Glad we got rid of that Doug", groupMemberDave), "Yeah, Doug was a douche", memberBob);

		forumService.createForumThread(groupForum, "No one can see this!", "Except for me", groupMemberDave);

	}

	@Override
	public int getPriority() {
		return 0;
	}
}
