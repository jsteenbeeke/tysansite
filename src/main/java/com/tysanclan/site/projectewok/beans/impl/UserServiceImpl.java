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
import com.tysanclan.rest.api.util.HashException;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.dao.*;
import com.tysanclan.site.projectewok.entities.filter.ActivationFilter;
import com.tysanclan.site.projectewok.entities.filter.EmailChangeConfirmationFilter;
import com.tysanclan.site.projectewok.entities.filter.PasswordRequestFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;
import com.tysanclan.site.projectewok.util.StringUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

	@Autowired
	private IEventDispatcher dispatcher;

	public void setDispatcher(IEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void setInactivityDAO(InactivityNotificationDAO inactivityDAO) {
		this.inactivityDAO = inactivityDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#createUser(java.lang.String,
	 * java.lang.String, java.lang.String)
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
			user.setArgon2hash(checkedPassword);
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
		filter.username(username);

		return userDAO.countByFilter(filter) > 0;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserImportData(long,
	 * com.tysanclan.rest.api.data.Rank, long)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserImportData(long user_id, Rank rank, long joinTime) {
		return userDAO.load(user_id).map(user -> {
			user.setRank(rank);
			user.setJoinDate(new Date(joinTime));
			userDAO.update(user);

			logService.logSystemAction("Membership", StringUtil.combineStrings(
					"User ", user.getUsername(), " imported"));

			return true;
		}).getOrElse(false);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserRetirement(long,
	 * boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserRetirement(long user_id, boolean retirement) {
		return userDAO.load(user_id).map(user -> {

			user.setRetired(retirement);
			userDAO.update(user);

			return true;
		}).getOrElse(false);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserAvatar(long,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserAvatar(long user_id, String avatarURL) {
		return userDAO.load(user_id).map(user -> {

			user.setImageURL(BBCodeUtil.filterURL(avatarURL));
			userDAO.update(user);

			return true;
		}).getOrElse(false);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserCustomTitle(long,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserCustomTitle(long user_id, String customTitle) {
		return userDAO.load(user_id).map(user -> {

			user.setCustomTitle(BBCodeUtil.stripTags(customTitle));
			userDAO.update(user);

			return true;
		}).getOrElse(false);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserSignature(long,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserSignature(long user_id, String signature) {
		return userDAO.load(user_id).map(user -> {

			user.setSignature(BBCodeUtil.stripTags(signature));
			userDAO.update(user);

			return true;
		}).getOrElse(false);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setUserCollapseForums(Long user_id, boolean collapse) {
		userDAO.load(user_id).forEach(user -> {

			user.setCollapseForums(collapse);
			userDAO.update(user);
		});

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#getActivationByUser(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Activation getActivationByUser(User user) {
		ActivationFilter filter = new ActivationFilter();
		filter.user(user);
		filter.id().orderBy(true);
		Seq<Activation> activations = activationDAO.findByFilter(filter);

		if (!activations.isEmpty()) {
			return activations.get(0);
		}

		// Anders bestaat hij nog niet, maak er dan eentje aan
		Activation activation = new Activation();
		activation.setActivationKey(StringUtil.generateRequestKey(17, 5));
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
		return activationDAO.load(activation.getId()).map(_activation -> {

			logService.logUserAction(activation.getUser(), "User",
									 "Account has been activated");

			activationDAO.delete(_activation);

			return true;
		}).getOrElse(false);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserMail(com.tysanclan.site.projectewok.entities.User,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean setUserMail(User user, String mail) {
		if (!user.getEMail().equals(mail)) {

			UserFilter filter = new UserFilter();
			filter.eMail(mail);

			if (userDAO.countByFilter(filter) == 0) {
				return userDAO.load(user.getId()).map(_user -> {
					_user.setEMail(mail);
					userDAO.save(_user);

					return true;
				}).getOrElse(false);
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
		request.setKey(StringUtil.generateRequestKey(17, 5));
		request.setUser(user);
		request.setRequested(new Date());
		passwordRequestDAO.save(request);

		return request;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#processPasswordReset(com.tysanclan.site.projectewok.entities.PasswordRequest,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void processPasswordReset(PasswordRequest request, String password) {
		Option<PasswordRequest> passwordRequestOption = passwordRequestDAO.load(request.getId());
		if (passwordRequestOption.isDefined()) {
			PasswordRequest _request = passwordRequestOption.get();
			User user = _request.getUser();
			user.setLegacyhash(false);
			user.setArgon2hash(MemberUtil.hashPassword(password));
			userDAO.update(user);

			passwordRequestDAO.delete(_request);
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#expireRequest(com.tysanclan.site.projectewok.entities.PasswordRequest)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expireRequest(PasswordRequest request) {
		passwordRequestDAO.load(request.getId()).forEach(passwordRequestDAO::delete);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#expireConfirmation(com.tysanclan.site.projectewok.entities.EmailChangeConfirmation)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expireConfirmation(EmailChangeConfirmation confirmation) {
		emailChangeConfirmationDAO
				.load(confirmation.getId()).forEach(emailChangeConfirmationDAO::delete);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setPaypalAddress(User user, String paypalAddress) {
		user.setPaypalAddress(paypalAddress);
		userDAO.update(user);

	}

	/**
	 * @param logService the logService to set
	 */
	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	/**
	 * @param activationDAO the activationDAO to set
	 */
	public void setActivationDAO(ActivationDAO activationDAO) {
		this.activationDAO = activationDAO;
	}

	/**
	 * @param passwordRequestDAO the passwordRequestDAO to set
	 */
	public void setPasswordRequestDAO(PasswordRequestDAO passwordRequestDAO) {
		this.passwordRequestDAO = passwordRequestDAO;
	}

	/**
	 * @param emailChangeConfirmationDAO the emailChangeConfirmationDAO to set
	 */
	public void setEmailChangeConfirmationDAO(
			EmailChangeConfirmationDAO emailChangeConfirmationDAO) {
		this.emailChangeConfirmationDAO = emailChangeConfirmationDAO;
	}

	@Override
	public List<User> getMembers() {
		UserFilter userFilter = getMemberFilter();

		return userDAO.findByFilter(userFilter).toJavaList();
	}

	@Override
	public List<User> getMembersOnline() {
		UserFilter userFilter = getMemberFilter();

		Calendar calendar = Calendar.getInstance(DateUtil.NEW_YORK, Locale.US);
		calendar.add(Calendar.MINUTE, -5);

		userFilter.lastAction().greaterThan(calendar.getTime());
		userFilter.username().orderBy(true);

		return userDAO.findByFilter(userFilter).toJavaList();
	}

	/**
	 *
	 */
	private UserFilter getMemberFilter() {
		UserFilter userFilter = new UserFilter();
		userFilter.rank(Rank.CHANCELLOR);
		userFilter.orRank(Rank.FULL_MEMBER);
		userFilter.orRank(Rank.JUNIOR_MEMBER);
		userFilter.orRank(Rank.REVERED_MEMBER);
		userFilter.orRank(Rank.SENATOR);
		userFilter.orRank(Rank.SENIOR_MEMBER);
		userFilter.orRank(Rank.TRIAL);
		userFilter.orRank(Rank.TRUTHSAYER);
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
		filter.lastAction().lessThan(fourteenDaysAgo.getTime());
		filter.rank(Rank.CHANCELLOR);
		filter.orRank(Rank.SENATOR);
		filter.orRank(Rank.TRUTHSAYER);
		filter.orRank(Rank.REVERED_MEMBER);
		filter.orRank(Rank.SENIOR_MEMBER);
		filter.orRank(Rank.FULL_MEMBER);
		filter.orRank(Rank.JUNIOR_MEMBER);
		filter.retired(false);
		filter.vacation(false);
		members.addAll(userDAO.findByFilter(filter).toJavaList());

		UserFilter filter2 = new UserFilter();
		Calendar oneYearAgo = DateUtil.getCalendarInstance();
		oneYearAgo.add(Calendar.YEAR, -1);
		filter2.lastAction().lessThan(oneYearAgo.getTime());
		filter2.rank(Rank.REVERED_MEMBER);
		filter2.orRank(Rank.SENIOR_MEMBER);
		filter2.orRank(Rank.FULL_MEMBER);
		filter2.orRank(Rank.JUNIOR_MEMBER);
		filter2.retired(true);

		members.addAll(userDAO.findByFilter(filter2).toJavaList());

		UserFilter filter3 = new UserFilter();
		Calendar sixtyDaysAgo = DateUtil.getCalendarInstance();
		sixtyDaysAgo.add(Calendar.DAY_OF_YEAR, -60);
		filter3.lastAction().lessThan(sixtyDaysAgo.getTime());
		filter3.rank(Rank.CHANCELLOR);
		filter3.orRank(Rank.SENATOR);
		filter3.orRank(Rank.TRUTHSAYER);
		filter3.orRank(Rank.REVERED_MEMBER);
		filter3.orRank(Rank.SENIOR_MEMBER);
		filter3.orRank(Rank.FULL_MEMBER);
		filter3.orRank(Rank.JUNIOR_MEMBER);
		filter3.orRank(Rank.TRIAL);
		filter3.retired(false);
		filter3.vacation(true);
		members.addAll(userDAO.findByFilter(filter3).toJavaList());

		UserFilter filter4 = new UserFilter();
		Calendar sevenSaysAgo = DateUtil.getCalendarInstance();
		sevenSaysAgo.add(Calendar.DAY_OF_YEAR, -7);
		filter4.lastAction().lessThan(sevenSaysAgo.getTime());
		filter4.rank(Rank.TRIAL);
		filter4.retired(false);
		filter4.vacation(false);
		members.addAll(userDAO.findByFilter(filter4).toJavaList());

		return members;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#countMembers()
	 */
	@Override
	public long countMembers() {
		UserFilter userFilter = getMemberFilter();

		return userDAO.countByFilter(userFilter);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserTimezone(java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setUserTimezone(Long userId, String timezone) {
		userDAO.load(userId).forEach(user -> {
			user.setTimezone(BBCodeUtil.stripTags(timezone));
			userDAO.update(user);
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#activateVacationMode(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void activateVacationMode(User user) {
		userDAO.load(user.getId()).forEach(_user -> {
			_user.setVacation(true);
			userDAO.update(_user);
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#createEmailChangeRequest(com.tysanclan.site.projectewok.entities.User,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public EmailChangeConfirmation createEmailChangeRequest(User user,
															String email) {
		return userDAO.load(user.getId()).map(_user -> {

			EmailChangeConfirmation confirmation = new EmailChangeConfirmation();
			confirmation.setActivationKey(StringUtil.generateRequestKey(17, 5));
			confirmation.setEmail(email);
			confirmation.setInitialized(new Date());
			confirmation.setUser(_user);

			emailChangeConfirmationDAO.save(confirmation);

			String mailBody = mailService.getEmailChangeMailBody(
					_user.getUsername(), confirmation.getActivationKey());

			mailService.sendHTMLMail(email, "Tysan Clan E-Mail Change", mailBody);

			return confirmation;
		}).getOrNull();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#setUserPassword(com.tysanclan.site.projectewok.entities.User,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setUserPassword(User user, String newPassword) {
		userDAO.load(user.getId()).forEach(_user -> {
			_user.setArgon2hash(MemberUtil.hashPassword(newPassword));
			_user.setLegacyhash(false);

			userDAO.update(_user);
		});
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#banUser(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void banUser(User banner, User user) {
		userDAO.load(user.getId())
			   .filter(_user -> _user.getRank() == Rank.FORUM)
			   .forEach(_user -> {
				   _user.setRank(Rank.BANNED);
				   userDAO.update(_user);

				   logService.logUserAction(banner, "Forums",
											"Forum user " + _user.getUsername()
													+ " was banned from the forums");

			   });

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.UserService#unbanUser(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void unbanUser(User unbanner, User user) {
		userDAO.load(user.getId())
			   .filter(_user -> _user.getRank() == Rank.BANNED)
			   .forEach(_user -> {
				   _user.setRank(Rank.FORUM);
				   userDAO.update(_user);

				   logService.logUserAction(unbanner, "Forums",
											"Forum user " + _user.getUsername()
													+ " may once again access the forums");

			   });

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void warnUserForInactivity(Long userId) {
		userDAO.load(userId).forEach(u -> {
			mailService.sendHTMLMail(u.getEMail(),
									 "Please remember to log in to the Tysan Clan website",
									 mailService.getInactivityWarningMail(u));

			logger.info("Notified " + u.getUsername() + " of inactivity");

			InactivityNotification notification = new InactivityNotification();
			notification.setUser(u);
			inactivityDAO.save(notification);
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expireConfirmations() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);

		EmailChangeConfirmationFilter filter = new EmailChangeConfirmationFilter();
		filter.initialized().lessThan(cal.getTime());

		Seq<EmailChangeConfirmation> expiredConfirmations = emailChangeConfirmationDAO
				.findByFilter(filter);
		for (EmailChangeConfirmation confirmation : expiredConfirmations) {
			expireConfirmation(confirmation);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expirePasswordRequests() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.DAY_OF_YEAR, -3);

		PasswordRequestFilter filter = new PasswordRequestFilter();
		filter.requested().lessThan(cal.getTime());

		Seq<PasswordRequest> expiredRequests = passwordRequestDAO
				.findByFilter(filter);
		for (PasswordRequest passwordRequest : expiredRequests) {
			expireRequest(passwordRequest);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void warnInactives() {
		List<Long> inactives = inactivityDAO.getUnnotifiedInactiveUsers();

		for (Long userId : inactives) {
			warnUserForInactivity(userId);
		}

	}
}
