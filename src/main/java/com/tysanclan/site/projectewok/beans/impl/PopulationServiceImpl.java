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

import com.jeroensteenbeeke.hyperion.tardis.scheduler.ApplicationContextServiceProvider;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.*;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.tasks.ChancellorElectionChecker;
import com.tysanclan.site.projectewok.tasks.SenateElectionChecker;
import com.tysanclan.site.projectewok.util.DateUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

@Component
@Scope("request")
public class PopulationServiceImpl
		implements PopulationService, ApplicationContextAware {
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ForumService forumService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private RealmService realmService;

	@Autowired
	private GameService gameService;

	@Autowired
	private FinanceService financeService;

	@Autowired
	private UserDAO userDAO;

	private ApplicationContext context;

	public void setForumService(ForumService forumService) {
		this.forumService = forumService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void createDebugSite() {
		Map<Rank, Integer> userTypes = new HashMap<Rank, Integer>();
		userTypes.put(Rank.CHANCELLOR, 1);
		userTypes.put(Rank.SENATOR, 5);
		userTypes.put(Rank.TRUTHSAYER, 3);
		userTypes.put(Rank.REVERED_MEMBER, 4);
		userTypes.put(Rank.SENIOR_MEMBER, 8);
		userTypes.put(Rank.FULL_MEMBER, 23);
		userTypes.put(Rank.JUNIOR_MEMBER, 17);
		userTypes.put(Rank.TRIAL, 7);
		userTypes.put(Rank.FORUM, 27);
		userTypes.put(Rank.BANNED, 3);

		Map<Rank, Long> joinTime = new HashMap<Rank, Long>();
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -1);
		joinTime.put(Rank.TRIAL, cal.getTimeInMillis());

		cal.add(Calendar.WEEK_OF_YEAR, -4);
		joinTime.put(Rank.JUNIOR_MEMBER, cal.getTimeInMillis());

		cal.add(Calendar.MONTH, -4);

		joinTime.put(Rank.FULL_MEMBER, cal.getTimeInMillis());

		cal.add(Calendar.YEAR, -2);

		joinTime.put(Rank.SENIOR_MEMBER, cal.getTimeInMillis());
		joinTime.put(Rank.TRUTHSAYER, cal.getTimeInMillis());
		joinTime.put(Rank.CHANCELLOR, cal.getTimeInMillis());
		joinTime.put(Rank.SENATOR, cal.getTimeInMillis());

		cal.add(Calendar.YEAR, -4);

		joinTime.put(Rank.REVERED_MEMBER, cal.getTimeInMillis());

		String[] potentialNames = { "Steve", "Bob", "Mike", "Fred", "George",
				"John", "Todd", "Kevin" };

		Random random = new Random();

		int counter = 0;
		boolean hasTreasurer = false, hasSteward = false, hasHerald = false;

		User chan = null;
		User ban = null;
		User averageJoe1 = null;
		User averageJoe2 = null;
		User averageJoe3 = null;
		User averageJoe4 = null;
		User averageJoe5 = null;
		User averageJoe6 = null;
		User averageJoe7 = null;
		User averageJoe8 = null;
		User averageJoe9 = null;

		for (Entry<Rank, Integer> entry : userTypes.entrySet()) {
			Rank rank = entry.getKey();
			int amount = entry.getValue();

			for (int i = 0; i < amount; i++) {
				String username =
						potentialNames[random.nextInt(potentialNames.length)]
								+ (++counter);

				User user = userService.createUser(username, "test",
						username + "@tysanclan.com");

				if (random.nextInt(3) == 0) {
					user.setBpm(random.nextInt(500));
					userDAO.update(user);
				}

				long joined = joinTime.containsKey(rank) ?
						joinTime.get(rank) :
						System.currentTimeMillis();
				userService.setUserImportData(user.getId(), rank, joined);

				if (rank == Rank.CHANCELLOR) {
					chan = user;
				}
				if (rank == Rank.BANNED && ban == null) {
					ban = user;
					ban.setRank(Rank.BANNED);
				}
				if (rank == Rank.SENIOR_MEMBER && averageJoe1 == null) {
					averageJoe1 = user;
				}
				if (rank == Rank.FULL_MEMBER && averageJoe2 == null) {
					averageJoe2 = user;
				}
				if (rank == Rank.JUNIOR_MEMBER && averageJoe3 == null) {
					averageJoe3 = user;
				}
				if (rank == Rank.SENIOR_MEMBER && averageJoe4 == null && !user
						.equals(averageJoe1)) {
					averageJoe4 = user;
				}
				if (rank == Rank.FULL_MEMBER && averageJoe5 == null && !user
						.equals(averageJoe2)) {
					averageJoe5 = user;
				}
				if (rank == Rank.JUNIOR_MEMBER && averageJoe6 == null && !user
						.equals(averageJoe3)) {
					averageJoe6 = user;
				}
				if (rank == Rank.SENIOR_MEMBER && averageJoe7 == null && !user
						.equals(averageJoe1) && !user.equals(averageJoe4)) {
					averageJoe7 = user;
				}
				if (rank == Rank.FULL_MEMBER && averageJoe8 == null && !user
						.equals(averageJoe2) && !user.equals(averageJoe5)) {
					averageJoe8 = user;
				}
				if (rank == Rank.JUNIOR_MEMBER && averageJoe9 == null && !user
						.equals(averageJoe3) && !user.equals(averageJoe6)) {
					averageJoe9 = user;
				}

				if (rank != Rank.BANNED && rank != Rank.FORUM) {

					if (!hasTreasurer && random.nextBoolean()) {
						hasTreasurer = true;
						Role role = roleService.createRole(user, "Treasurer",
								"The lord of cash", RoleType.TREASURER);
						roleService.assignTo(user.getId(), role.getId(),
								user.getId());
					} else if (!hasSteward && random.nextBoolean()) {
						hasSteward = true;
						Role role = roleService
								.createRole(user, "Steward", "The lord of code",
										RoleType.STEWARD);
						roleService.assignTo(user.getId(), role.getId(),
								user.getId());
					} else if (!hasHerald && random.nextBoolean()) {
						hasHerald = true;
						Role role = roleService.createRole(user, "Herald",
								"The voice of Tysan", RoleType.HERALD);
						roleService.assignTo(user.getId(), role.getId(),
								user.getId());
					}
				}
			}
		}

		Group testGroup = null;

		EnumMap<Group.JoinPolicy, User> leaders = new EnumMap<>(
				Group.JoinPolicy.class);
		leaders.put(Group.JoinPolicy.APPLICATION, averageJoe1);
		leaders.put(Group.JoinPolicy.INVITATION, averageJoe4);
		leaders.put(Group.JoinPolicy.OPEN, averageJoe7);

		for (Group.JoinPolicy joinPolicy : Group.JoinPolicy.values()) {

			@SuppressWarnings("deprecation")

			Group g = groupService
					.createSocialGroup("Test Group " + joinPolicy.name(),
							"A group for testing " + joinPolicy.name());
			groupService.setJoinPolicy(g, joinPolicy);

			groupService.setGroupLeader(leaders.get(joinPolicy), g);
			groupService.addUserToGroup(leaders.get(joinPolicy), g);
			groupService.addUserToGroup(averageJoe2, g);
			groupService.addUserToGroup(averageJoe3, g);

			if (testGroup == null) {
				testGroup = g;
			}
		}

		ForumCategory cat = forumService
				.createCategory(chan, "Test Category", false);
		Forum newsForum = forumService
				.createNewsForum("News forum", "News goes here", true, cat);
		Forum forum = forumService
				.createForum("Test Forum", "Everything else goes here", true,
						cat, chan);
		forumService.setInteractive(forum, true, chan);

		Forum membersOnly = forumService.createForum("Members Only Forum",
				"Members only stuff goes here", true, cat, chan);
		forumService.setMembersOnly(null, membersOnly, true);

		ForumCategory groupCat = forumService
				.createCategory(chan, "Test Category", true);

		Forum groupForum = forumService.createGroupForum("Test Group Forum",
				"The forum for the test group", groupCat, testGroup);

		generateForumThreads(newsForum, 6, averageJoe1, chan, averageJoe2,
				averageJoe3);
		generateForumThreads(forum, 19, averageJoe1, ban, chan, averageJoe2,
				averageJoe3);
		generateForumThreads(groupForum, 12, averageJoe1, averageJoe2,
				averageJoe3);
		generateForumThreads(membersOnly, 12, averageJoe1, averageJoe2,
				averageJoe3, ban);

		for (int i = 0; i < 200; i++) {
			generateShadowThread(forum, ban);
		}

		Game game = gameService.createGame("Diablo 9", new byte[0]);
		gameService.setGameSupervisor(game, averageJoe2);
		realmService.createRealm("USNorth", game, averageJoe2);

		financeService.requestPayment(roleService.getSteward(), "Burritos",
				BigDecimal.TEN);

		SenateElectionChecker checker = new SenateElectionChecker();
		checker.run(new ApplicationContextServiceProvider(context));

		ChancellorElectionChecker checker2 = new ChancellorElectionChecker();
		checker2.run(new ApplicationContextServiceProvider(context));

	}

	private void generateShadowThread(Forum forum, User ban) {

		forumService.createForumThread(forum, "PROSPERO SUCKS COCK!1",
				"This is a test thread", ban);

	}

	private void generateForumThreads(Forum forum, int amount,
			User... posters) {
		int j = 0;
		for (int i = 0; i < amount; i++) {
			User poster = posters[j++ % posters.length];
			while (poster.getRank() == Rank.BANNED) {
				// Replies may be banned, but not the thread starter
				poster = posters[j++ % posters.length];
			}

			ForumThread thread = forumService
					.createForumThread(forum, "Test thread " + (i + 1),
							"This is a test thread", poster);

			for (int k = 0; k < ((i % 2) + (i % 5)); k++) {
				poster = posters[j++ % posters.length];
				forumService.replyToThread(thread, "This is test response " + k,
						poster);
			}

		}
	}
}
