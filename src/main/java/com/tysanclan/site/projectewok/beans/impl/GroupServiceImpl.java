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

import com.google.common.collect.Lists;
import com.jeroensteenbeeke.hyperion.events.IEventDispatcher;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.Group.JoinPolicy;
import com.tysanclan.site.projectewok.entities.dao.GroupCreationRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.GroupDAO;
import com.tysanclan.site.projectewok.entities.dao.GroupForumDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.GroupCreationRequestFilter;
import com.tysanclan.site.projectewok.entities.filter.GroupForumFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.event.GroupWithoutLeaderEvent;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import io.vavr.collection.Seq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.events.EventException;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GroupServiceImpl
		implements com.tysanclan.site.projectewok.beans.GroupService {
	private static final Logger logger = LoggerFactory
			.getLogger(GroupServiceImpl.class);

	@Autowired
	private IEventDispatcher dispatcher;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private GroupForumDAO groupForumDAO;

	@Autowired
	private GroupCreationRequestDAO groupCreationRequestDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.NotificationService notificationService;

	public void setNotificationService(
			com.tysanclan.site.projectewok.beans.NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public void setDispatcher(IEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * @param groupForumDAO the groupForumDAO to set
	 */
	public void setGroupForumDAO(GroupForumDAO groupForumDAO) {
		this.groupForumDAO = groupForumDAO;
	}

	/**
	 * @param groupDAO the groupDAO to set
	 */
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void clearRequestedGroups(User user) {
		GroupCreationRequestFilter filter = new GroupCreationRequestFilter();
		filter.requester(user);

		Seq<GroupCreationRequest> requests = groupCreationRequestDAO
				.findByFilter(filter);
		for (GroupCreationRequest request : requests) {
			groupCreationRequestDAO.delete(request);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<Group> clearGroupLeaderStatus(User _user) {
		List<Group> leaderless = Lists.newArrayList();
		List<Group> groups = _user.getGroups();
		for (Group group : groups) {
			if (group.getLeader() != null && group.getLeader().equals(_user)) {
				group.setLeader(null);
				groupDAO.update(group);

				leaderless.add(group);

			}
		}

		return leaderless;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void clearGroupMemberships(User user) {
		for (Group group : groupDAO.findAll()) {
			if (group.getAppliedMembers().contains(user)) {
				group.getAppliedMembers().remove(user);
			}
			if (group.getGroupMembers().contains(user)) {
				group.getGroupMembers().remove(user);
			}
			if (group.getInvitedMembers().contains(user)) {
				group.getInvitedMembers().remove(user);
			}

			user.getGroups().clear();

			groupDAO.update(group);
			userDAO.update(user);
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#addUserToGroup(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean addUserToGroup(User user, Group group) {
		if (user != null && group != null) {
			group.getGroupMembers().add(user);

			groupDAO.update(group);

			return true;
		}
		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#createCommittee(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Committee createCommittee(String name, String description) {
		Committee committee = new Committee();
		committee.setName(name);
		committee.setDescription(description);
		committee.setJoinPolicy(JoinPolicy.INVITATION);
		groupDAO.save(committee);

		return committee;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	@Deprecated
	public GamingGroup createGamingGroup(String name, String description,
			Game game) {
		return null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#createSocialGroup(java.lang.String,
	 * java.lang.String)
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Deprecated
	@Override
	public SocialGroup createSocialGroup(String name, String description) {
		SocialGroup group = new SocialGroup();
		group.setName(name);
		group.setDescription(description);
		group.setJoinPolicy(JoinPolicy.APPLICATION);
		groupDAO.save(group);

		return group;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#inviteUserToGroup(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean inviteUserToGroup(User user, Group group) {
		if (user != null && group != null) {
			group.getInvitedMembers().add(user);

			groupDAO.update(group);

			logService.logUserAction(user, "Groups",
					"User was invited to join " + group.getName());
			notificationService.notifyUser(user,
					"You have been invited to join " + group.getName());

			return true;
		}
		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#setGroupLeader(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setGroupLeader(User user, Group group) {
		if (user != null && group != null) {
			group.setLeader(user);

			groupDAO.update(group);

			notificationService.notifyUser(user,
					"You are now the leader of " + group.getName());

			return true;
		}
		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#applyToGroup(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void applyToGroup(User user, Group group) {
		if (group.getJoinPolicy() == JoinPolicy.APPLICATION) {
			if (!group.getGroupMembers().contains(user) && !group
					.getAppliedMembers().contains(user)) {
				group.getAppliedMembers().add(user);
				groupDAO.update(group);

				logService.logUserAction(user, "Groups",
						"Has applied to join " + group.getName());

				notificationService.notifyUser(group.getLeader(),
						user.getUsername() + " has applied to join " + group
								.getName());
			}
		} else if (group.getJoinPolicy() == JoinPolicy.OPEN) {
			if (!group.getGroupMembers().contains(user)) {
				group.getGroupMembers().add(user);
				groupDAO.update(group);
				logService.logUserAction(user, "Groups",
						"Has joined " + group.getName());
			}

			for (User next : group.getAppliedMembers()) {
				group.getGroupMembers().add(next);
			}

			group.getAppliedMembers().clear();
			groupDAO.update(group);
		}

		userDAO.evict(user);
		// groupDAO.evict(group);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#acceptInvitation(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void acceptInvitation(User user, Group group) {
		if (group.getInvitedMembers().contains(user)) {
			group.getInvitedMembers().remove(user);
			group.getGroupMembers().add(user);
			groupDAO.update(group);

			logService.logUserAction(user, "Groups",
					"Has accepted an invitation to join " + group.getName());
			notificationService.notifyUser(group.getLeader(), user.getUsername()
					+ " has accepted your invitation to join " + group
					.getName());
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#declineInvitation(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void declineInvitation(User user, Group group) {
		if (group.getInvitedMembers().contains(user)) {
			group.getInvitedMembers().remove(user);
			groupDAO.update(group);

			logService.logUserAction(user, "Groups",
					"Has declined an invitation to join " + group.getName());
			notificationService.notifyUser(group.getLeader(), user.getUsername()
					+ " has declined your invitation to join " + group
					.getName());
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#leaveGroup(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void leaveGroup(User user, Group group) {
		if (group.getGroupMembers().contains(user)) {
			int size = group.getGroupMembers().size();
			boolean wasLeader = group.getLeader().equals(user);
			if (wasLeader) {
				group.setLeader(null);

			}
			if (size > 1 && wasLeader) {
				try {
					dispatcher
							.dispatchEvent(new GroupWithoutLeaderEvent(group));
				} catch (EventException e) {
					logger.error(e.getMessage(), e);
				}
			}
			if (size == 1) {
				disbandGroup(user, group);
			} else {

				group.getGroupMembers().remove(user);
				groupDAO.update(group);
				user.getGroups().remove(group);
				userDAO.update(user);
			}

			if (!wasLeader) {
				notificationService.notifyUser(group.getLeader(),
						user.getUsername() + " has left " + group.getName());
			}

			logService.logUserAction(user, "Groups",
					"Has left " + group.getName());
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeFromGroup(User user, Group group) {
		if (group.getGroupMembers().contains(user)) {
			boolean wasLeader = group.getLeader().equals(user);

			if (wasLeader) {
				return;
			}

			group.getGroupMembers().remove(user);
			groupDAO.update(group);
			user.getGroups().remove(group);
			userDAO.update(user);

			notificationService.notifyUser(user,
					"You have been removed from " + group.getName());

			logService.logUserAction(group.getLeader(), "Groups",
					user.getUsername() + " has been removed from " + group
							.getName());
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#disbandGroup(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void disbandGroup(User disbander, Group group) {
		for (User next : group.getGroupMembers()) {
			next.getGroups().remove(group);
			userDAO.update(next);
		}

		GroupForumFilter filter = new GroupForumFilter();
		filter.group(group);

		Seq<GroupForum> forums = groupForumDAO.findByFilter(filter);

		for (GroupForum forum : forums) {
			groupForumDAO.delete(forum);
		}

		groupDAO.delete(group);

		logService.logUserAction(disbander, "Groups",
				"Group " + group.getName() + " was disbanded");

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GroupCreationRequest createGamingGroupRequest(User requester,
			Game game, Realm realm, String name, String description,
			String motivation) {
		GroupCreationRequest request = new GroupCreationRequest();
		request.setName(BBCodeUtil.stripTags(name));
		request.setRequester(requester);
		request.setGame(game);
		request.setRealm(realm);
		request.setDescription(BBCodeUtil.stripTags(description));
		request.setMotivation(BBCodeUtil.stripTags(motivation));

		groupCreationRequestDAO.save(request);

		notifyChancellor(requester.getUsername()
				+ " has requested to create a gaming group called " + name);

		return request;
	}

	/**
	 * @param message
	 */
	private void notifyChancellor(String message) {
		UserFilter filter = new UserFilter();
		filter.rank(Rank.CHANCELLOR);
		for (User user : userDAO.findByFilter(filter)) {
			notificationService.notifyUser(user, message);
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#createSocialGroupRequest(com.tysanclan.site.projectewok.entities.User,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GroupCreationRequest createSocialGroupRequest(User requester,
			String name, String description, String motivation) {
		GroupCreationRequest request = new GroupCreationRequest();
		request.setName(BBCodeUtil.stripTags(name));
		request.setRequester(requester);
		request.setGame(null);
		request.setDescription(BBCodeUtil.stripTags(description));
		request.setMotivation(BBCodeUtil.stripTags(motivation));

		groupCreationRequestDAO.save(request);

		notifyChancellor(requester.getUsername()
				+ " has requested to create a social group called " + name);

		return request;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#declineRequest(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.GroupCreationRequest)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void declineRequest(User decliner, GroupCreationRequest request) {
		groupCreationRequestDAO.load(request.getId()).forEach(_request -> {

			groupCreationRequestDAO.delete(_request);

			logService.logUserAction(decliner, "Groups",
					"Request to create group " + request.getName()
							+ " has been declined");

			notificationService.notifyUser(_request.getRequester(),
					"Your request to create a group called " + request.getName()
							+ " has been declined");
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#acceptRequest(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.GroupCreationRequest)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Group acceptRequest(User accepter, GroupCreationRequest request) {
		return groupCreationRequestDAO.load(request.getId()).map(_request -> {

			Group group;

			if (_request.getGame() != null && _request.getRealm() != null) {
				GamingGroup gamingGroup = new GamingGroup();
				gamingGroup.setName(_request.getName());
				gamingGroup.setDescription(_request.getDescription());
				gamingGroup.setGame(_request.getGame());
				gamingGroup.setRealm(_request.getRealm());
				gamingGroup.setJoinPolicy(JoinPolicy.APPLICATION);
				groupDAO.save(gamingGroup);

				group = gamingGroup;

			} else {
				SocialGroup socialGroup = new SocialGroup();
				socialGroup.setName(_request.getName());
				socialGroup.setDescription(_request.getDescription());
				socialGroup.setJoinPolicy(JoinPolicy.APPLICATION);
				groupDAO.save(socialGroup);

				group = socialGroup;
			}

			setGroupLeader(_request.getRequester(), group);

			group.getGroupMembers().add(_request.getRequester());

			groupCreationRequestDAO.delete(_request);

			logService.logUserAction(accepter, "Groups",
					"Request to create group " + request.getName()
							+ " has been accepted");
			notificationService.notifyUser(_request.getRequester(),
					"Your request to create a group called " + request.getName()
							+ " has been accepted");
			return group;
		}).getOrElseThrow(IllegalStateException::new);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#acceptGroupJoinRequest(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group,
	 * com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void acceptGroupJoinRequest(User accepter, Group group,
			User applicant) {
		userDAO.load(applicant.getId()).forEach(
				_user -> groupDAO.load(group.getId()).forEach(_group -> {
					_group.getAppliedMembers().remove(_user);
					_group.getGroupMembers().add(_user);

					groupDAO.update(_group);
					userDAO.evict(_user);

					notificationService.notifyUser(applicant,
							"Your request to join " + group.getName()
									+ " has been accepted");

					logService.logUserAction(accepter, "Groups",
							"Request by " + applicant.getUsername()
									+ " to join group " + group.getName()
									+ " has been accepted");
				}));
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#declineGroupJoinRequest(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Group,
	 * com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void declineGroupJoinRequest(User decliner, Group group,
			User applicant) {
		userDAO.load(applicant.getId()).forEach(
				_user -> groupDAO.load(group.getId()).forEach(_group -> {
					_group.getAppliedMembers().remove(_user);

					groupDAO.update(_group);

					notificationService.notifyUser(applicant,
							"Your request to join " + group.getName()
									+ " has been declined");

					logService.logUserAction(decliner, "Groups",
							"Request by " + applicant.getUsername()
									+ " to join group " + group.getName()
									+ " has been declined");
				}));

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#setJoinPolicy(com.tysanclan.site.projectewok.entities.Group,
	 * com.tysanclan.site.projectewok.entities.Group.JoinPolicy)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setJoinPolicy(Group group, JoinPolicy joinPolicy) {
		groupDAO.load(group.getId()).forEach(_group -> {
			_group.setJoinPolicy(joinPolicy);
			groupDAO.update(_group);

			String name =
					joinPolicy.name().substring(0, 1).toUpperCase() + joinPolicy
							.name().substring(1).toLowerCase();

			logService.logUserAction(group.getLeader(), "Groups",
					"Join policy of group " + _group.getName() + " set to "
							+ name);
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#setGroupMOTD(com.tysanclan.site.projectewok.entities.Group,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setGroupMOTD(Group group, String motd) {
		groupDAO.load(group.getId()).forEach(_group -> {
			_group.setMessageOfTheDay(BBCodeUtil.stripTags(motd));

			groupDAO.update(_group);
		});

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GroupService#setGroupDescription(com.tysanclan.site.projectewok.entities.Group,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void setGroupDescription(Group group, String description) {
		groupDAO.load(group.getId()).forEach(_group -> {
			_group.setDescription(BBCodeUtil.stripTags(description));

			groupDAO.update(_group);
		});

	}

}
