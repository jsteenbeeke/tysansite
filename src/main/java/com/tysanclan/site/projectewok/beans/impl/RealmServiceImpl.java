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
import com.tysanclan.site.projectewok.beans.LogService;
import com.tysanclan.site.projectewok.beans.NotificationService;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;
import com.tysanclan.site.projectewok.entities.dao.RealmPetitionDAO;
import com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO;
import com.tysanclan.site.projectewok.entities.filter.UserGameRealmFilter;
import com.tysanclan.site.projectewok.event.RealmDeletionEvent;
import com.tysanclan.site.projectewok.util.MemberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.events.EventException;

import java.util.Date;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class RealmServiceImpl
		implements com.tysanclan.site.projectewok.beans.RealmService {
	private static final Logger log = LoggerFactory
			.getLogger(RealmServiceImpl.class);

	@Autowired
	private RealmDAO realmDAO;

	@Autowired
	private RealmPetitionDAO realmPetitionDAO;

	@Autowired
	private UserGameRealmDAO userGameRealmDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.NotificationService notificationService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.UserService userService;

	@Autowired
	private IEventDispatcher dispatcher;

	public void setDispatcher(IEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(
			com.tysanclan.site.projectewok.beans.UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param notificationService
	 *            the notificationService to set
	 */
	public void setNotificationService(
			NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/**
	 * @param logService
	 *            the logService to set
	 */
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	/**
	 * @param realmDAO
	 *            the realmDAO to set
	 */
	public void setRealmDAO(RealmDAO realmDAO) {
		this.realmDAO = realmDAO;
	}

	/**
	 * @param userGameRealmDAO
	 *            the userGameRealmDAO to set
	 */
	public void setUserGameRealmDAO(UserGameRealmDAO userGameRealmDAO) {
		this.userGameRealmDAO = userGameRealmDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#createRealmFromPetition(com.tysanclan.site.projectewok.entities.RealmPetition)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Realm createRealmFromPetition(RealmPetition petition) {
		Realm realm = new Realm();
		realm.setName(petition.getName());
		realm.setChannel(null);
		realm.getGames().add(petition.getGame());
		realm.setOverseer(petition.getRequester());
		realmDAO.save(realm);

		addPlayedGame(petition.getRequester(), petition.getGame(), realm);
		for (User user : petition.getSignatures()) {
			addPlayedGame(user, petition.getGame(), realm);
		}

		String message =
				"The petition for expanding " + petition.getGame().getName()
						+ " to realm " + petition.getName()
						+ " has passed succesfully!";

		logService.logSystemAction("Petitions", message);
		notifyPetitionParticipants(petition, message);

		realmPetitionDAO.delete(petition);

		return realm;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#createRealmPetition(java.lang.String,
	 *      com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Game)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RealmPetition createRealmPetition(String name, User user,
			Game game) {

		RealmPetition petition = new RealmPetition();
		petition.setGame(game);
		petition.setRequester(user);
		petition.setName(name);
		petition.setStart(new Date());

		realmPetitionDAO.save(petition);

		return petition;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkPetitionExpired(RealmPetition petition) {
		if (petition.getExpires().before(new Date())) {
			String message;

			if (petition.getRealm() == null) {
				message = "The petition for new realm " + petition.getName()
						+ " for game " + petition.getGame().getName()
						+ " has expired without gaining enough signatures";

			} else {
				message = "The petition for expanding " + petition.getGame()
						.getName() + " to realm " + petition.getRealm()
						.getName()
						+ " has expired without gaining enough signatures";
			}

			logService.logSystemAction("Petitions", message);
			notifyPetitionParticipants(petition, message);

			realmPetitionDAO.delete(petition);

		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#createRealmPetition(com.tysanclan.site.projectewok.entities.Realm,
	 *      com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Game)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public RealmPetition createRealmPetition(Realm realm, User user,
			Game game) {
		RealmPetition petition = new RealmPetition();
		petition.setGame(game);
		petition.setRequester(user);
		petition.setRealm(realm);
		petition.setStart(new Date());

		realmPetitionDAO.save(petition);

		return petition;
	}

	/**
	 * @param realmPetitionDAO
	 *            the realmPetitionDAO to set
	 */
	public void setRealmPetitionDAO(RealmPetitionDAO realmPetitionDAO) {
		this.realmPetitionDAO = realmPetitionDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#getRealms()
	 */
	@Override
	public List<Realm> getRealms() {
		return realmDAO.findAll().asJava();
	}

	/**
	 * @deprecated For internal use by GameService only
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@Deprecated
	public Realm createRealm(String newRealmName, Game game, User requester) {
		Realm realm = new Realm();
		realm.setName(newRealmName);
		realm.setChannel(null);
		realm.getGames().add(game);
		realm.setOverseer(requester);

		realmDAO.save(realm);

		return realm;

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#countActivePlayers(com.tysanclan.site.projectewok.entities.Realm)
	 */
	@Override
	public int countActivePlayers(Realm realm) {
		return userGameRealmDAO.countActivePlayers(realm);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#isRealmInactive(com.tysanclan.site.projectewok.entities.Realm)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isRealmInactive(Realm realm) {
		if (realm.getGames().isEmpty()) {
			return true;
		}
		if (countActivePlayers(realm) < 5) {
			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#setSupervisor(com.tysanclan.site.projectewok.entities.Realm,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setSupervisor(Realm _realm, User user) {
		realmDAO.load(_realm.getId()).forEach(realm -> {
			if (MemberUtil.isMember(user) && user.getRank() != Rank.TRIAL) {
				boolean wasReplaced = realm.getOverseer() == null;
				if (realm.getOverseer() != null && !realm.getOverseer()
						.equals(user)) {
					notificationService.notifyUser(realm.getOverseer(),
							"You are no longer supervisor for the realm "
									+ realm.getName());
					wasReplaced = true;
				}

				realm.setOverseer(user);

				if (wasReplaced) {
					notificationService.notifyUser(realm.getOverseer(),
							"You are now supervisor for the realm " + realm
									.getName());
					logService.logUserAction(realm.getOverseer(), "Realm",
							"User is now supervisor for realm " + realm
									.getName());
				}
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteRealm(Realm _realm, User user) {
		realmDAO.load(_realm.getId()).forEach(realm -> {

			if (user.getRank() == Rank.CHANCELLOR) {
				realmDAO.delete(realm);

				logService.logUserAction(user, "Realm", "Realm was removed");

				try {
					dispatcher.dispatchEvent(new RealmDeletionEvent(realm));
				} catch (EventException e) {
					log.error(e.getMessage(), e);
				}
			}
		});
	}

	/**
	 * @param petition
	 * @param message
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	protected void notifyPetitionParticipants(RealmPetition petition,
			String message) {
		notificationService.notifyUser(petition.getRequester(), message);
		for (User user : petition.getSignatures()) {
			notificationService.notifyUser(user, message);
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#linkRealmToGame(com.tysanclan.site.projectewok.entities.RealmPetition)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void linkRealmToGame(RealmPetition petition) {
		Realm realm = petition.getRealm();
		Game game = petition.getGame();

		realm.getGames().add(game);

		realmDAO.update(realm);

		addPlayedGame(petition.getRequester(), game, realm);
		for (User user : petition.getSignatures()) {
			addPlayedGame(user, game, realm);
		}

		String message =
				"The petition for expanding " + game.getName() + " to realm "
						+ realm.getName() + " has passed succesfully!";

		logService.logSystemAction("Petitions", message);
		notifyPetitionParticipants(petition, message);

		realmPetitionDAO.delete(petition);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#signPetition(com.tysanclan.site.projectewok.entities.RealmPetition,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void signPetition(RealmPetition petition, User user) {
		if (!petition.getRequester().equals(user)) {
			petition.getSignatures().add(user);
			realmPetitionDAO.update(petition);

			if (petition.getSignatures().size()
					>= getRequiredPetitionSignatures()) {
				// Trigger check
				if (petition.getName() != null) {
					createRealmFromPetition(petition);
				} else {
					linkRealmToGame(petition);
				}
			}
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#addPlayedGame(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Game,
	 *      com.tysanclan.site.projectewok.entities.Realm)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addPlayedGame(User user, Game game, Realm realm) {
		UserGameRealmFilter filter = new UserGameRealmFilter();

		filter.user(user);
		filter.game(game);
		filter.realm(realm);

		if (userGameRealmDAO.countByFilter(filter) == 0) {
			UserGameRealm ugr = new UserGameRealm();
			ugr.setGame(game);
			ugr.setRealm(realm);
			ugr.setUser(user);

			userGameRealmDAO.save(ugr);
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.RealmService#getRequiredPetitionSignatures()
	 */
	@Override
	public int getRequiredPetitionSignatures() {
		Long mcount = userService.countMembers();

		return petitionFormula(mcount);
	}

	static int petitionFormula(long mcount) {
		if (mcount > 75) {
			return 9;
		} else if (mcount < 25) {
			return 4;
		}

		return (int) (((mcount - 25) / 10) + 4);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void expirePetitions() {
		for (RealmPetition petition : realmPetitionDAO.findAll()) {
			checkPetitionExpired(petition);
		}

	}
}
