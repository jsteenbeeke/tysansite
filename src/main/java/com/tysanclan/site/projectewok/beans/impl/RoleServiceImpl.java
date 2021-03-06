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

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.RoleTransfer;
import com.tysanclan.site.projectewok.entities.RoleTransferApproval;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RoleDAO;
import com.tysanclan.site.projectewok.entities.dao.RoleTransferApprovalDAO;
import com.tysanclan.site.projectewok.entities.dao.RoleTransferDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.RoleFilter;
import com.tysanclan.site.projectewok.entities.filter.RoleTransferFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import io.vavr.collection.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class RoleServiceImpl
		implements com.tysanclan.site.projectewok.beans.RoleService {
	@Autowired
	private RoleDAO roleDAO;

	@Autowired
	private RoleTransferDAO roleTransferDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleTransferApprovalDAO roleTransferApprovalDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.NotificationService notificationService;

	public void setTransferDAO(
			RoleTransferApprovalDAO roleTransferApprovalDAO) {
		this.roleTransferApprovalDAO = roleTransferApprovalDAO;
	}

	public void setNotificationService(
			com.tysanclan.site.projectewok.beans.NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	public void setRoleTransferDAO(RoleTransferDAO roleTransferDAO) {
		this.roleTransferDAO = roleTransferDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Role createRole(User user, String name, String description,
			RoleType type) {
		Role role = new Role();
		role.setName(name);
		role.setDescription(BBCodeUtil.stripTags(description));
		role.setRoleType(type);
		role.setAssignedTo(null);
		roleDAO.save(role);

		logService.logUserAction(user, "Roles",
				"Role " + role.getName() + " created");

		return role;
	}

	/**
	 * @param roleDAO the roleDAO to set
	 */
	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @param logService the logService to set
	 */
	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean assignTo(Long assigner_id, Long role_id, Long user_id) {
		return roleDAO.load(role_id).map(role -> {
			User user =
					user_id != null ? userDAO.load(user_id).getOrNull() : null;
			User assigner = assigner_id != null ?
					userDAO.load(assigner_id).getOrNull() :
					null;

			if (role != null && user != null) {
				role.setAssignedTo(user);
				roleDAO.update(role);

				logService.logUserAction(assigner, "Roles",
						"Role " + role.getName() + " assigned to " + user
								.getUsername());

				notificationService.notifyUser(user,
						"You have been given the role of " + role.getName());

				return true;
			} else if (role != null) {
				role.setAssignedTo(null);
				roleDAO.update(role);

				logService.logUserAction(assigner, "Roles",
						"Role " + role.getName() + " cleared");

				return true;
			}

			return false;
		}).getOrElse(false);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public RoleTransfer getCurrentTransfer(RoleType type) {
		RoleTransferFilter filter = new RoleTransferFilter();
		filter.roleType(type);

		return roleTransferDAO.getUniqueByFilter(filter).getOrNull();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void initiateTransfer(RoleType type, User candidate) {
		if (getCurrentTransfer(type) == null) {
			RoleTransfer transfer = new RoleTransfer();
			transfer.setCandidate(candidate);
			transfer.setRoleType(type);
			transfer.setStart(new Date());
			roleTransferDAO.save(transfer);

			logService.logUserAction(candidate, "Organization",
					"User was nominated for " + type.toString());
			notificationService.notifyUser(candidate,
					"You were nominated for " + type.toString());
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void objectToTransfer(User user, RoleTransfer transfer) {
		if (user.getRank() == Rank.SENATOR
				|| user.getRank() == Rank.CHANCELLOR) {
			User current = getCurrentPersonWithRole(transfer.getRoleType());

			if (current != null) {
				notificationService.notifyUser(current,
						"Your nomination of " + transfer.getCandidate()
								.getUsername() + " for " + transfer
								.getRoleType().toString() + " was rejected by "
								+ user.getUsername());
			}

			logService.logUserAction(user, "Organization",
					"Has rejected " + transfer.getCandidate().getUsername()
							+ " as candidate for " + transfer.getRoleType()
							.toString());

			roleTransferDAO.delete(transfer);

		}

	}

	private User getCurrentPersonWithRole(RoleType type) {
		User current = null;
		switch (type) {
			case HERALD:
				current = getHerald();
				break;
			case STEWARD:
				current = getSteward();
				break;
			case TREASURER:
				current = getTreasurer();
				break;
			default:
				return null;
		}
		return current;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void resolveTransfer(RoleTransfer _transfer) {
		roleTransferDAO.load(_transfer.getId()).forEach(transfer -> {

			if (transfer.isAccepted()) {
				RoleFilter filter = new RoleFilter();
				filter.roleType(transfer.getRoleType());

				roleDAO.getUniqueByFilter(filter).forEach(role -> {

					notificationService.notifyUser(transfer.getCandidate(),
							"You have been approved as " + transfer
									.getRoleType().toString());
					logService.logUserAction(transfer.getCandidate(),
							"Organization",
							"Has been approved as the new " + transfer
									.getRoleType());

					role.setAssignedTo(transfer.getCandidate());
					roleTransferDAO.delete(transfer);
				});
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void acceptNomination(RoleTransfer transfer) {
		logService.logUserAction(transfer.getCandidate(), "Organization",
				"Has accepted a nomination as " + transfer.getRoleType());

		transfer.setAccepted(true);
		transfer.setStart(new Date());
		roleTransferDAO.update(transfer);

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void removeRoles(User user) {
		RoleFilter rfilter = new RoleFilter();
		rfilter.assignedTo(user);

		for (Role role : roleDAO.findByFilter(rfilter)) {
			role.setAssignedTo(null);
			roleDAO.update(role);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void removeTransfers(User user) {
		RoleTransferFilter filter = new RoleTransferFilter();
		filter.candidate(user);

		for (RoleTransfer transfer : roleTransferDAO.findByFilter(filter)) {
			roleTransferDAO.delete(transfer);

			User current = getCurrentPersonWithRole(transfer.getRoleType());

			if (current != null) {
				notificationService.notifyUser(current,
						"Your nomination of " + transfer.getCandidate()
								.getUsername() + " for " + transfer
								.getRoleType().toString()
								+ " was removed due to membership termination");
			}

			logService.logUserAction(transfer.getCandidate(), "Organization",
					"Was removed as candidate for " + transfer.getRoleType()
							.toString() + " due to membership termination");

		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void rejectNomination(RoleTransfer transfer) {
		logService.logUserAction(transfer.getCandidate(), "Organization",
				"Has rejected a nomination as " + transfer.getRoleType());

		roleTransferDAO.delete(transfer);
	}

	@Override
	public User getHerald() {
		return getUserByRoleType(RoleType.HERALD);
	}

	/**
	 * @return
	 */
	private User getUserByRoleType(RoleType roleType) {
		Role role = getRoleByType(roleType);

		if (role == null) {
			return null;
		}

		return role.getAssignedTo();
	}

	@Override
	public Role getRoleByType(RoleType type) {
		RoleFilter filter = new RoleFilter();
		filter.roleType(type);

		Seq<Role> roles = roleDAO.findByFilter(filter);

		if (roles.isEmpty()) {
			return null;
		}

		return roles.get(0);

	}

	@Override
	public User getSteward() {
		return getUserByRoleType(RoleType.STEWARD);
	}

	@Override
	public User getTreasurer() {
		return getUserByRoleType(RoleType.TREASURER);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RoleService#deleteRole(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Role)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteRole(User user, Role role) {
		roleDAO.load(role.getId()).forEach(_role -> {
			if (_role.isReassignable()) {
				roleDAO.delete(_role);

				logService.logUserAction(user, "Roles",
						"The role " + role.getName() + " has been deleted");

				if (role.getAssignedTo() != null) {
					notificationService.notifyUser(user,
							"Your role of " + role.getName()
									+ " has been deleted");
				}
			} else {
				throw new RuntimeException(
						"Attempt to delete the Caretaker! You've got a leak somewhere");
			}
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RoleService#setRoleDescription(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Role, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setRoleDescription(User user, Role role, String description) {
		roleDAO.load(role.getId()).forEach(_role -> {
			if (_role.isReassignable()) {
				_role.setDescription(description);
				roleDAO.update(_role);

				logService.logUserAction(user, "Roles",
						"The role " + role.getName()
								+ " has an updated description ");
			} else {
				throw new RuntimeException(
						"Attempt to modify the Caretaker! You've got a leak somewhere");
			}
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RoleService#setRoleName(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Role, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setRoleName(User user, Role role, String name) {
		roleDAO.load(role.getId()).forEach(_role -> {

			String oldName = _role.getName();

			if (_role.isReassignable()) {
				_role.setName(name);
				roleDAO.update(_role);

				logService.logUserAction(user, "Roles",
						"The role " + oldName + " has been renamed to " + name);

				if (role.getAssignedTo() != null) {
					notificationService.notifyUser(user,
							"Your role of " + oldName + " has been renamed to "
									+ name);
				}
			} else {
				throw new RuntimeException(
						"Attempt to modify the Caretaker! You've got a leak somewhere");
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void approveCandidate(RoleTransfer transfer, User user) {
		RoleTransferApproval approval = new RoleTransferApproval();
		approval.setApprovedBy(user);
		approval.setApprovesOf(transfer);

		roleTransferApprovalDAO.save(approval);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveTransfers() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -1);

		RoleTransferFilter filter = new RoleTransferFilter();
		filter.accepted(true);
		filter.start().lessThan(cal.getTime());

		for (RoleTransfer transfer : roleTransferDAO.findByFilter(filter)) {
			resolveTransfer(transfer);
		}

		filter = new RoleTransferFilter();
		filter.accepted(false);
		filter.start().lessThan(cal.getTime());

		for (RoleTransfer transfer : roleTransferDAO.findByFilter(filter)) {
			rejectNomination(transfer);
		}
	}
}
