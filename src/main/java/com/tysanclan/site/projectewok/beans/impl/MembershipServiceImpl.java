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

import com.google.common.collect.Sets;
import com.jeroensteenbeeke.hyperion.events.IEventDispatcher;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.dao.JoinApplicationDAO;
import com.tysanclan.site.projectewok.entities.dao.JoinVerdictDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.JoinApplicationFilter;
import com.tysanclan.site.projectewok.entities.filter.JoinVerdictFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.event.LoginEvent;
import com.tysanclan.site.projectewok.event.MemberStatusEvent;
import com.tysanclan.site.projectewok.util.MemberUtil;
import io.vavr.collection.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class MembershipServiceImpl
		implements com.tysanclan.site.projectewok.beans.MembershipService {
	private static final Random random = new Random();

	@Autowired
	private IEventDispatcher dispatcher;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ForumService forumService;

	@Autowired
	private JoinApplicationDAO joinApplicationDAO;

	@Autowired
	private JoinVerdictDAO joinVerdictDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.DemocracyService democracyService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.NotificationService notificationService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.UserService userService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.MailService mailService;

	public void setMailService(
			com.tysanclan.site.projectewok.beans.MailService mailService) {
		this.mailService = mailService;
	}

	public void setUserService(
			com.tysanclan.site.projectewok.beans.UserService userService) {
		this.userService = userService;
	}

	public void setNotificationService(
			com.tysanclan.site.projectewok.beans.NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/**
	 * @param logService the logService to set
	 */
	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	/**
	 * @param democracyService the democracyService to set
	 */
	public void setDemocracyService(
			com.tysanclan.site.projectewok.beans.DemocracyService democracyService) {
		this.democracyService = democracyService;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @param forumService the forumService to set
	 */
	public void setForumService(ForumService forumService) {
		this.forumService = forumService;
	}

	/**
	 * @param joinApplicationDAO the joinApplicationDAO to set
	 */
	public void setJoinApplicationDAO(JoinApplicationDAO joinApplicationDAO) {
		this.joinApplicationDAO = joinApplicationDAO;
	}

	/**
	 * @param joinVerdictDAO the joinVerdictDAO to set
	 */
	public void setJoinVerdictDAO(JoinVerdictDAO joinVerdictDAO) {
		this.joinVerdictDAO = joinVerdictDAO;
	}

	public void setDispatcher(IEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void terminateMembership(User _user, boolean judicial) {
		userDAO.load(_user.getId()).forEach(user -> {

			if (user != null) {
				clearMentorStatus(user);
				clearSenatorStatus(user);
				clearChancellorStatus(user);
				clearEndorsements(user);
				user.setRank(judicial ? Rank.BANNED : Rank.FORUM);
				userDAO.update(user);
			}

			logService.logUserAction(user, "Membership",
					"Membership has been terminated");
		});
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void clearEndorsements(User user) {
		user.getEndorsedBy().clear();
		user.setEndorses(null);
		user.getEndorsedForSenateBy().clear();
		user.setEndorsesForSenate(null);
		userDAO.update(user);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void clearSenatorStatus(User _user) {
		if (_user.getRank() == Rank.SENATOR) {
			UserFilter filter = new UserFilter();
			filter.rank(Rank.SENATOR);
			long count = userDAO.countByFilter(filter);
			// Only the current user is a Senator
			if (count == 1) {
				// User is the last senator, so prepare new elections
				democracyService.createSenateElection();
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void clearChancellorStatus(User _user) {
		if (_user.getRank() == Rank.CHANCELLOR) {
			// If the user was the Chancellor, prepare new elections
			democracyService.createChancellorElection();
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void clearMentorStatus(User _user) {
		JoinApplicationFilter filter = new JoinApplicationFilter();
		filter.mentor(_user);

		Seq<JoinApplication> applications = joinApplicationDAO
				.findByFilter(filter);
		for (JoinApplication application : applications) {
			application.setMentor(null);
			joinApplicationDAO.update(application);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void registerAction(User user) {
		userDAO.load(user.getId()).forEach(_user -> {
			_user.setLastAction(new Date());
			_user.setVacation(false);
			userDAO.save(_user);
		});

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ForumThread applyForMembership(User user, String motivation,
			Game game, Realm realm) {
		return userDAO.load(user.getId()).map(_user -> {

			Forum forum = forumService.getInteractionForum();

			ForumThread joinThread = forumService.createForumThread(forum,
					"Join application: " + _user.getUsername(), motivation,
					_user);

			JoinApplication joinApplication = new JoinApplication();
			joinApplication.setApplicant(_user);
			joinApplication.setJoinThread(joinThread);
			joinApplication.setStartDate(new Date());
			joinApplication.setPrimaryGame(game);
			joinApplication.setPrimaryRealm(realm);

			joinApplicationDAO.save(joinApplication);

			logService.logUserAction(user, "Membership",
					"Has applied for membership");

			dispatcher.dispatchEvent(new MemberStatusEvent(
					com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType.APPLIED,
					user));

			return joinThread;
		}).getOrElseThrow(IllegalStateException::new);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MembershipService#setMentor(com.tysanclan.site.projectewok.entities.JoinApplication,
	 * com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setMentor(JoinApplication application, User mentor) {
		if (application.getMentor() == null) {
			userDAO.load(mentor.getId()).filter(MemberUtil::canUserBeMentor)
					.forEach(_mentor -> {
						joinApplicationDAO.load(application.getId())
								.forEach(_application -> {
									_application.setMentor(_mentor);
									joinApplicationDAO.update(_application);

									logService.logUserAction(_mentor,
											"Membership",
											"Has become Mentor of "
													+ application.getApplicant()
													.getUsername());

									checkResolved(application);
								});
					});
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MembershipService#setJoinApplicationVote(com.tysanclan.site.projectewok.entities.JoinApplication,
	 * com.tysanclan.site.projectewok.entities.User, boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setJoinApplicationVote(JoinApplication application,
			User senator, boolean inFavor) {
		joinApplicationDAO.load(application.getId()).forEach(
				_application -> userDAO.load(senator.getId())
						.forEach(_senator -> {

							JoinVerdictFilter filter = new JoinVerdictFilter();
							filter.application(_application);
							filter.user(_senator);

							long count = joinVerdictDAO.countByFilter(filter);
							if (count == 0
									&& _senator.getRank() == Rank.SENATOR) {
								JoinVerdict verdict = new JoinVerdict();
								verdict.setApplication(_application);
								verdict.setInFavor(inFavor);
								verdict.setUser(_senator);
								joinVerdictDAO.save(verdict);
							} else if (_senator.getRank() == Rank.SENATOR) {
								Seq<JoinVerdict> verdicts = joinVerdictDAO
										.findByFilter(filter);
								JoinVerdict verdict = verdicts.get(0);
								verdict.setInFavor(inFavor);
								joinVerdictDAO.update(verdict);
							}

							checkResolved(application);

						}));

	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void checkResolved(JoinApplication application) {
		if (application.getMentor() != null) {
			Set<User> negatives = Sets.newHashSet();
			for (JoinVerdict verdict : application.getVerdicts()) {
				if (verdict.isInFavor()) {
					democracyService.resolveJoinApplication(application);

					return;
				}

				negatives.add(verdict.getUser());
			}

			Set<User> notYetVoted = Sets
					.newHashSet(userDAO.findByRank(Rank.SENATOR));
			notYetVoted.removeAll(negatives);

			if (notYetVoted.isEmpty()) {
				democracyService.resolveJoinApplication(application);
			}
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MembershipService#performAutoPromotion(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void performAutoPromotion(User _user) {
		userDAO.load(_user.getId()).forEach(user -> {
			Rank newRank = MemberUtil
					.determineRankByJoinDate(_user.getJoinDate());

			user.setRank(newRank);

			userDAO.update(user);

			logService.logUserAction(user, "Membership",
					"User has been promoted to " + newRank.toString());
			notificationService.notifyUser(user,
					"You now have the rank of " + newRank.toString());
		});

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void feelLucky(User _luckyOne) {
		userDAO.load(_luckyOne.getId()).forEach(luckyOne -> {
			int action = random.nextInt(100);

			luckyOne.setLuckyScore(luckyOne.getLuckyScore() != null ?
					luckyOne.getLuckyScore() + 1 :
					1);

			userDAO.update(luckyOne);

			if (action < 23) {
				// Reset avatar
				luckyOne.setCustomTitle("Unlucky One");
				luckyOne.setSignature("Unlucky One");
				luckyOne.setImageURL("/images/unlucky.png");
				userDAO.update(luckyOne);
			} else if (action < 24) {
				// Reset score
				luckyOne.setLuckyScore(0);
				userDAO.update(luckyOne);

				notificationService.notifyUser(luckyOne,
						"You were unlucky enough to have your lucky score reset!");
			} else if (action < 58) {
				// Nothing, really lucky
			} else {
				// Rickroll
				luckyOne.setCustomTitle("I <3 Rick Astley");
				luckyOne.setSignature("Rick Astley's greatest fan");
				luckyOne.setImageURL("/images/unlucky2.png");
				userDAO.update(luckyOne);
			}
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MembershipService#onLogin(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	public void onLogin(User u) {
		dispatcher.dispatchEvent(new LoginEvent(u));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expireMembers() {
		List<User> expiredMembers = userService.getInactiveMembers();
		for (User user : expiredMembers) {
			terminateMembership(user, false);
			String mailBody = mailService.getInactivityExpirationMail(user);

			mailService.sendHTMLMail(user.getEMail(),
					"Tysan Clan Membership Expired", mailBody);

			dispatcher.dispatchEvent(new MemberStatusEvent(
					com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType.INACTIVITY_TIMEOUT,
					user));
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void determinePromotions() {
		UserFilter filter = new UserFilter();
		filter.rank(Rank.JUNIOR_MEMBER);
		filter.orRank(Rank.FULL_MEMBER);
		filter.orRank(Rank.SENIOR_MEMBER);

		Seq<User> users = userDAO.findByFilter(filter);
		for (User user : users) {
			if (MemberUtil.determineRankByJoinDate(user.getJoinDate()) != user
					.getRank()) {
				performAutoPromotion(user);
			}
		}

	}

	@Override
	public void bumpAccounts() {
		for (User user : userDAO.findAll()) {
			registerAction(user);
		}
	}
}
