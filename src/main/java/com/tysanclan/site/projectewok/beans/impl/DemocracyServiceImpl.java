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

import com.jeroensteenbeeke.hyperion.events.IEventDispatcher;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.beans.MailService;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.RegulationChange.ChangeType;
import com.tysanclan.site.projectewok.entities.dao.*;
import com.tysanclan.site.projectewok.entities.filter.*;
import com.tysanclan.site.projectewok.event.MemberStatusEvent;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import io.vavr.collection.Seq;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class DemocracyServiceImpl
	implements com.tysanclan.site.projectewok.beans.DemocracyService {


	@Autowired
	private ChancellorElectionDAO chancellorElectionDAO;

	@Autowired
	private SenateElectionDAO senateElectionDAO;

	@Autowired
	private GroupLeaderElectionDAO groupLeaderElectionDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private AcceptanceVoteDAO acceptanceVoteDAO;

	@Autowired
	private AcceptanceVoteVerdictDAO acceptanceVoteVerdictDAO;

	@Autowired
	private CompoundVoteDAO compoundVoteDAO;
	@Autowired
	private CompoundVoteChoiceDAO compoundVoteChoiceDAO;

	@Autowired
	private JoinApplicationDAO joinApplicationDAO;

	@Autowired
	private JoinVerdictDAO joinVerdictDAO;

	@Autowired
	private ImpeachmentDAO impeachmentDAO;

	@Autowired
	private ImpeachmentVoteDAO impeachmentVoteDAO;

	@Autowired
	private UntenabilityVoteDAO untenabilityVoteDAO;

	@Autowired
	private UntenabilityVoteChoiceDAO untenabilityVoteChoiceDAO;

	@Autowired
	private RegulationDAO regulationDAO;

	@Autowired
	private RegulationChangeDAO regulationChangeDAO;

	@Autowired
	private RegulationChangeVoteDAO regulationChangeVoteDAO;

	@Autowired
	private DonationDAO donationDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.MailService mailService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.UserService userService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.NotificationService notificationService;

	@Autowired
	private IEventDispatcher dispatcher;

	public void setDispatcher(IEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setJoinVerdictDAO(JoinVerdictDAO joinVerdictDAO) {
		this.joinVerdictDAO = joinVerdictDAO;
	}

	/**
	 * @param donationDAO the donationDAO to set
	 */
	public void setDonationDAO(DonationDAO donationDAO) {
		this.donationDAO = donationDAO;
	}

	public void setNotificationService(
		com.tysanclan.site.projectewok.beans.NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/**
	 * @param regulationChangeVoteDAO the regulationChangeVoteDAO to set
	 */
	public void setRegulationChangeVoteDAO(
		RegulationChangeVoteDAO regulationChangeVoteDAO) {
		this.regulationChangeVoteDAO = regulationChangeVoteDAO;
	}

	public void setMembershipService(
		com.tysanclan.site.projectewok.beans.UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @param compoundVoteDAO the compoundVoteDAO to set
	 */
	public void setCompoundVoteDAO(CompoundVoteDAO compoundVoteDAO) {
		this.compoundVoteDAO = compoundVoteDAO;
	}

	/**
	 * @param compoundVoteChoiceDAO the compoundVoteChoiceDAO to set
	 */
	public void setCompoundVoteChoiceDAO(
		CompoundVoteChoiceDAO compoundVoteChoiceDAO) {
		this.compoundVoteChoiceDAO = compoundVoteChoiceDAO;
	}

	/**
	 * @param acceptanceVoteDAO the acceptanceVoteDAO to set
	 */
	public void setAcceptanceVoteDAO(AcceptanceVoteDAO acceptanceVoteDAO) {
		this.acceptanceVoteDAO = acceptanceVoteDAO;
	}

	/**
	 * @param joinApplicationDAO the joinApplicationDAO to set
	 */
	public void setJoinApplicationDAO(JoinApplicationDAO joinApplicationDAO) {
		this.joinApplicationDAO = joinApplicationDAO;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * @param groupDAO the groupDAO to set
	 */
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	/**
	 * @param impeachmentDAO the impeachmentDAO to set
	 */
	public void setImpeachmentDAO(ImpeachmentDAO impeachmentDAO) {
		this.impeachmentDAO = impeachmentDAO;
	}

	/**
	 * @param impeachmentVoteDAO the impeachmentVoteDAO to set
	 */
	public void setImpeachmentVoteDAO(ImpeachmentVoteDAO impeachmentVoteDAO) {
		this.impeachmentVoteDAO = impeachmentVoteDAO;
	}

	/**
	 * @param logService the logService to set
	 */
	public void setLogService(
		com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	/**
	 * @param acceptanceVoteVerdictDAO the acceptanceVoteVerdictDAO to set
	 */
	public void setAcceptanceVoteVerdictDAO(
		AcceptanceVoteVerdictDAO acceptanceVoteVerdictDAO) {
		this.acceptanceVoteVerdictDAO = acceptanceVoteVerdictDAO;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(
		com.tysanclan.site.projectewok.beans.UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param regulationDAO the regulationDAO to set
	 */
	public void setRegulationDAO(RegulationDAO regulationDAO) {
		this.regulationDAO = regulationDAO;
	}

	/**
	 * @param regulationChangeDAO the regulationChangeDAO to set
	 */
	public void setRegulationChangeDAO(
		RegulationChangeDAO regulationChangeDAO) {
		this.regulationChangeDAO = regulationChangeDAO;
	}

	/**
	 * @param untenabilityVoteChoiceDAO the untenabilityVoteChoiceDAO to set
	 */
	public void setUntenabilityVoteChoiceDAO(
		UntenabilityVoteChoiceDAO untenabilityVoteChoiceDAO) {
		this.untenabilityVoteChoiceDAO = untenabilityVoteChoiceDAO;
	}

	/**
	 * @param untenabilityVoteDAO the untenabilityVoteDAO to set
	 */
	public void setUntenabilityVoteDAO(
		UntenabilityVoteDAO untenabilityVoteDAO) {
		this.untenabilityVoteDAO = untenabilityVoteDAO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void removeAcceptanceVotes(User user) {
		AcceptanceVoteFilter filter = new AcceptanceVoteFilter();
		filter.trialMember(user);

		Seq<AcceptanceVote> votes = acceptanceVoteDAO.findByFilter(filter);
		for (AcceptanceVote vote : votes) {
			acceptanceVoteDAO.delete(vote);
		}
	}

	@Override
	public void resetSenateElectionIfUserIsParticipating(User user) {
		SenateElection election = getCurrentSenateElection();
		if (election != null) {
			if (election.isNominationOpen() && election.getCandidates()
													   .contains(user)) {
				election.getCandidates().remove(user);

			} else if (election.getCandidates().contains(user)) {
				// Reset election to nomination period
				resetSenateElection(election, Messages.Notification.RESTART_SENATE_DUE_TO_TERMINATION, Messages.Log.RESTART_SENATE_DUE_TO_TERMINATION);
			}

			senateElectionDAO.update(election);
		}
	}

	protected void resetSenateElection(SenateElection election, String notificationMessage, String logMessage) {
		election.setStart(new Date());
		for (CompoundVote vote : election.getVotes()) {
			for (CompoundVoteChoice choice : vote.getChoices()) {
				compoundVoteChoiceDAO.delete(choice);
			}
			compoundVoteDAO.delete(vote);

			notificationService.notifyUser(vote.getCaster(),
										   notificationMessage);

		}

		logService.logSystemAction("Democracy",
								   logMessage);
	}

	@Override
	public void resetChancellorElectionIfUserIsParticipating(User user) {
		ChancellorElection election = getCurrentChancellorElection();
		if (election != null) {
			if (election.isNominationOpen() && election.getCandidates()
													   .contains(user)) {
				election.getCandidates().remove(user);

			} else if (election.getCandidates().contains(user)) {
				// Reset election to nomination period
				election.setStart(new Date());
				for (CompoundVote vote : election.getVotes()) {
					for (CompoundVoteChoice choice : vote.getChoices()) {
						compoundVoteChoiceDAO.delete(choice);
					}
					compoundVoteDAO.delete(vote);

					notificationService.notifyUser(vote.getCaster(),
												   "The Chancellor election was restarted due to a candidate's membership being terminated. You will need to vote again in a week");

				}

				logService.logSystemAction("Democracy",
										   "Chancellor election restarted due to candidate membership termination");
			}

			chancellorElectionDAO.update(election);
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#isEligibleChancellorCandidate(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isEligibleChancellorCandidate(User _user) {
		return userDAO.load(_user.getId()).map(user -> {

			if (user.isRetired()) {
				return false;
			}

			if (user.getEndorsedBy().size()
				>= getRequiredChancellorEndorsements()) {
				return true;
			}

			Calendar cal = DateUtil.getCalendarInstance();
			cal.add(Calendar.MONTH, -6);

			DonationFilter filter = new DonationFilter();
			filter.donationTime().greaterThan(cal.getTime());
			filter.donator(user);

			BigDecimal value = BigDecimal.ZERO;
			Seq<Donation> donations = donationDAO.findByFilter(filter);
			for (Donation donation : donations) {
				value = value.add(donation.getAmount());
			}

			if (value.compareTo(new BigDecimal(30)) >= 0) {
				return true;
			}

			return false;
		}).getOrElse(false);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#isEligibleSenateCandidate(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isEligibleSenateCandidate(User _user) {
		return userDAO.load(_user.getId()).map(user -> {

			if (user.isRetired()) {
				return false;
			}

			if (user.getEndorsedForSenateBy().size()
				>= getRequiredSenateEndorsements()) {
				return true;
			}

			Calendar cal = DateUtil.getCalendarInstance();
			cal.add(Calendar.MONTH, -6);

			DonationFilter filter = new DonationFilter();
			filter.donationTime().greaterThan(cal.getTime());
			filter.donator(user);

			BigDecimal value = BigDecimal.ZERO;
			Seq<Donation> donations = donationDAO.findByFilter(filter);
			for (Donation donation : donations) {
				value = value.add(donation.getAmount());
			}

			if (value.compareTo(new BigDecimal(20)) > 0) {
				return true;
			}

			return false;
		}).getOrElse(false);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createChancellorElection()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ChancellorElection createChancellorElection() {
		ChancellorElection election = new ChancellorElection();
		election.setStart(DateUtil.getCalendarInstance().getTime());

		chancellorElectionDAO.save(election);

		logService.logSystemAction("Democracy", "Chancellor election started");

		return election;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createGroupLeaderElection(com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GroupLeaderElection createGroupLeaderElection(Group group) {
		GroupLeaderElection election = new GroupLeaderElection();
		election.setStart(DateUtil.getCalendarInstance().getTime());
		election.setGroup(group);
		groupLeaderElectionDAO.save(election);

		logService.logSystemAction("Groups",
								   "Group leader election for group " + group.getName()
									   + " started");

		return election;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createSenateElection()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SenateElection createSenateElection() {

		UserFilter filter = new UserFilter();
		filter.rank(Rank.CHANCELLOR);
		filter.orRank(Rank.TRUTHSAYER);
		filter.orRank(Rank.SENATOR);
		filter.orRank(Rank.REVERED_MEMBER);
		filter.orRank(Rank.SENIOR_MEMBER);
		filter.orRank(Rank.FULL_MEMBER);
		filter.orRank(Rank.JUNIOR_MEMBER);
		filter.orRank(Rank.TRIAL);
		filter.retired(false);

		long memberCount = userDAO.countByFilter(filter);
		int seats = calculateSenateSeats(memberCount);

		SenateElection election = new SenateElection();
		election.setStart(DateUtil.getCalendarInstance().getTime());
		election.setSeats(seats);

		senateElectionDAO.save(election);

		logService.logSystemAction("Democracy",
								   "Senate election started with " + seats + " available seats");

		return election;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#resolveChancellorElection(com.tysanclan.site.projectewok.entities.ChancellorElection)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveChancellorElection(ChancellorElection _election) {
		chancellorElectionDAO.load(_election.getId()).forEach(election -> {

			Map<User, Integer> scores = determineUserScoreTotals(election);

			Set<User> winners = determineBordaWinners(scores, 1);
			if (winners.isEmpty()) {
				// Restart election
				election.setStart(new Date());
				chancellorElectionDAO.update(election);

				logService.logSystemAction("Democracy",
										   "Chancellor election restarted due to lack of winners");
			} else {
				election.setWinner(winners.iterator().next());
				chancellorElectionDAO.update(election);

				UserFilter filter = new UserFilter();
				filter.rank(Rank.CHANCELLOR);

				Seq<User> chancellors = userDAO.findByFilter(filter);
				// Should only be 0 or 1, but hey, who knows!
				for (User chancellor : chancellors) {
					chancellor.setRank(MemberUtil
										   .determineRankByJoinDate(chancellor.getJoinDate()));
					if (!election.getWinner().equals(chancellor)) {
						logService.logUserAction(chancellor, "Election",
												 "Has not been reelected as Chancellor, and has assumed the rank of "
													 + chancellor.getRank().toString());
						notificationService.notifyUser(chancellor,
													   "You were not reelected as Chancellor");
					}

					userDAO.update(chancellor);

				}

				election.getWinner().setRank(Rank.CHANCELLOR);
				userDAO.update(election.getWinner());

				if (chancellors.contains(election.getWinner())) {
					logService.logUserAction(election.getWinner(), "Election",
											 "Has been reelected as Chancellor");
					notificationService.notifyUser(election.getWinner(),
												   "You were reelected as Chancellor");
				} else {
					logService.logUserAction(election.getWinner(), "Election",
											 "Has been elected as Chancellor");
					notificationService.notifyUser(election.getWinner(),
												   "You were elected as Chancellor");
				}
			}
		});

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#resolveGroupLeaderElection(com.tysanclan.site.projectewok.entities.GroupLeaderElection)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveGroupLeaderElection(GroupLeaderElection _election) {
		groupLeaderElectionDAO.load(_election.getId()).forEach(election -> {

			Map<User, Integer> scores = determineUserScoreTotals(election);

			Set<User> winners = determineBordaWinners(scores, 1);

			if (winners.isEmpty()) {
				// Restart election
				election.setStart(new Date());
				groupLeaderElectionDAO.update(election);

				logService.logSystemAction("Democracy",
										   "Group leader election for " + election.getGroup()
																				  .getName()
											   + " restarted due to lack of winners");
			} else {
				election.setWinner(winners.iterator().next());

				groupLeaderElectionDAO.update(election);

				election.getGroup().setLeader(election.getWinner());

				logService.logUserAction(election.getWinner(), "Election",
										 "Has become the new leader of " + election.getGroup()
																				   .getName());
				notificationService.notifyUser(election.getWinner(),
											   "You were elected as leader of " + election.getGroup()
																						  .getName());

				groupDAO.update(election.getGroup());
			}
		});
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected Set<User> determineBordaWinners(final Map<User, Integer> scores,
											  int amount) {
		List<User> winners = new ArrayList<User>(scores.size());
		Set<User> result = new HashSet<User>();

		winners.addAll(scores.keySet());

		winners.sort((o1, o2) -> {

			int diff = scores.get(o2).compareTo(scores.get(o1));

			if (diff == 0) {
				return o1.getJoinDate().compareTo(o2.getJoinDate());
			}

			return diff;
		});

		for (int i = 0; i < amount; i++) {
			if (i < winners.size()) {
				result.add(winners.get(i));
			}
		}

		return result;
	}

	/**
	 * Counts the total score gained by each candidate in an election
	 *
	 * @param election The election to count the score for
	 * @return A data structure mapping each user to his total score
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	protected Map<User, Integer> determineUserScoreTotals(Election election) {
		// Election election = electionDAO.load(_election
		// .getId());

		Set<CompoundVote> votes = election.getVotes();
		Map<User, Integer> scores = new HashMap<User, Integer>();

		for (CompoundVote vote : votes) {
			for (CompoundVoteChoice choice : vote.getChoices()) {
				User votesFor = choice.getVotesFor();

				if (!election.getEligibility().isEligible(votesFor)) {
					continue;
				}

				int score = choice.getScore();
				if (!scores.containsKey(votesFor)) {
					scores.put(votesFor, score);
				} else {
					scores.put(votesFor, score + scores.get(votesFor));
				}

			}
		}
		return scores;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#resolveSenateElection(com.tysanclan.site.projectewok.entities.SenateElection)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveSenateElection(SenateElection _election) {
		senateElectionDAO.load(_election.getId()).forEach(election -> {

			if (!election.getWinners().isEmpty())
				return;

			int seats = election.getSeats();
			int candidateCount = election.getCandidates().size();

			if (seats >= candidateCount) {
				seats = Math.max(2, candidateCount - 1);

				logService.logSystemAction("Democracy",
										   "Available Senate seats adjusted to " + seats
											   + " due to lack of candidates");
			}

			Map<User, Integer> scores = determineUserScoreTotals(election);

			Set<User> winners = determineBordaWinners(scores, seats);

			if (!winners.isEmpty()) {
				StringBuilder winnerString = new StringBuilder();
				winnerString.append("New Senate: ");
				int i = 0;
				for (User winner : winners) {
					if (i++ > 0) {
						winnerString.append(", ");
					}

					winnerString.append(winner.getUsername());
				}

				election.setWinners(winners);
				senateElectionDAO.update(election);

				UserFilter filter = new UserFilter();
				filter.rank(Rank.SENATOR);

				Seq<User> senators = userDAO.findByFilter(filter);
				for (User senator : senators) {
					senator.setRank(MemberUtil
										.determineRankByJoinDate(senator.getJoinDate()));
					if (!winners.contains(senator)) {
						logService.logUserAction(senator, "Election",
												 "Has not been reelected as Senator, and has assumed the rank of "
													 + senator.getRank().toString());
						notificationService.notifyUser(senator,
													   "You were not reelected as Senator");
					} else {
						logService.logUserAction(senator, "Election",
												 "Has been reelected as Senator");
						notificationService.notifyUser(senator,
													   "You were reelected as Senator");
					}

					userDAO.update(senator);
				}

				for (User senator : election.getWinners()) {
					senator.setRank(Rank.SENATOR);
					userDAO.update(senator);

					if (!senators.contains(senator)) {
						logService.logUserAction(senator, "Election",
												 "Has been elected as Senator");
						notificationService.notifyUser(senator,
													   "You were elected as Senator");
					}
				}

				logService.logSystemAction("Election", winnerString.toString());
			} else {
				election.setStart(new Date());
				senateElectionDAO.update(election);

				logService.logSystemAction("Democracy",
										   "Senate election restarted due to lack of winners");
			}
		});

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkAcceptanceVote(User user) {
		userDAO.load(user.getId()).forEach(_user -> {
			AcceptanceVoteFilter filter = new AcceptanceVoteFilter();
			filter.trialMember(_user);

			long count = acceptanceVoteDAO.countByFilter(filter);

			if (count == 0) {
				AcceptanceVote acceptanceVote = new AcceptanceVote();
				acceptanceVote.setTrialMember(_user);
				acceptanceVote.setStart(new Date());
				acceptanceVoteDAO.save(acceptanceVote);

				logService.logUserAction(_user, "Membership",
										 "Acceptance vote has started");
				notificationService
					.notifyUser(_user, "Your acceptance vote has started");
			}
		});

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveAcceptanceVote(AcceptanceVote _vote) {
		acceptanceVoteDAO.load(_vote.getId()).forEach(vote -> {

			User user = vote.getTrialMember();

			int inFavor = 0, total = 0;

			for (AcceptanceVoteVerdict verdict : vote.getVerdicts()) {
				if (MemberUtil.isMember(verdict.getCaster())) {
					if (verdict.isInFavor())
						inFavor++;
					total++;
				}
			}

			Integer factor = total > 0 ? (100 * inFavor) / total : null;

			boolean accepted = factor != null
				&& factor >= MEMBERSHIP_ACCEPTANCE_VOTE_PERCENTAGE_REQUIRED;

			String body = mailService
				.getAcceptanceVoteNotificationMail(user, accepted, inFavor,
												   total);

			mailService.sendHTMLMail(user.getEMail(),
									 "Your Tysan Clan trial period is over", body);

			user.setRank(accepted ? Rank.JUNIOR_MEMBER : Rank.FORUM);

			if (accepted) {
				notificationService
					.notifyUser(user, "You are now a Junior Member");

				dispatcher.dispatchEvent(new MemberStatusEvent(
					com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType.MEMBERSHIP_GRANTED,
					user));

			} else {
				dispatcher.dispatchEvent(new MemberStatusEvent(
					com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType.MEMBERSHIP_DENIED,
					user));
			}

			user.setMentor(null);

			userDAO.update(user);

			acceptanceVoteDAO.delete(vote);

			logService.logUserAction(user, "Membership",
									 "User has " + (accepted ? "passed" : "failed")
										 + " his or her acceptance vote (" + (
										 factor != null ? factor : 0) + "%)");
		});

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveJoinApplication(JoinApplication _application) {
		joinApplicationDAO.load(_application.getId()).forEach(application -> {
			if (application != null) {
				int inFavor = 0, total = 0;
				for (JoinVerdict verdict : application.getVerdicts()) {
					if (verdict.isInFavor()) {
						inFavor++;
					}
					total++;
				}

				final User mentor = application.getMentor();
				final User applicant = application.getApplicant();
				Date startDate = application.getStartDate();
				LocalDate start = new LocalDate(startDate);
				LocalDate now = LocalDate.now();

				final boolean have3DaysPassed = start.plusDays(3).isBefore(now);

				boolean accepted = false;

				if (have3DaysPassed) {
					if (applicant.getActivations().isEmpty()) {
						// If we managed to let 3 days pass without the member
						// getting
						// accepted,
						// check for mentor is no longer relevant, all we need to
						// know
						// is if a Senator
						// voted against
						if (total == 0) {
							accepted = true;
						} else {
							if (inFavor == 0) {
								accepted = false;
							} else {
								accepted = true;
							}
						}
					} else {
						accepted = false;
					}
				} else {
					// This really shouldn't happen, but don't resolve unactivated
					// users prior to 3 days
					if (!applicant.getActivations().isEmpty()) {
						return;
					}

					// If this method gets invoked earlier, however, then member
					// needs a mentor and 1
					// vote in favor - otherwise do not resolve
					if (inFavor > 0 && mentor != null) {
						accepted = true;
					} else {
						return;
					}
				}

				if (accepted) {
					applicant.setMentor(application.getMentor());
					applicant.setRank(Rank.TRIAL);
					applicant.setJoinDate(new Date());
					applicant.setLoginCount(0);
					applicant.setLastAction(new Date());
					userDAO.update(applicant);

				}

				joinVerdictDAO.deleteForApplication(application);
				joinApplicationDAO.delete(application);

				mailService.sendHTMLMail(applicant.getEMail(),
										 "Result of your Tysan Clan Application", mailService
											 .getJoinApplicationMail(application, accepted,
																	 inFavor, total));

				logService.logUserAction(applicant, "Membership",
										 "User has " + (accepted ? "been" : "not been")
											 + " granted a trial membership");

				if (accepted) {
					notificationService.notifyUser(applicant,
												   "You are now a Trial Member");

					dispatcher.dispatchEvent(new MemberStatusEvent(
						com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType.TRIAL_GRANTED,
						applicant));
				} else {
					dispatcher.dispatchEvent(new MemberStatusEvent(
						com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType.TRIAL_DENIED,
						applicant));
				}
			}
		});

	}

	/**
	 * @param chancellorElectionDAO the chancellorElectionDAO to set
	 */
	public void setChancellorElectionDAO(
		ChancellorElectionDAO chancellorElectionDAO) {
		this.chancellorElectionDAO = chancellorElectionDAO;
	}

	/**
	 * @param senateElectionDAO the senateElectionDAO to set
	 */
	public void setSenateElectionDAO(SenateElectionDAO senateElectionDAO) {
		this.senateElectionDAO = senateElectionDAO;
	}

	/**
	 * @param groupLeaderElectionDAO the groupLeaderElectionDAO to set
	 */
	public void setGroupLeaderElectionDAO(
		GroupLeaderElectionDAO groupLeaderElectionDAO) {
		this.groupLeaderElectionDAO = groupLeaderElectionDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#getCurrentChancellorElection()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public ChancellorElection getCurrentChancellorElection() {
		ChancellorElectionFilter filter = new ChancellorElectionFilter();
		Calendar twoWeeksAgo = DateUtil.getCalendarInstance();
		twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);

		filter.start().between(twoWeeksAgo.getTime(), new Date());

		Seq<ChancellorElection> elections = chancellorElectionDAO
			.findByFilter(filter);

		if (elections.size() > 0) {
			return elections.get(0);
		}

		return null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#getCurrentSenateElection()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public SenateElection getCurrentSenateElection() {
		SenateElectionFilter filter = new SenateElectionFilter();
		Calendar twoWeeksAgo = DateUtil.getCalendarInstance();
		twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);

		filter.start().between(twoWeeksAgo.getTime(), new Date());

		Seq<SenateElection> elections = senateElectionDAO.findByFilter(filter);

		if (elections.size() > 0) {
			return elections.get(0);
		}

		return null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#isElectionCandidate(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public boolean isElectionCandidate(User user) {
		SenateElection sElection = getCurrentSenateElection();

		boolean res = false;

		if (sElection != null) {
			res |= sElection.getCandidates().contains(user);
		}

		ChancellorElection cElection = getCurrentChancellorElection();

		if (cElection != null) {
			res |= cElection.getCandidates().contains(user);
		}

		return res;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#getRequiredChancellorEndorsements()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public long getRequiredChancellorEndorsements() {
		long memberCount = userService.countMembers();
		long required = (5 * memberCount) / 100;

		if (required == 0) {
			required = 1;
		}

		return required;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#getRequiredSenateEndorsements()
	 */
	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public long getRequiredSenateEndorsements() {
		long memberCount = userService.countMembers();
		long required = (3 * memberCount) / 100;

		if (required == 0) {
			required = 1;
		}

		return required;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void endorseUserForChancellor(User endorser, User target) {
		userDAO.load(endorser.getId()).forEach(_endorser -> {
			User _target = target != null ?
				userDAO.load(target.getId()).getOrNull() :
				null;

			User _current = _endorser.getEndorses();

			if (_current != null) {
				// Can not change endorsements of running candidates
				if (isElectionCandidate(_current)) {
					return;
				}
			}

			if (MemberUtil.canUserGrantEndorsement(_endorser) && (
				_target == null || MemberUtil
					.canUserGrantEndorsement(target))) {
				_endorser.setEndorses(_target);
				userDAO.update(_endorser);

				if (_target != null) {
					logService.logUserAction(_endorser, "Democracy",
											 "User has endorsed " + _target.getUsername()
												 + " for Chancellor");
					notificationService.notifyUser(_target,
												   "You are endorsed for Chancellor by " + _endorser
													   .getUsername());
				} else {
					logService.logUserAction(_endorser, "Democracy",
											 "User is no longer endorsing any member for Chancellor");
				}
			}
		});

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void endorseUserForSenate(User endorser, User target) {
		userDAO.load(endorser.getId()).forEach(_endorser -> {
			User _target = target != null ?
				userDAO.load(target.getId()).getOrNull() :
				null;

			User _current = _endorser.getEndorsesForSenate();

			if (_current != null) {
				// Can not change endorsements of running candidates
				if (isElectionCandidate(_current)) {
					return;
				}
			}

			if (MemberUtil.canUserGrantEndorsement(_endorser) && (
				_target == null || MemberUtil
					.canUserGrantEndorsement(target))) {
				_endorser.setEndorsesForSenate(_target);
				userDAO.update(_endorser);

				if (_target != null) {
					logService.logUserAction(_endorser, "Democracy",
											 "User has endorsed " + _target.getUsername()
												 + " for Senator");
					notificationService.notifyUser(_target,
												   "You are endorsed for Senator by " + _endorser
													   .getUsername());
				} else {
					logService.logUserAction(_endorser, "Democracy",
											 "User is no longer endorsing any member for Senator");
				}
			}
		});

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#addChancellorCandidate(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean addChancellorCandidate(User user) {
		ChancellorElection election = getCurrentChancellorElection();

		if (!user.isRetired() && election != null) {
			if (user.getEndorsedBy().size()
				>= getRequiredChancellorEndorsements() || user
				.hasDonatedAtLeast(new BigDecimal(30))) {
				if (!isElectionCandidate(user) && election.isNominationOpen()) {
					election.getCandidates().add(user);
					chancellorElectionDAO.update(election);

					logService.logUserAction(user, "Democracy",
											 "Has decided to run for Chancellor!");

					return true;
				}
			}
		}

		return false;

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#addSenateCandidate(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean addSenateCandidate(User user) {
		SenateElection election = getCurrentSenateElection();

		if (!user.isRetired() && election != null) {
			if (user.getEndorsedForSenateBy().size()
				>= getRequiredSenateEndorsements() || user
				.hasDonatedAtLeast(new BigDecimal(20))) {
				if (!isElectionCandidate(user) && election.isNominationOpen()) {
					election.getCandidates().add(user);
					senateElectionDAO.update(election);

					logService.logUserAction(user, "Democracy",
											 "Has decided to run for Senator!");

					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#addGroupLeaderCandidate(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.GroupLeaderElection)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean addGroupLeaderCandidate(User user,
										   GroupLeaderElection election) {
		return groupLeaderElectionDAO.load(election.getId())
									 .map(_election -> userDAO.load(user.getId()).map(_user -> {

										 if (_election != null) {
											 if (!_election.getCandidates().contains(_user)
												 && _election.isNominationOpen()) {
												 _election.getCandidates().add(_user);
												 groupLeaderElectionDAO.update(_election);
												 groupLeaderElectionDAO.flush();

												 return true;
											 }
										 }

										 return false;
									 }).getOrElse(false)).getOrElse(false);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createVote(com.tysanclan.site.projectewok.entities.User,
	 * java.util.List, com.tysanclan.site.projectewok.entities.Election)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CompoundVote createVote(User caster, List<User> votesFor,
								   Election election) {
		CompoundVote vote = new CompoundVote();
		vote.setCaster(caster);
		vote.setElection(election);
		compoundVoteDAO.save(vote);

		int score = votesFor.size() - 1;

		for (User user : votesFor) {
			CompoundVoteChoice choice = new CompoundVoteChoice();
			choice.setCompoundVote(vote);
			choice.setVotesFor(user);
			choice.setScore(score--);
			compoundVoteChoiceDAO.save(choice);

		}
		compoundVoteDAO.update(vote);

		return vote;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#castAcceptanceVote(com.tysanclan.site.projectewok.entities.AcceptanceVote,
	 * com.tysanclan.site.projectewok.entities.User, boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void castAcceptanceVote(AcceptanceVote vote, User caster,
								   boolean inFavor) {
		acceptanceVoteDAO.load(vote.getId()).forEach(
			_vote -> userDAO.load(caster.getId()).forEach(_caster -> {
				AcceptanceVoteVerdict myVerdict = null;

				for (AcceptanceVoteVerdict v : _vote.getVerdicts()) {
					if (v.getCaster().equals(_caster)) {
						myVerdict = v;
						break;
					}
				}

				if (myVerdict == null) {
					myVerdict = new AcceptanceVoteVerdict();
					myVerdict.setCaster(_caster);
					myVerdict.setVote(_vote);
					myVerdict.setInFavor(inFavor);

					acceptanceVoteVerdictDAO.save(myVerdict);
					acceptanceVoteDAO.evict(_vote);

				} else {
					myVerdict.setInFavor(inFavor);
					acceptanceVoteVerdictDAO.update(myVerdict);
				}
			}));

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createImpeachmentVote(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void createImpeachmentVote(User initiator) {
		UserFilter filter = new UserFilter();
		filter.rank(Rank.CHANCELLOR);
		Seq<User> chancellors = userDAO.findByFilter(filter);
		if (!chancellors.isEmpty()) {
			User chancellor = chancellors.get(0);

			Impeachment impeachment = new Impeachment();
			impeachment.setChancellor(chancellor);
			impeachment.setInitiator(initiator);
			impeachment.setStart(new Date());

			impeachmentDAO.save(impeachment);

			logService.logUserAction(initiator, "Democracy",
									 "An impeachment vote against the Chancellor has been started");
			notificationService.notifyUser(chancellor,
										   "An impeachment vote against you was started by "
											   + initiator.getUsername());

		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#castImpeachmentVote(com.tysanclan.site.projectewok.entities.User,
	 * boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void castImpeachmentVote(User voter, boolean inFavor) {
		userDAO.load(voter.getId()).forEach(_voter -> {
			Seq<Impeachment> impeachments = impeachmentDAO.findAll();

			if (!impeachments.isEmpty()) {
				Impeachment impeachment = impeachments.get(0);

				ImpeachmentVote vote = null;

				for (ImpeachmentVote next : impeachment.getVotes()) {
					if (next.getCaster().equals(_voter)) {
						vote = next;
						break;
					}
				}

				if (vote == null) {
					vote = new ImpeachmentVote();
					vote.setImpeachment(impeachment);
					vote.setInFavor(inFavor);
					vote.setCaster(_voter);

					impeachmentVoteDAO.save(vote);
				} else {
					vote.setInFavor(inFavor);

					impeachmentVoteDAO.update(vote);
				}
			}
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#resolveImpeachment()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveImpeachment() {
		Seq<Impeachment> impeachments = impeachmentDAO.findAll();

		if (!impeachments.isEmpty()) {
			Calendar calendar = DateUtil.getCalendarInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -4);

			Impeachment impeachment = impeachments.get(0);

			if (impeachment.getStart().before(calendar.getTime())) {

				int inFavor = 0;
				int total = impeachment.getVotes().size();

				User chancellor = impeachment.getChancellor();

				for (ImpeachmentVote vote : impeachment.getVotes()) {
					if (vote.isInFavor()) {
						inFavor++;
					}
				}

				int percentage = (100 * inFavor) / total;

				if (percentage >= 66) {
					notificationService
						.notifyUser(chancellor, "You have been impeached");

					logService.logUserAction(chancellor, "Democracy",
											 "Chancellor has been impeached (" + percentage
												 + ")");

					chancellor.setRank(MemberUtil
										   .determineRankByJoinDate(chancellor.getJoinDate()));
					userDAO.update(chancellor);
				}

				impeachmentDAO.delete(impeachment);
			}
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#stepDownAsChancellor(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void stepDownAsChancellor(User chancellor) {
		userDAO.load(chancellor.getId()).forEach(user -> {
			user.setRank(
				MemberUtil.determineRankByJoinDate(user.getJoinDate()));

			logService.logUserAction(user, "Democracy",
									 "User has stepped down as Chancellor");

			userDAO.update(user);
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#stepDownAsSenator(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void stepDownAsSenator(User senator) {
		userDAO.load(senator.getId()).forEach(user -> {
			user.setRank(
				MemberUtil.determineRankByJoinDate(user.getJoinDate()));

			logService.logUserAction(user, "Democracy",
									 "User has stepped down as Senator");

			userDAO.update(user);
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#stepDownAsTruthsayer(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void stepDownAsTruthsayer(User truthsayer) {
		userDAO.load(truthsayer.getId()).forEach(user -> {

			user.setRank(
				MemberUtil.determineRankByJoinDate(user.getJoinDate()));

			logService.logUserAction(user, "Democracy",
									 "User has stepped down as Truthsayer");

			userDAO.update(user);
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#castUntenabilityVote(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.UntenabilityVote, boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void castUntenabilityVote(User user, UntenabilityVote vote,
									 boolean inFavor) {
		untenabilityVoteDAO.load(vote.getId())
						   .forEach(_vote -> userDAO.load(user.getId()).forEach(_user -> {

							   UntenabilityVoteChoice myChoice = null;

							   for (UntenabilityVoteChoice choice : _vote.getChoices()) {
								   if (choice.getCaster().equals(_user)) {
									   myChoice = choice;
									   break;
								   }
							   }

							   if (myChoice == null) {
								   myChoice = new UntenabilityVoteChoice();
								   myChoice.setCaster(_user);
								   myChoice.setInFavor(inFavor);
								   myChoice.setVote(_vote);

								   untenabilityVoteChoiceDAO.save(myChoice);
							   } else {
								   myChoice.setInFavor(inFavor);
								   untenabilityVoteChoiceDAO.update(myChoice);
							   }
						   }));
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createUntenabilityVote(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Regulation)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void createUntenabilityVote(User user, Regulation regulation) {
		regulationDAO.load(regulation.getId()).forEach(
			_regulation -> userDAO.load(user.getId()).forEach(_user -> {

				UntenabilityVoteFilter filter = new UntenabilityVoteFilter();
				filter.regulation(_regulation);

				if (untenabilityVoteDAO.countByFilter(filter) == 0) {
					UntenabilityVote vote = new UntenabilityVote();
					vote.setRegulation(_regulation);
					vote.setStart(new Date());

					untenabilityVoteDAO.save(vote);

					logService.logUserAction(_user, "Democracy",
											 "Regulation " + _regulation.getName()
												 + " declared untenable, vote started");
				}
			}));
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#resolveUntenabilityVote(com.tysanclan.site.projectewok.entities.UntenabilityVote)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveUntenabilityVote(UntenabilityVote vote) {
		untenabilityVoteDAO.load(vote.getId()).forEach(_vote -> {
			int inFavor = 0;
			int total = _vote.getChoices().size();

			Regulation regulation = _vote.getRegulation();

			for (UntenabilityVoteChoice choice : _vote.getChoices()) {
				if (choice.isInFavor()) {
					inFavor++;
				}
			}

			if (total > 0) {

				int percentage = (100 * inFavor) / total;

				if (percentage >= 51) {
					logService.logSystemAction("Democracy",
											   "Regulation " + regulation.getName()
												   + " was repealled by untenability vote ("
												   + percentage + "%)");

					regulationDAO.delete(regulation);

				} else {
					logService.logSystemAction("Democracy",
											   "Regulation " + regulation.getName()
												   + " was maintained by untenability vote ("
												   + percentage + "%)");
				}
			}

			untenabilityVoteDAO.delete(_vote);
		});

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createAddRegulationVote(com.tysanclan.site.projectewok.entities.User,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RegulationChange createAddRegulationVote(User user, String title,
													String description) {
		return userDAO.load(user.getId()).map(_user -> {

			RegulationChange change = new RegulationChange();
			change.setChangeType(ChangeType.ADD);
			change.setTitle(BBCodeUtil.stripTags(title));
			change.setDescription(BBCodeUtil.stripTags(description));
			change.setStart(new Date());
			change.setVeto(false);
			change.setRegulation(null);
			change.setProposer(_user);

			regulationChangeDAO.save(change);

			return change;
		}).getOrElseThrow(IllegalStateException::new);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createModifyRegulationVote(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Regulation,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RegulationChange createModifyRegulationVote(User user,
													   Regulation regulation, String newTitle, String newDescription) {
		return userDAO.load(user.getId()).flatMap(
			_user -> regulationDAO.load(regulation.getId())
								  .map(_regulation -> {

									  RegulationChange change = new RegulationChange();
									  change.setChangeType(ChangeType.MODIFY);
									  change.setTitle(BBCodeUtil.stripTags(newTitle));
									  change.setDescription(
										  BBCodeUtil.stripTags(newDescription));
									  change.setStart(new Date());
									  change.setVeto(false);
									  change.setRegulation(_regulation);
									  change.setProposer(_user);

									  regulationChangeDAO.save(change);

									  return change;
								  })).getOrElseThrow(IllegalStateException::new);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#createRepealRegulationVote(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Regulation)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RegulationChange createRepealRegulationVote(User user,
													   Regulation regulation) {
		return userDAO.load(user.getId()).flatMap(
			_user -> regulationDAO.load(regulation.getId())
								  .map(_regulation -> {

									  RegulationChange change = new RegulationChange();
									  change.setChangeType(ChangeType.REPEAL);
									  change.setVeto(false);
									  change.setStart(new Date());
									  change.setRegulation(_regulation);
									  change.setProposer(_user);

									  regulationChangeDAO.save(change);

									  return change;
								  })).getOrElseThrow(IllegalStateException::new);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#resolveRegulationVote(com.tysanclan.site.projectewok.entities.RegulationChange)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveRegulationVote(RegulationChange change) {
		regulationChangeDAO.load(change.getId()).forEach(_change -> {

			UserFilter filter = new UserFilter();
			filter.rank(Rank.SENATOR);

			long total = _change.isVeto() ?
				userDAO.countByFilter(filter) :
				_change.getVotes().size();
			int required_factor = _change.isVeto() ? 66 : 51;

			int inFavor = 0;

			for (RegulationChangeVote vote : _change.getVotes()) {
				if (vote.isInFavor()) {
					inFavor++;
				}
			}

			long factor = (100 * inFavor) / total;

			if (factor >= required_factor) {
				// Execute regulation change
				switch (_change.getChangeType()) {
					case ADD:
						createRegulation(_change, factor);
						break;
					case MODIFY:
						modifyRegulation(_change, factor);
						break;
					case REPEAL:
						repealRegulation(_change, factor);
						break;
				}
			} else {
				switch (_change.getChangeType()) {
					case ADD:
						logService.logSystemAction("Regulations",
												   "The proposed regulation " + _change.getTitle()
													   + " did not pass (" + factor + "%)");
						break;
					case MODIFY:
						logService.logSystemAction("Regulations",
												   "The proposed modifications to regulation "
													   + _change.getTitle() + " did not pass ("
													   + factor + "%)");
						break;
					case REPEAL:
						logService.logSystemAction("Regulations",
												   "The proposed repeal of regulation " + _change
													   .getTitle() + " did not pass (" + factor
													   + "%)");
						break;
				}
			}

			for (RegulationChangeVote rcv : _change.getVotes()) {
				regulationChangeVoteDAO.delete(rcv);
			}

			regulationChangeDAO.delete(_change);
		});

	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void repealRegulation(RegulationChange _change, long factor) {
		Regulation regulation = _change.getRegulation();

		logService.logSystemAction("Regulations",
								   "The Senate has voted to repeal the Regulation named "
									   + regulation.getName() + " (" + factor + "%)");
		regulationDAO.delete(regulation);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void modifyRegulation(RegulationChange _change, long factor) {
		Regulation regulation = _change.getRegulation();

		logService.logSystemAction("Regulations",
								   "The Senate has voted to modify the Regulation named "
									   + regulation.getName() + " (" + factor + "%)");

		regulation.setContents(_change.getDescription());
		regulation.setName(_change.getTitle());
		regulationDAO.update(regulation);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void createRegulation(RegulationChange _change, long factor) {
		Regulation regulation = new Regulation();

		logService.logSystemAction("Regulations",
								   "The Senate has voted to create a new Regulation named "
									   + _change.getTitle() + " (" + factor + "%)");

		regulation.setContents(_change.getDescription());
		regulation.setDrafter(_change.getProposer());
		regulation.setName(_change.getTitle());
		regulationDAO.save(regulation);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#voteForRegulation(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.RegulationChange, boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void voteForRegulation(User senator, RegulationChange change,
								  boolean inFavor) {
		regulationChangeDAO.load(change.getId()).forEach(_change -> {
			RegulationChangeVote myVote = null;

			for (RegulationChangeVote vote : _change.getVotes()) {
				if (vote.getSenator().equals(senator)) {
					myVote = vote;
				}
			}

			if (myVote == null) {
				myVote = new RegulationChangeVote();
				myVote.setInFavor(inFavor);
				myVote.setRegulationChange(_change);
				myVote.setSenator(senator);
				regulationChangeVoteDAO.save(myVote);
				regulationChangeDAO.evict(_change);
			} else {
				myVote.setInFavor(inFavor);
				regulationChangeVoteDAO.update(myVote);
			}
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.DemocracyService#vetoRegulationChange(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.RegulationChange)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void vetoRegulationChange(User chancellor, RegulationChange change) {
		regulationChangeDAO.load(change.getId()).forEach(_change -> {
			UserFilter filter = new UserFilter();
			filter.rank(Rank.SENATOR);

			String status;

			if (userDAO.countByFilter(filter) < 2 || _change.getProposer()
															.equals(chancellor)) {
				regulationChangeDAO.delete(_change);

				status = " was vetoed and automatically withdrawn";
			} else {
				_change.setVeto(true);

				regulationChangeDAO.update(_change);

				status = " was vetoed. A 66% Senate majority is now required";
			}

			switch (_change.getChangeType()) {
				case ADD:
					logService.logUserAction(chancellor, "Regulations",
											 "The proposed regulation " + _change.getTitle()
												 + status);
					break;
				case MODIFY:
					logService.logUserAction(chancellor, "Regulations",
											 "The proposed modifications to regulation "
												 + _change.getTitle() + status);
					break;
				case REPEAL:
					logService.logUserAction(chancellor, "Regulations",
											 "The proposed repeal of regulation " + _change
												 .getTitle() + status);
					break;
			}
		});

	}

	public static int calculateSenateSeats(long memberCount) {
		return (int) Math.max(2, 1 + (memberCount / 20));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkAcceptanceVotes() {

		for (User user : userDAO.getTrialMembersReadyForVote()) {
			checkAcceptanceVote(user);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveAcceptanceVotes() {
		Calendar threeDaysAgo = DateUtil.getCalendarInstance();

		threeDaysAgo.add(Calendar.DAY_OF_YEAR, -3);

		AcceptanceVoteFilter filter = new AcceptanceVoteFilter();
		filter.start().lessThanOrEqualTo(threeDaysAgo.getTime());

		Seq<AcceptanceVote> eligibles = acceptanceVoteDAO.findByFilter(filter);
		for (AcceptanceVote vote : eligibles) {
			resolveAcceptanceVote(vote);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkChancellorElections() {
		Calendar calendar = DateUtil.getCalendarInstance();
		calendar.add(Calendar.MONTH, -6);

		ChancellorElectionFilter electionsLessThanSixMonthsAgo = new ChancellorElectionFilter();
		electionsLessThanSixMonthsAgo.start().greaterThan(calendar.getTime());
		electionsLessThanSixMonthsAgo.start().orderBy(false);

		ChancellorElection current = getCurrentChancellorElection();

		UserFilter chancellorFilter = new UserFilter();
		chancellorFilter.rank(Rank.CHANCELLOR);

		boolean noChancellor = userDAO.countByFilter(chancellorFilter) == 0;
		boolean noElectionInPastSixMonths = chancellorElectionDAO
			.countByFilter(electionsLessThanSixMonthsAgo) == 0;
		boolean noElectionCurrently = current == null;

		if ((noElectionCurrently && noChancellor) || (noElectionInPastSixMonths
			&& noElectionCurrently)) {
			createChancellorElection();
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveChancellorElections() {
		ChancellorElectionFilter filter = new ChancellorElectionFilter();
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -2);
		filter.start().lessThan(cal.getTime());
		filter.winner().isNull();
		Seq<ChancellorElection> elections = chancellorElectionDAO
			.findByFilter(filter);
		for (ChancellorElection election : elections) {
			resolveChancellorElection(election);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkSenatorElections() {
		final SenateElection current = getCurrentSenateElection();

		if (current != null) {
			if (current.getCandidates().size() == 1) {
				LocalDate start = LocalDate.fromDateFields(current.getStart());
				LocalDate sevenDaysAgo = LocalDate.now().minusWeeks(1);

				if (start.isBefore(sevenDaysAgo)) {
					resetSenateElection(current, Messages.Notification.RESTART_SENATE_DUE_TO_CANDIDATE_LACK, Messages.Log.RESTART_SENATE_DUE_TO_CANDIDATE_LACK);
				}
			}

			return;
		}

		LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);

		SenateElectionFilter electionsMoreThanSixMonthsAgo = new SenateElectionFilter();
		electionsMoreThanSixMonthsAgo.start().lessThan(sixMonthsAgo.toDate());
		electionsMoreThanSixMonthsAgo.start().orderBy(false);

		SenateElectionFilter electionsLessThanSixMonthsAgo = new SenateElectionFilter();
		electionsLessThanSixMonthsAgo.start()
									 .greaterThan(sixMonthsAgo.toDate());
		electionsLessThanSixMonthsAgo.start().orderBy(false);

		final boolean thereWasAnElectionMoreThanSixMonthsAgo =
			senateElectionDAO.countByFilter(electionsMoreThanSixMonthsAgo)
				> 0;
		final boolean thereWasNoElectionLessThanSixMonthsAgo =
			senateElectionDAO.countByFilter(electionsLessThanSixMonthsAgo)
				== 0;
		final boolean thereHasNeverBeenAnElection =
			senateElectionDAO.countAll() == 0;
		if (thereHasNeverBeenAnElection || (
			thereWasAnElectionMoreThanSixMonthsAgo
				&& thereWasNoElectionLessThanSixMonthsAgo)) {
			if (thereHasNeverBeenAnElection) {
				logService.logSystemAction("Democracy",
										   "There has never been a Senate election");
			} else {
				logService.logSystemAction("Democracy",
										   "Last Senate election more than six months ago");
			}
			createSenateElection();
		} else {

			SenateElectionFilter lastSenateElectionFilter = new SenateElectionFilter();
			lastSenateElectionFilter.start().orderBy(false);
			Seq<SenateElection> elections = senateElectionDAO
				.findByFilter(lastSenateElectionFilter, 0, 1);

			final long senators = userDAO.countByRank(Rank.SENATOR);

			if (senators <= 1) {
				logService
					.logSystemAction("Democracy", "Only one Senator left");
				createSenateElection();
				return;
			}

			for (SenateElection election : elections) {
				final int seats = election.getSeats();

				if (seats > 0) {

					int fraction = (int) ((senators * 100) / seats);

					if (fraction < 40) {
						logService.logSystemAction("Democracy",
												   "Senate size less than 40% of last election's seats");
						createSenateElection();
					}
				}
				break;

			}
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveSenatorElections() {
		SenateElectionFilter filter = new SenateElectionFilter();
		Calendar twoWeeksAgo = DateUtil.getCalendarInstance();
		Calendar threeWeeksAgo = DateUtil.getCalendarInstance();
		twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);
		threeWeeksAgo.add(Calendar.WEEK_OF_YEAR, -3);
		filter.start().lessThan(twoWeeksAgo.getTime());
		filter.andStart().greaterThan(threeWeeksAgo.getTime());
		Seq<SenateElection> elections = senateElectionDAO.findByFilter(filter);
		for (SenateElection election : elections) {
			resolveSenateElection(election);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveGroupLeaderElections() {
		GroupLeaderElectionFilter filter = new GroupLeaderElectionFilter();
		Calendar twoWeeksAgo = DateUtil.getCalendarInstance();
		Calendar threeWeeksAgo = DateUtil.getCalendarInstance();
		twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);
		threeWeeksAgo.add(Calendar.WEEK_OF_YEAR, -3);
		filter.start().lessThan(twoWeeksAgo.getTime());
		filter.andStart().greaterThan(threeWeeksAgo.getTime());

		Seq<GroupLeaderElection> elections = groupLeaderElectionDAO
			.findByFilter(filter);
		for (GroupLeaderElection election : elections) {
			resolveGroupLeaderElection(election);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void resolveJoinApplications() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.DAY_OF_YEAR, -3);

		JoinApplicationFilter filter = new JoinApplicationFilter();
		filter.startDate().lessThan(cal.getTime());

		Seq<JoinApplication> applications = joinApplicationDAO
			.findByFilter(filter);
		for (JoinApplication application : applications) {
			resolveJoinApplication(application);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void resolveRegulationVotes() {
		RegulationChangeFilter filter = new RegulationChangeFilter();
		Calendar cal = DateUtil.getMidnightCalendarInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -1);
		filter.start().lessThan(cal.getTime());

		for (RegulationChange change : regulationChangeDAO
			.findByFilter(filter)) {
			resolveRegulationVote(change);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveUntenabilityVotes() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.WEEK_OF_YEAR, 1);

		UntenabilityVoteFilter filter = new UntenabilityVoteFilter();
		filter.start().lessThan(cal.getTime());

		Seq<UntenabilityVote> votes = untenabilityVoteDAO.findByFilter(filter);

		for (UntenabilityVote vote : votes) {
			resolveUntenabilityVote(vote);
		}

	}
}
