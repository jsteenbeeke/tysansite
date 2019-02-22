/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.beans.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.events.IEventDispatcher;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType;
import com.tysanclan.site.projectewok.entities.PenaltyPoint;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.Trial.Verdict;
import com.tysanclan.site.projectewok.entities.TruthsayerComplaint;
import com.tysanclan.site.projectewok.entities.TruthsayerComplaintVote;
import com.tysanclan.site.projectewok.entities.TruthsayerNomination;
import com.tysanclan.site.projectewok.entities.TruthsayerNominationVote;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.PenaltyPointDAO;
import com.tysanclan.site.projectewok.entities.dao.TrialDAO;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerComplaintDAO;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerComplaintVoteDAO;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerNominationDAO;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerNominationVoteDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.PenaltyPointFilter;
import com.tysanclan.site.projectewok.entities.filter.TruthsayerComplaintFilter;
import com.tysanclan.site.projectewok.entities.filter.TruthsayerNominationFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.event.MemberStatusEvent;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class LawEnforcementServiceImpl implements
		com.tysanclan.site.projectewok.beans.LawEnforcementService {
	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.MembershipService membershipService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.UserService userService;

	@Autowired
	private TruthsayerNominationDAO truthsayerNominationDAO;

	@Autowired
	private TruthsayerNominationVoteDAO truthsayerNominationVoteDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.ForumService forumService;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private TrialDAO trialDAO;

	@Autowired
	private PenaltyPointDAO penaltyPointDAO;

	@Autowired
	private IEventDispatcher dispatcher;

	@Autowired
	private TruthsayerComplaintDAO truthsayerComplaintDAO;

	@Autowired
	private TruthsayerComplaintVoteDAO truthsayerComplaintVoteDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.NotificationService notificationService;

	public void setTruthsayerComplaintVoteDAO(
			TruthsayerComplaintVoteDAO truthsayerComplaintVoteDAO) {
		this.truthsayerComplaintVoteDAO = truthsayerComplaintVoteDAO;
	}

	public void setTruthsayerComplaintDAO(
			TruthsayerComplaintDAO truthsayerComplaintDAO) {
		this.truthsayerComplaintDAO = truthsayerComplaintDAO;
	}

	public void setNotificationService(
			com.tysanclan.site.projectewok.beans.NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/**
	 * @param membershipService
	 *            the membershipService to set
	 */
	public void setMembershipService(
			com.tysanclan.site.projectewok.beans.MembershipService membershipService) {
		this.membershipService = membershipService;
	}

	/**
	 * @param penaltyPointDAO
	 *            the penaltyPointDAO to set
	 */
	public void setPenaltyPointDAO(PenaltyPointDAO penaltyPointDAO) {
		this.penaltyPointDAO = penaltyPointDAO;
	}

	/**
	 * @param trialDAO
	 *            the trialDAO to set
	 */
	public void setTrialDAO(TrialDAO trialDAO) {
		this.trialDAO = trialDAO;
	}

	/**
	 * @param logService
	 *            the logService to set
	 */
	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	/**
	 * @param truthsayerNominationDAO
	 *            the truthsayerNominationDAO to set
	 */
	public void setTruthsayerNominationDAO(
			TruthsayerNominationDAO truthsayerNominationDAO) {
		this.truthsayerNominationDAO = truthsayerNominationDAO;
	}

	/**
	 * @param truthsayerNominationVoteDAO
	 *            the truthsayerNominationVoteDAO to set
	 */
	public void setTruthsayerNominationVoteDAO(
			TruthsayerNominationVoteDAO truthsayerNominationVoteDAO) {
		this.truthsayerNominationVoteDAO = truthsayerNominationVoteDAO;
	}

	/**
	 * @param userDAO
	 *            the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @param forumService
	 *            the forumService to set
	 */
	public void setForumService(
			com.tysanclan.site.projectewok.beans.ForumService forumService) {
		this.forumService = forumService;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#nominateTruthsayer(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public TruthsayerNomination nominateTruthsayer(User nominator, User nominee) {
		if (nominator.getRank() == Rank.CHANCELLOR) {
			TruthsayerNominationFilter filter = new TruthsayerNominationFilter();
			filter.setNominee(nominee);

			if (truthsayerNominationDAO.countByFilter(filter) == 0) {

				TruthsayerNomination nomination = new TruthsayerNomination();
				nomination.setUser(nominee);

				truthsayerNominationDAO.save(nomination);

				logService.logUserAction(nominator, "Truthsayers",
						"" + nominee.getUsername()
								+ " was nominated to become a Truthsayer");

				notificationService.notifyUser(nominee,
						"You have been nominated to become a Truthsayer");

				return nomination;
			}
		}

		return null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#acceptTruthsayerNomination(com.tysanclan.site.projectewok.entities.TruthsayerNomination)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void acceptTruthsayerNomination(TruthsayerNomination nomination) {
		TruthsayerNomination _nomination = truthsayerNominationDAO
				.load(nomination.getId());

		logService.logUserAction(_nomination.getUser(), "Truthsayers",
				"User has accepted his nomination as Truthsayer");

		_nomination.setVoteStart(new Date());

		truthsayerNominationDAO.update(_nomination);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#declineTruthsayerNomination(com.tysanclan.site.projectewok.entities.TruthsayerNomination)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void declineTruthsayerNomination(TruthsayerNomination nomination) {
		TruthsayerNomination _nomination = truthsayerNominationDAO
				.load(nomination.getId());

		truthsayerNominationDAO.delete(_nomination);

		logService.logUserAction(_nomination.getUser(), "Truthsayers",
				"User has declined his nomination as Truthsayer");

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#voteAgainst(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.TruthsayerNomination)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void voteAgainst(User senator, TruthsayerNomination nomination) {
		TruthsayerNomination _nomination = truthsayerNominationDAO
				.load(nomination.getId());

		castVote(senator, _nomination, false);

		logService.logUserAction(senator, "Truthsayers",
				"Has voted against accepting "
						+ _nomination.getUser().getUsername()
						+ " as a Truthsayer");

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private void castVote(User senator, TruthsayerNomination _nomination,
			boolean verdict) {
		for (TruthsayerNominationVote vote : _nomination.getVotes()) {
			if (vote.getSenator().equals(senator)) {
				return;
			}
		}

		TruthsayerNominationVote vote = new TruthsayerNominationVote();
		vote.setNomination(_nomination);
		vote.setSenator(senator);
		vote.setVerdict(verdict);

		truthsayerNominationVoteDAO.save(vote);

		truthsayerNominationDAO.evict(_nomination);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#voteInFavor(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.TruthsayerNomination)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void voteInFavor(User senator, TruthsayerNomination nomination) {
		TruthsayerNomination _nomination = truthsayerNominationDAO
				.load(nomination.getId());

		castVote(senator, _nomination, true);

		logService.logUserAction(senator, "Truthsayers",
				"Has voted in favor of accepting "
						+ _nomination.getUser().getUsername()
						+ " as a Truthsayer");

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#resolveNomination(com.tysanclan.site.projectewok.entities.TruthsayerNomination)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveNomination(TruthsayerNomination nomination) {
		TruthsayerNomination _nomination = truthsayerNominationDAO
				.load(nomination.getId());
		int yesCount = 0, noCount = 0;

		for (TruthsayerNominationVote vote : _nomination.getVotes()) {
			if (vote.getVerdict()) {
				yesCount++;
			} else {
				noCount++;
			}
		}

		boolean verdict = true;

		if (yesCount > 0 || noCount > 0) {
			int total = yesCount + noCount;
			int fraction = (100 * yesCount) / total;
			if (fraction < 51) {
				verdict = false;
			}
		}

		User user = _nomination.getUser();

		if (verdict) {
			user.setRank(Rank.TRUTHSAYER);

			userDAO.update(user);

			logService.logSystemAction("Truthsayers",
					"User " + user.getUsername()
							+ " was accepted as Truthsayer by the Senate");

			notificationService.notifyUser(user, "You are now a Truthsayer");
		} else {
			logService.logSystemAction("Truthsayers",
					"User " + user.getUsername()
							+ " was not accepted as Truthsayer by the Senate");
		}

		truthsayerNominationDAO.delete(_nomination);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#startTrial(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.User, java.lang.String,
	 *      java.util.Collection)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Trial startTrial(User accuser, User accused, String motivation,
			Collection<Regulation> regulations) {
		if (accuser == null || accused == null || motivation == null
				|| regulations == null || regulations.isEmpty()) {
			return null;
		}

		User _accuser = userDAO.load(accuser.getId());
		User _accused = userDAO.load(accused.getId());

		Trial trial = new Trial();
		trial.setAccused(_accused);
		trial.setAccuser(_accuser);
		trial.setTrialThread(null);
		trial.setMotivation(BBCodeUtil.stripTags(motivation));
		List<Regulation> _regulations = new LinkedList<Regulation>();
		_regulations.addAll(regulations);

		trial.setRegulations(_regulations);

		trialDAO.save(trial);

		return trial;

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#confirmTrial(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Trial)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Trial confirmTrial(User truthsayer, Trial _trial) {
		Trial trial = trialDAO.load(_trial.getId());

		if (truthsayer.getRank() != Rank.TRUTHSAYER) {
			return null;
		}

		UserFilter filter = new UserFilter();
		filter.addRank(Rank.TRUTHSAYER);

		List<User> truthsayers = userDAO.findByFilter(filter);
		if (truthsayers.contains(trial.getAccuser())) {
			truthsayers.remove(trial.getAccuser());
		}
		if (truthsayers.contains(trial.getAccused())) {
			truthsayers.remove(trial.getAccused());
		}

		if (truthsayers.isEmpty()) {
			// Highly unusual, but possible
			UserFilter filter2 = new UserFilter();
			filter2.addRank(Rank.CHANCELLOR);
			filter2.addRank(Rank.SENATOR);

			truthsayers.addAll(userDAO.findByFilter(filter2));
		}

		if (truthsayers.contains(trial.getAccuser())) {
			truthsayers.remove(trial.getAccuser());
		}
		if (truthsayers.contains(trial.getAccused())) {
			truthsayers.remove(trial.getAccused());
		}

		if (truthsayers.isEmpty()) {
			// All hell has broken loose - 2 truthsayers accusing one another
			// while there is no chancellor and no senate
			return null;
		}

		ForumThread thread = forumService.createEmptyForumThread(
				forumService.getInteractionForum(), "Trial for "
						+ trial.getAccused().getUsername(), truthsayer);

		trial.setTrialThread(thread);
		trial.setJudge(truthsayers.get(new Random(trial.getId())
				.nextInt(truthsayers.size())));
		trialDAO.update(trial);

		forumService
				.replyToThread(
						thread,
						"<strong><span style=\"text-decoration: underline;\">Autogenerated post</span></strong><br /><p>Our member "
								+ trial.getAccused().getUsername()
								+ " has been accused of rules violations</p>",
						trial.getJudge());

		notificationService.notifyUser(trial.getAccused(),
				"You have been accused of violating one or more regulations");

		notificationService.notifyUser(trial.getAccuser(),
				"Your request for a trial against "
						+ trial.getAccused().getUsername()
						+ " has been granted");

		return trial;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#dismissTrial(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Trial)
	 */
	@Override
	public void dismissTrial(User truthsayer, Trial _trial) {
		Trial trial = trialDAO.load(_trial.getId());

		if (truthsayer.getRank() != Rank.TRUTHSAYER) {
			return;
		}

		trialDAO.delete(trial);

		notificationService.notifyUser(trial.getAccuser(),
				"Your request for a trial against "
						+ trial.getAccused().getUsername()
						+ " has been dismissed");

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#passVerdict(com.tysanclan.site.projectewok.entities.Trial,
	 *      com.tysanclan.site.projectewok.entities.Trial.Verdict)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void passVerdict(Trial trial, Verdict verdict) {
		forumService.lockThread(trial.getTrialThread(), trial.getJudge());

		int points = 0;

		trial.setVerdict(verdict);
		trialDAO.update(trial);

		switch (verdict) {
			case MAJOR:
				points = 3;
				break;
			case MEDIUM:
				points = 2;
				break;
			case MINOR:
				points = 1;
				break;
			case INNOCENT:
				logService.logUserAction(trial.getJudge(), "Justice",
						"User was declared innocent");
				notificationService.notifyUser(trial.getAccused(),
						"You were declared innocent");
		}

		for (int i = 0; i < points; i++) {
			PenaltyPoint point = new PenaltyPoint();
			point.setGiven(new Date());
			point.setUser(trial.getAccused());
			penaltyPointDAO.save(point);
		}

		if (points > 0) {
			switch (trial.getAccused().getRank()) {
				case SENATOR:
					trial.getAccused().setRank(
							MemberUtil.determineRankByJoinDate(trial
									.getAccused().getJoinDate()));
					userDAO.update(trial.getAccused());
					logService
							.logUserAction(trial.getJudge(), "Justice",
									"User was removed from the Senate as per Charter section 3.2.3.2");

					notificationService
							.notifyUser(
									trial.getAccused(),
									"You were removed from the Senate for a Regulation violation (Charter section 3.2.3.2)");
					break;
				case TRUTHSAYER:
					trial.getAccused().setRank(Rank.FORUM);
					userDAO.update(trial.getAccused());
					logService
							.logUserAction(trial.getJudge(), "Justice",
									"User membership was terminated as per Charter section 3.3.3.1");
					return;
				default:
					break;
			}

			logService.logUserAction(trial.getJudge(), "Justice",
					"User was declared guilty, and given " + points
							+ " penalty points");
			notificationService.notifyUser(trial.getAccused(),
					"You were declared guilty, and given " + points
							+ " penalty points");
		}

		checkPenaltyPoints();

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#checkPenaltyPoints()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkPenaltyPoints() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.MONTH, -6);

		for (User user : userService.getMembers()) {
			PenaltyPointFilter filter = new PenaltyPointFilter();
			filter.setDateAfter(cal.getTime());
			filter.setUser(user);

			if (penaltyPointDAO.countByFilter(filter) >= 3) {
				logService.logUserAction(user, "Justice",
						"User was banned for misconduct");

				membershipService.terminateMembership(user, true);

				dispatcher.dispatchEvent(new MemberStatusEvent(
						ChangeType.FORCED_OUT, user));

				userService.banUser(null, user);
			}
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#restrainAccused(com.tysanclan.site.projectewok.entities.Trial)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void restrainAccused(Trial _trial) {
		Trial trial = trialDAO.load(_trial.getId());
		trial.setRestrained(true);
		trialDAO.update(trial);

		logService.logUserAction(trial.getJudge(), "Justice", "Has restricted "
				+ trial.getAccused().getUsername() + "'s posting privileges");

		notificationService
				.notifyUser(
						trial.getAccused(),
						"You were restrained for misbehavior during trial, you can only post in trial threads that concern you until they have been resolved");
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LawEnforcementService#unrestrainAccused(com.tysanclan.site.projectewok.entities.Trial)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void unrestrainAccused(Trial _trial) {
		Trial trial = trialDAO.load(_trial.getId());
		trial.setRestrained(false);
		trialDAO.update(trial);

		logService.logUserAction(trial.getJudge(), "Justice",
				"Has lifted the restriction of "
						+ trial.getAccused().getUsername()
						+ "'s posting privileges");
		notificationService.notifyUser(trial.getAccused(),
				"Your restraint has been lifted");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void complaintMediated(TruthsayerComplaint complaint) {
		truthsayerComplaintDAO.delete(complaint);

		notificationService.notifyUser(complaint.getTruthsayer(),
				"The Chancellor has dismissed a complaint against you");

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void complaintToSenate(TruthsayerComplaint complaint,
			boolean byChancellor) {
		complaint.setMediated(true);
		complaint.setStart(new Date());

		if (byChancellor) {
			notificationService
					.notifyUser(complaint.getTruthsayer(),
							"The Chancellor has passed a complaint against you to the Senate");
		} else {
			notificationService
					.notifyUser(complaint.getTruthsayer(),
							"A complaint filed against you has been automatically passed to the Senate");

		}

		truthsayerComplaintDAO.update(complaint);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void fileComplaint(User complainer, User truthsayer,
			String motivation) {
		TruthsayerComplaint complaint = new TruthsayerComplaint();
		complaint.setComplainer(complainer);
		complaint.setMediated(false);
		complaint.setStart(new Date());
		complaint.setTruthsayer(truthsayer);
		complaint.setComplaint(BBCodeUtil.stripTags(motivation));
		truthsayerComplaintDAO.save(complaint);

		notificationService.notifyUser(truthsayer,
				"A complaint has been filed against you");

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setComplaintObserved(TruthsayerComplaint c) {
		c.setObserved(true);
		truthsayerComplaintDAO.update(c);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void passComplaintVote(TruthsayerComplaint complaint, User senator,
			boolean inFavor) {
		for (TruthsayerComplaintVote vote : complaint.getVotes()) {
			if (vote.getCaster().equals(senator)) {
				return;
			}
		}

		TruthsayerComplaintVote vote = new TruthsayerComplaintVote();
		vote.setCaster(senator);
		vote.setComplaint(complaint);
		vote.setInFavor(inFavor);
		truthsayerComplaintVoteDAO.save(vote);

		complaint.getVotes().add(vote);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveComplaint(TruthsayerComplaint complaint) {
		UserFilter filter = new UserFilter();
		filter.addRank(Rank.SENATOR);

		Set<User> abstainingSenators = new HashSet<User>(
				userDAO.findByFilter(filter));

		int yes = 0;
		int no = 0;

		for (TruthsayerComplaintVote vote : complaint.getVotes()) {
			if (vote.isInFavor()) {
				yes++;
			} else {
				no++;
			}
			abstainingSenators.remove(vote.getCaster());
		}

		no += abstainingSenators.size();

		int total = yes + no;

		int percentage = (100 * yes) / total;

		User user = complaint.getTruthsayer();

		if (percentage >= 66) {
			user.setRank(MemberUtil.determineRankByJoinDate(user.getJoinDate()));
			logService.logUserAction(user, "Justice",
					"Has been stripped of Truthsayer privileges by the Senate ("
							+ percentage + "%)");
			notificationService.notifyUser(user,
					"The Senate has stripped you of your Truthsayer position ("
							+ percentage + "%)");

			notificationService.notifyUser(complaint.getComplainer(),
					"The Senate has stripped " + user.getUsername()
							+ " of his/her Truthsayer position (" + percentage
							+ "%)");
		} else {
			logService.logUserAction(user, "Justice",
					"Has been cleared of a Truthsayer complaint by the Senate ("
							+ percentage + "%)");

			notificationService.notifyUser(user,
					"The Senate has decided to maintain your Truthsayer position ("
							+ percentage + "%)");
			notificationService.notifyUser(complaint.getComplainer(),
					"The Senate has decided to maintain " + user.getUsername()
							+ "'s Truthsayer position (" + percentage + "%)");
		}

		truthsayerComplaintDAO.delete(complaint);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveComplaints() {
		TruthsayerComplaintFilter filter = new TruthsayerComplaintFilter();
		filter.setMediated(true);
		filter.setStartBefore(new DateTime().minusWeeks(1).toDate());
		for (TruthsayerComplaint complaint : truthsayerComplaintDAO
				.findByFilter(filter)) {
			resolveComplaint(complaint);
		}

		filter.setMediated(false);
		for (TruthsayerComplaint complaint : truthsayerComplaintDAO
				.findByFilter(filter)) {
			complaintToSenate(complaint, false);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveNominations() {
		Calendar calendar = DateUtil.getCalendarInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, -1);

		TruthsayerNominationFilter filter = new TruthsayerNominationFilter();
		filter.setStartBefore(calendar.getTime());

		List<TruthsayerNomination> nominations = truthsayerNominationDAO
				.findByFilter(filter);

		for (TruthsayerNomination nomination : nominations) {
			resolveNomination(nomination);
		}
	}
}
