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
package com.tysanclan.site.projectewok.beans.impl;

import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.entities.Achievement;
import com.tysanclan.site.projectewok.entities.AchievementIcon;
import com.tysanclan.site.projectewok.entities.AchievementProposal;
import com.tysanclan.site.projectewok.entities.AchievementProposalVote;
import com.tysanclan.site.projectewok.entities.AchievementRequest;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.AchievementDAO;
import com.tysanclan.site.projectewok.entities.dao.AchievementIconDAO;
import com.tysanclan.site.projectewok.entities.dao.AchievementProposalDAO;
import com.tysanclan.site.projectewok.entities.dao.AchievementProposalVoteDAO;
import com.tysanclan.site.projectewok.entities.dao.AchievementRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.AchievementIconFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.AchievementProposalFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.HTMLSanitizer;
import com.tysanclan.site.projectewok.util.ImageUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen
 */
@Component
@Scope("request")
class AchievementServiceImpl implements AchievementService {
	private static final long serialVersionUID = 1L;

	@Autowired
	private AchievementDAO achievementDAO;

	@Autowired
	private AchievementProposalDAO achievementProposalDAO;

	@Autowired
	private AchievementProposalVoteDAO achievementProposalVoteDAO;

	@Autowired
	private AchievementRequestDAO achievementRequestDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.NotificationService notificationService;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private AchievementIconDAO achievementIconDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void approveProposal(AchievementProposal proposal, User senator) {
		if (senator.getRank() == Rank.SENATOR) {
			castVote(proposal, senator, true);
		}
		if (senator.getRank() == Rank.TRUTHSAYER) {
			proposal.setTruthsayerReviewed(true);
			achievementProposalDAO.update(proposal);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void rejectProposal(AchievementProposal proposal, User senator) {
		if (senator.getRank() == Rank.SENATOR) {
			castVote(proposal, senator, false);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void castVote(AchievementProposal proposal, User senator,
			boolean inFavor) {
		AchievementProposalVote vote = getCurrentVote(proposal, senator);

		if (vote == null) {
			vote = new AchievementProposalVote();
			vote.setInFavor(inFavor);
			vote.setProposal(proposal);
			vote.setSenator(senator);
			achievementProposalVoteDAO.save(vote);

			proposal.getApprovedBy().add(vote);
			achievementProposalDAO.update(proposal);
		} else {
			vote.setInFavor(inFavor);
			achievementProposalVoteDAO.update(vote);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private AchievementProposalVote getCurrentVote(
			AchievementProposal proposal, User senator) {
		AchievementProposalVote vote = null;

		for (AchievementProposalVote v : proposal.getApprovedBy()) {
			if (v.getSenator().equals(senator)) {
				vote = v;
				break;
			}
		}
		return vote;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AchievementProposal createProposal(User proposer, String name,
			String description, AchievementIcon icon, Game game, Group group) {
		if (icon.getApproved() != null && icon.getApproved().booleanValue()) {

			AchievementProposal proposal = new AchievementProposal();
			proposal.setSuggestor(proposer);
			proposal.setName(HTMLSanitizer.stripTags(name));
			proposal.setDescription(HTMLSanitizer.sanitize(description));
			proposal.setIcon(icon);
			proposal.setStartDate(new Date());
			proposal.setGame(game);
			proposal.setGroup(group);
			proposal.setTruthsayerReviewed(false);

			achievementProposalDAO.save(proposal);

			icon.setProposal(proposal);

			achievementIconDAO.update(icon);

			return proposal;
		}

		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AchievementRequest requestAchievement(User user,
			Achievement achievement, byte[] evidencePicture,
			String evidenceDescription) {
		AchievementRequest request = new AchievementRequest();
		request.setAchievement(achievement);
		request.setRequestedBy(user);
		request.setEvidenceDescription(evidenceDescription);
		request.setEvidencePicture(evidencePicture);

		achievementRequestDAO.save(request);

		return request;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void approveRequest(User truthsayer, AchievementRequest request) {
		Group group = request.getAchievement().getGroup();

		if (truthsayer.getRank() == Rank.TRUTHSAYER
				|| (group != null && truthsayer.equals(group.getLeader()))) {
			Achievement a = request.getAchievement();
			User recipient = request.getRequestedBy();

			a.getAchievedBy().add(recipient);

			notificationService.notifyUser(request.getRequestedBy(),
					"Request for achievement "
							+ request.getAchievement().getName()
							+ " has been approved");

			achievementDAO.update(a);
			achievementRequestDAO.delete(request);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void denyRequest(User truthsayer, AchievementRequest request) {
		Group group = request.getAchievement().getGroup();

		if (truthsayer.getRank() == Rank.TRUTHSAYER
				|| (group != null && truthsayer.equals(group.getLeader()))) {

			achievementRequestDAO.delete(request);

			notificationService.notifyUser(request.getRequestedBy(),
					"Request for achievement "
							+ request.getAchievement().getName()
							+ " has been denied");
		}
	}

	@Override
	public List<AchievementProposal> getProposedAchievements(User user) {
		AchievementProposalFilter filter = new AchievementProposalFilter();

		filter.setSuggestor(user);

		return achievementProposalDAO.findByFilter(filter);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void approveIcon(User truthsayer, AchievementIcon icon) {
		if (truthsayer != null && truthsayer.getRank() == Rank.TRUTHSAYER) {
			icon.setApproved(true);

			achievementIconDAO.update(icon);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void rejectAchievement(AchievementProposal proposal,
			Regulation regulation, boolean charter, User truthsayer) {
		if (charter || regulation != null) {

			if (truthsayer != null && truthsayer.getRank() == Rank.TRUTHSAYER) {
				proposal.getIcon().setProposal(null);
				achievementIconDAO.update(proposal.getIcon());

				achievementProposalDAO.delete(proposal);

				String reason = null;

				if (charter && regulation != null) {
					reason = " for incompatibility with the Charter and with regulation "
							+ regulation.getName();
				} else if (charter) {
					reason = " for incompatibility with the Charter";
				} else if (regulation == null) {
					// Never happens
					reason = " for no apparent reason";
				} else {
					reason = " for incompatibility with regulation "
							+ regulation.getName();
				}

				notificationService.notifyUser(proposal.getSuggestor(),
						"The Truthsayers have rejected your proposed achievement "
								+ proposal.getName() + reason);
				logService.logUserAction(
						truthsayer,
						"Achievements",
						"Has rejected proposed achievement "
								+ proposal.getName() + reason);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void rejectIcon(User truthsayer, AchievementIcon icon) {
		if (truthsayer != null && truthsayer.getRank() == Rank.TRUTHSAYER) {
			notificationService.notifyUser(icon.getCreator(),
					"One of your icons was rejected by the Truthsayers");

			icon.setApproved(false);

			achievementIconDAO.update(icon);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public AchievementIcon uploadAchievementIcon(User author, byte[] image,
			String purpose, Boolean privateIcon) {
		Dimension dim = ImageUtil.getImageDimensions(image);

		if (MemberUtil.isMember(author) && author.getRank() != Rank.TRIAL
				&& dim != null && ((int) dim.getHeight()) == 48
				&& ((int) dim.getWidth()) == 48) {
			AchievementIcon icon = new AchievementIcon();
			icon.setApproved(null);
			icon.setCreator(author);
			icon.setImage(image);
			icon.setPurpose(purpose);
			icon.setCreatorOnly(privateIcon);

			achievementIconDAO.save(icon);

			return icon;
		}

		return null;
	}

	@Override
	public List<AchievementIcon> getAvailableIcons(User user) {
		List<AchievementIcon> icons = new LinkedList<AchievementIcon>();
		addClassicIcons(icons);
		addMyIcons(user, icons);
		addOtherIcons(icons);

		return icons;
	}

	private void addOtherIcons(List<AchievementIcon> icons) {

		AchievementIconFilter filter = new AchievementIconFilter();
		filter.setUnclaimed(true);
		filter.setCreatorOnly(false);
		filter.setApproved(true);
		icons.addAll(achievementIconDAO.findByFilter(filter));
	}

	private void addMyIcons(User user, List<AchievementIcon> icons) {
		AchievementIconFilter filter = new AchievementIconFilter();
		filter.setUnclaimed(true);
		filter.setCreatorOnly(true);
		filter.setApproved(true);
		filter.setCreator(user);
		icons.addAll(achievementIconDAO.findByFilter(filter));
	}

	private void addClassicIcons(List<AchievementIcon> icons) {
		AchievementIconFilter filter = new AchievementIconFilter();
		filter.setUnclaimed(true);
		filter.setCreatorOnlyAsNull(true);
		filter.setApproved(true);
		icons.addAll(achievementIconDAO.findByFilter(filter));
	}

	@Override
	public void deleteIcon(User deleter, AchievementIcon icon) {
		if (icon.getCreator().equals(deleter)) {
			achievementIconDAO.delete(icon);
		}

	}

	public void setAchievementDAO(AchievementDAO achievementDAO) {
		this.achievementDAO = achievementDAO;
	}

	public void setAchievementProposalDAO(
			AchievementProposalDAO achievementProposalDAO) {
		this.achievementProposalDAO = achievementProposalDAO;
	}

	public void setAchievementRequestDAO(
			AchievementRequestDAO achievementRequestDAO) {
		this.achievementRequestDAO = achievementRequestDAO;
	}

	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	public void setNotificationService(
			com.tysanclan.site.projectewok.beans.NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public void setAchievementIconDAO(AchievementIconDAO achievementIconDAO) {
		this.achievementIconDAO = achievementIconDAO;
	}

	public void setAchievementProposalVoteDAO(
			AchievementProposalVoteDAO achievementProposalVoteDAO) {
		this.achievementProposalVoteDAO = achievementProposalVoteDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolvePendingProposals() {
		AchievementProposalFilter filter = new AchievementProposalFilter();

		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.DAY_OF_MONTH, -4);

		filter.setStartsBefore(cal.getTime());

		for (AchievementProposal proposal : achievementProposalDAO
				.findByFilter(filter)) {

			int senateSize = userDAO.countByRank(Rank.SENATOR);
			int totalVotes = proposal.getChancellorVeto() == null
					|| !proposal.getChancellorVeto().booleanValue() ? proposal
					.getApprovedBy().size() : senateSize;
			int inFavor = 0;

			for (AchievementProposalVote vote : proposal.getApprovedBy()) {
				if (vote.isInFavor()) {
					inFavor++;
				}
			}

			int requiredPercentage = proposal.getChancellorVeto() == null
					|| !proposal.getChancellorVeto().booleanValue() ? 51 : 66;

			AchievementIcon icon = proposal.getIcon();
			icon.setProposal(null);

			int perc = 100 * inFavor / totalVotes;
			if (perc >= requiredPercentage) {
				Achievement achievement = new Achievement();
				achievement.setDescription(proposal.getDescription());
				achievement.setGame(proposal.getGame());
				achievement.setGroup(proposal.getGroup());
				achievement.setIcon(icon);
				achievement.setName(proposal.getName());

				achievementDAO.save(achievement);

				icon.setAchievement(achievement);

				logService.logSystemAction("Achievements",
						"New achievement approved: " + proposal.getName());

				notificationService.notifyUser(
						proposal.getSuggestor(),
						"Your suggested achievement named "
								+ proposal.getName()
								+ " was approved by the Senate, with " + perc
								+ "% in favor");

				for (AchievementProposalVote vote : proposal.getApprovedBy()) {
					notificationService.notifyUser(vote.getSenator(),
							"Suggested achievement named " + proposal.getName()
									+ " was approved with " + perc
									+ "% in favor");
				}
			} else {
				notificationService.notifyUser(
						proposal.getSuggestor(),
						"Your suggested achievement named "
								+ proposal.getName()
								+ " was rejected by the Senate, with only "
								+ perc + "% of the required ("
								+ requiredPercentage + ") in favor");

				for (AchievementProposalVote vote : proposal.getApprovedBy()) {
					notificationService.notifyUser(vote.getSenator(),
							"Suggested achievement named " + proposal.getName()
									+ " was rejected with " + perc
									+ "% of the required ("
									+ requiredPercentage + ") in favor");
				}

				logService.logSystemAction("Achievements",
						"Proposed achievement " + proposal.getName()
								+ " was rejected with " + perc + "% in favor ");
			}

			achievementIconDAO.update(icon);

			achievementProposalDAO.delete(proposal);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void vetoProposal(User chancellor, AchievementProposal proposal) {
		if (chancellor != null && chancellor.getRank() == Rank.CHANCELLOR) {
			proposal.setChancellorVeto(true);
			achievementProposalDAO.update(proposal);

			logService.logUserAction(chancellor, "Achievements",
					"Has vetoed proposed achievement " + proposal.getName());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkProposal(User chancellor, AchievementProposal proposal) {
		if (chancellor != null && chancellor.getRank() == Rank.CHANCELLOR) {
			proposal.setChancellorVeto(false);
			achievementProposalDAO.update(proposal);

		}
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
