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

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.tysanclan.site.projectewok.entities.Activation;
import com.tysanclan.site.projectewok.entities.EmailChangeConfirmation;
import com.tysanclan.site.projectewok.entities.InactivityNotification;
import com.tysanclan.site.projectewok.entities.PasswordRequest;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ActivationDAO;
import com.tysanclan.site.projectewok.entities.dao.EmailChangeConfirmationDAO;
import com.tysanclan.site.projectewok.entities.dao.InactivityNotificationDAO;
import com.tysanclan.site.projectewok.entities.dao.PasswordRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ActivationFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.event.LoginEvent;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.HTMLSanitizer;
import com.tysanclan.site.projectewok.util.MemberUtil;
import com.tysanclan.site.projectewok.util.StringUtil;

/**
 * Various general user-related actions (non-member)
 * 
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class UserServiceImpl implements
		com.tysanclan.site.projectewok.beans.UserService {
	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ActivationDAO activationDAO;

	@Autowired
	private PasswordRequestDAO passwordRequestDAO;

	@Autowired
	private EmailChangeConfirmationDAO emailChangeConfirmationDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.MailService mailService;

	@Autowired
	private InactivityNotificationDAO inactivityDAO;

	public void setInactivityDAO(InactivityNotificationDAO inactivityDAO) {
		this.inactivityDAO = inactivityDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#createUser(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public User createUser(String username, String password, String email) {
		if (!hasUser(username)) {
			User user = new User();

			String checkedPassword = MemberUtil.isHashedPassword(password) ? password
					: MemberUtil.hashPassword(password);

			user.setCustomTitle("");
			user.setEMail(email);
			user.setImageURL("");
			user.setPassword(checkedPassword);
			user.setRank(Rank.FORUM);
			user.setSignature("");
			user.setUsername(username);
			user.setJoinDate(new Date());
			user.setVacation(false);
			user.setRetired(false);
			userDAO.save(user);

			logger.info(StringUtil.combineStrings("Created user ",
					user.getUsername(), " (uid ", user.getId(), ")"));

			return user;
		}

		return null;
	}

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#hasUser(java.lang.String)
	 */
	@Override
	public boolean hasUser(String username) {
		UserFilter filter = new UserFilter();
		filter.setUsername(username);

		return userDAO.countByFilter(filter) > 0;
	}

	/**
	 * @param userDAO
	 *            the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserImportData(long,
	 *      com.tysanclan.site.projectewok.entities.Rank, long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserImportData(long user_id, Rank rank, long joinTime) {
		User user = userDAO.load(user_id);

		if (user != null) {
			user.setRank(rank);
			user.setJoinDate(new Date(joinTime));
			userDAO.update(user);

			logService.logSystemAction("Membership", StringUtil.combineStrings(
					"User ", user.getUsername(), " imported"));

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserRetirement(long,
	 *      boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserRetirement(long user_id, boolean retirement) {
		User user = userDAO.load(user_id);

		if (user != null) {
			user.setRetired(retirement);
			userDAO.update(user);

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserAvatar(long,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserAvatar(long user_id, String avatarURL) {
		User user = userDAO.load(user_id);

		if (user != null) {
			user.setImageURL(HTMLSanitizer.filterURL(avatarURL));
			userDAO.update(user);

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserCustomTitle(long,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserCustomTitle(long user_id, String customTitle) {
		User user = userDAO.load(user_id);

		if (user != null) {
			user.setCustomTitle(HTMLSanitizer.stripTags(customTitle));
			userDAO.update(user);

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserSignature(long,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserSignature(long user_id, String signature) {
		User user = userDAO.load(user_id);

		if (user != null) {
			user.setSignature(HTMLSanitizer.sanitize(signature));
			userDAO.update(user);

			return true;
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setUserCollapseForums(Long user_id, boolean collapse) {
		User user = userDAO.load(user_id);

		if (user != null) {
			user.setCollapseForums(collapse);
			userDAO.update(user);
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#getActivationByUser(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Activation getActivationByUser(User user) {
		ActivationFilter filter = new ActivationFilter();
		filter.setUser(user);
		filter.addOrderBy("id", true);
		List<Activation> activations = activationDAO.findByFilter(filter);

		if (!activations.isEmpty()) {
			return activations.get(0);
		}

		// Anders bestaat hij nog niet, maak er dan eentje aan
		Activation activation = new Activation();
		activation.setActivationKey(StringUtil.generateRequestKey());
		activation.setUser(user);
		activation.setRegistered(new Date());

		activationDAO.save(activation);

		return activation;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#activateAccount(com.tysanclan.site.projectewok.entities.Activation)
	 */
	@Override
	public boolean activateAccount(Activation activation) {
		Activation _activation = activationDAO.load(activation.getId());

		if (_activation != null) {
			logService.logUserAction(activation.getUser(), "User",
					"Account has been activated");

			activationDAO.delete(_activation);

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#expireActivation(com.tysanclan.site.projectewok.entities.Activation)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expireActivation(Activation activation) {
		Activation _activation = activationDAO.load(activation.getId());

		User user = _activation.getUser();
		activationDAO.delete(_activation);
		userDAO.delete(user);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserMail(com.tysanclan.site.projectewok.entities.User,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserMail(User user, String mail) {
		if (!user.getEMail().equals(mail)) {

			UserFilter filter = new UserFilter();
			filter.setEmail(mail);

			if (userDAO.countByFilter(filter) == 0) {

				User _user = userDAO.load(user.getId());
				_user.setEMail(mail);
				userDAO.save(_user);

				return true;
			}

			return false;
		}

		return true;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#generatePasswordRequest(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PasswordRequest generatePasswordRequest(User user) {
		PasswordRequest request = new PasswordRequest();
		request.setKey(StringUtil.generateRequestKey());
		request.setUser(user);
		request.setRequested(new Date());
		passwordRequestDAO.save(request);

		return request;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@OnFortuityEvent(LoginEvent.class)
	public void onLoginEvent(EventContext<LoginEvent> context) {
		LoginEvent event = context.getEvent();
		User user = event.getSource();
		if (MemberUtil.isMember(user)) {
			user.setLoginCount(user.getLoginCount() + 1);
			userDAO.update(user);
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#processPasswordReset(com.tysanclan.site.projectewok.entities.PasswordRequest,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void processPasswordReset(PasswordRequest request, String password) {
		PasswordRequest _request = passwordRequestDAO.load(request.getId());
		User user = _request.getUser();
		user.setPassword(MemberUtil.hashPassword(password));
		userDAO.update(user);

		passwordRequestDAO.delete(_request);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#expireRequest(com.tysanclan.site.projectewok.entities.PasswordRequest)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expireRequest(PasswordRequest request) {
		PasswordRequest _request = passwordRequestDAO.load(request.getId());
		passwordRequestDAO.delete(_request);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#expireConfirmation(com.tysanclan.site.projectewok.entities.EmailChangeConfirmation)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expireConfirmation(EmailChangeConfirmation confirmation) {
		EmailChangeConfirmation _confirmation = emailChangeConfirmationDAO
				.load(confirmation.getId());
		emailChangeConfirmationDAO.delete(_confirmation);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setPaypalAddress(User user, String paypalAddress) {
		user.setPaypalAddress(paypalAddress);
		userDAO.update(user);

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
	 * @param activationDAO
	 *            the activationDAO to set
	 */
	public void setActivationDAO(ActivationDAO activationDAO) {
		this.activationDAO = activationDAO;
	}

	/**
	 * @param passwordRequestDAO
	 *            the passwordRequestDAO to set
	 */
	public void setPasswordRequestDAO(PasswordRequestDAO passwordRequestDAO) {
		this.passwordRequestDAO = passwordRequestDAO;
	}

	/**
	 * @param emailChangeConfirmationDAO
	 *            the emailChangeConfirmationDAO to set
	 */
	public void setEmailChangeConfirmationDAO(
			EmailChangeConfirmationDAO emailChangeConfirmationDAO) {
		this.emailChangeConfirmationDAO = emailChangeConfirmationDAO;
	}

	@Override
	public List<User> getMembers() {
		UserFilter userFilter = getMemberFilter();

		return userDAO.findByFilter(userFilter);
	}

	@Override
	public List<User> getMembersOnline() {
		UserFilter userFilter = getMemberFilter();

		Calendar calendar = Calendar.getInstance(DateUtil.NEW_YORK, Locale.US);
		calendar.add(Calendar.MINUTE, -5);

		userFilter.setActiveSince(calendar.getTime());

		return userDAO.findByFilter(userFilter);
	}

	/**
	 	 */
	private UserFilter getMemberFilter() {
		UserFilter userFilter = new UserFilter();
		userFilter.addRank(Rank.CHANCELLOR);
		userFilter.addRank(Rank.FULL_MEMBER);
		userFilter.addRank(Rank.JUNIOR_MEMBER);
		userFilter.addRank(Rank.REVERED_MEMBER);
		userFilter.addRank(Rank.SENATOR);
		userFilter.addRank(Rank.SENIOR_MEMBER);
		userFilter.addRank(Rank.TRIAL);
		userFilter.addRank(Rank.TRUTHSAYER);
		return userFilter;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#getInactiveMembers()
	 */
	@Override
	public List<User> getInactiveMembers() {
		List<User> members = new LinkedList<User>();
		UserFilter filter = new UserFilter();
		Calendar fourteenDaysAgo = DateUtil.getCalendarInstance();
		fourteenDaysAgo.add(Calendar.DAY_OF_YEAR, -14);
		filter.setActiveBefore(fourteenDaysAgo.getTime());
		filter.addRank(Rank.CHANCELLOR);
		filter.addRank(Rank.SENATOR);
		filter.addRank(Rank.TRUTHSAYER);
		filter.addRank(Rank.REVERED_MEMBER);
		filter.addRank(Rank.SENIOR_MEMBER);
		filter.addRank(Rank.FULL_MEMBER);
		filter.addRank(Rank.JUNIOR_MEMBER);
		filter.setRetired(false);
		filter.setVacation(false);
		members.addAll(userDAO.findByFilter(filter));

		UserFilter filter2 = new UserFilter();
		Calendar oneYearAgo = DateUtil.getCalendarInstance();
		oneYearAgo.add(Calendar.YEAR, -1);
		filter2.setActiveBefore(oneYearAgo.getTime());
		filter2.addRank(Rank.REVERED_MEMBER);
		filter2.addRank(Rank.SENIOR_MEMBER);
		filter2.addRank(Rank.FULL_MEMBER);
		filter2.addRank(Rank.JUNIOR_MEMBER);
		filter2.setRetired(true);

		members.addAll(userDAO.findByFilter(filter2));

		UserFilter filter3 = new UserFilter();
		Calendar sixtyDaysAgo = DateUtil.getCalendarInstance();
		sixtyDaysAgo.add(Calendar.DAY_OF_YEAR, -60);
		filter3.setActiveBefore(sixtyDaysAgo.getTime());
		filter3.addRank(Rank.CHANCELLOR);
		filter3.addRank(Rank.SENATOR);
		filter3.addRank(Rank.TRUTHSAYER);
		filter3.addRank(Rank.REVERED_MEMBER);
		filter3.addRank(Rank.SENIOR_MEMBER);
		filter3.addRank(Rank.FULL_MEMBER);
		filter3.addRank(Rank.JUNIOR_MEMBER);
		filter3.addRank(Rank.TRIAL);
		filter3.setRetired(false);
		filter3.setVacation(true);
		members.addAll(userDAO.findByFilter(filter3));

		UserFilter filter4 = new UserFilter();
		Calendar sevenSaysAgo = DateUtil.getCalendarInstance();
		sevenSaysAgo.add(Calendar.DAY_OF_YEAR, -7);
		filter4.setActiveBefore(sevenSaysAgo.getTime());
		filter4.addRank(Rank.TRIAL);
		filter4.setRetired(false);
		filter4.setVacation(false);
		members.addAll(userDAO.findByFilter(filter4));

		return members;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#countMembers()
	 */
	@Override
	public int countMembers() {
		UserFilter userFilter = getMemberFilter();

		return userDAO.countByFilter(userFilter);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserTimezone(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setUserTimezone(Long userId, String timezone) {
		User user = userDAO.load(userId);

		if (user != null) {
			user.setTimezone(HTMLSanitizer.stripTags(timezone));
			userDAO.update(user);
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#activateVacationMode(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void activateVacationMode(User user) {
		User _user = userDAO.load(user.getId());

		if (_user != null) {
			_user.setVacation(true);
			userDAO.update(_user);
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#createEmailChangeRequest(com.tysanclan.site.projectewok.entities.User,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public EmailChangeConfirmation createEmailChangeRequest(User user,
			String email) {
		User _user = userDAO.load(user.getId());

		EmailChangeConfirmation confirmation = new EmailChangeConfirmation();
		confirmation.setActivationKey(StringUtil.generateRequestKey());
		confirmation.setEmail(email);
		confirmation.setInitialized(new Date());
		confirmation.setUser(_user);

		emailChangeConfirmationDAO.save(confirmation);

		String mailBody = mailService.getEmailChangeMailBody(
				_user.getUsername(), confirmation.getActivationKey());

		mailService.sendHTMLMail(email, "Tysan Clan E-Mail Change", mailBody);

		return confirmation;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserPassword(com.tysanclan.site.projectewok.entities.User,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setUserPassword(User user, String newPassword) {
		User _user = userDAO.load(user.getId());

		_user.setPassword(MemberUtil.hashPassword(newPassword));

		userDAO.update(_user);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#banUser(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void banUser(User banner, User user) {
		User _user = userDAO.load(user.getId());
		if (_user.getRank() == Rank.FORUM) {
			_user.setRank(Rank.BANNED);
			userDAO.update(_user);

			logService.logUserAction(banner, "Forums",
					"Forum user " + _user.getUsername()
							+ " was banned from the forums");

		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#unbanUser(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void unbanUser(User unbanner, User user) {
		User _user = userDAO.load(user.getId());
		if (_user.getRank() == Rank.BANNED) {
			_user.setRank(Rank.FORUM);
			userDAO.update(_user);

			logService.logUserAction(unbanner, "Forums",
					"Forum user " + _user.getUsername()
							+ " may once again access the forums");

		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void warnUserForInactivity(Long userId) {
		User u = userDAO.load(userId);

		mailService.sendHTMLMail(u.getEMail(),
				"Please remember to log in to the Tysan Clan website",
				mailService.getInactivityWarningMail(u));

		logger.info("Notified " + u.getUsername() + " of inactivity");

		InactivityNotification notification = new InactivityNotification();
		notification.setUser(u);
		inactivityDAO.save(notification);
	}
}
