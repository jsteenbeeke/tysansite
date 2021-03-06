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
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.dao.*;
import com.tysanclan.site.projectewok.entities.filter.AllowedAccountTypeFilter;
import com.tysanclan.site.projectewok.entities.filter.GameFilter;
import com.tysanclan.site.projectewok.entities.filter.UserGameRealmFilter;
import com.tysanclan.site.projectewok.event.GameDeletionEvent;
import com.tysanclan.site.projectewok.util.ImageUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.events.EventException;

import java.awt.*;
import java.util.Date;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GameServiceImpl
		implements com.tysanclan.site.projectewok.beans.GameService {
	private static final Logger log = LoggerFactory
			.getLogger(GameServiceImpl.class);

	@Autowired
	private GameAccountDAO gameAccountDAO;

	@Autowired
	private GameDAO gameDAO;

	@Autowired
	private RealmDAO realmDAO;

	@Autowired
	private GamePetitionDAO gamePetitionDAO;

	@Autowired
	private UserGameRealmDAO userGameRealmDAO;

	@Autowired
	private AllowedAccountTypeDAO allowedAccountTypeDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.UserService userService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.NotificationService notificationService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.RealmService realmService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private GroupDAO groupDAO;

	@Autowired
	private IEventDispatcher dispatcher;

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public void setDispatcher(IEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * @return the realmDAO
	 */
	public RealmDAO getRealmDAO() {
		return realmDAO;
	}

	/**
	 * @param gameAccountDAO the gameAccountDAO to set
	 */
	public void setGameAccountDAO(GameAccountDAO gameAccountDAO) {
		this.gameAccountDAO = gameAccountDAO;
	}

	public void setAllowedAccountTypeDAO(
			AllowedAccountTypeDAO allowedAccountTypeDAO) {
		this.allowedAccountTypeDAO = allowedAccountTypeDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#createGame(java.lang.String,
	 * byte[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Game createGame(String name, byte[] image) {
		Game game = new Game();
		game.setName(name);
		game.setImage(image);
		game.setActive(true);
		gameDAO.save(game);

		return game;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#getActiveGames()
	 */
	@Override
	public List<Game> getActiveGames() {
		GameFilter filter = new GameFilter();

		filter.active(true);

		return gameDAO.findByFilter(filter).toJavaList();
	}

	/**
	 * @param logService the logService to set
	 */
	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	/**
	 * @param realmService the realmService to set
	 */
	public void setRealmService(RealmServiceImpl realmService) {
		this.realmService = realmService;
	}

	/**
	 * @param notificationService the notificationService to set
	 */
	public void setNotificationService(
			com.tysanclan.site.projectewok.beans.NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	/**
	 * @param gameDAO the gameDAO to set
	 */
	public void setGameDAO(GameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}

	public void setUserService(UserServiceImpl userService) {
		this.userService = userService;
	}

	/**
	 * @param gamePetitionDAO the gamePetitionDAO to set
	 */
	public void setGamePetitionDAO(GamePetitionDAO gamePetitionDAO) {
		this.gamePetitionDAO = gamePetitionDAO;
	}

	/**
	 * @param userGameRealmDAO the userGameRealmDAO to set
	 */
	public void setUserGameRealmDAO(UserGameRealmDAO userGameRealmDAO) {
		this.userGameRealmDAO = userGameRealmDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#getRequiredPetitionSignatures()
	 */
	@Override
	public int getRequiredPetitionSignatures() {
		long mcount = userService.countMembers();

		return petitionFormula(mcount);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GamePetition createPetition(User user, String name, Realm realm,
			byte[] icon) {
		Dimension d = ImageUtil.getImageDimensions(icon);

		if (d.width < 48 || d.height < 48) {
			return null;
		}

		if (d.width > 64 || d.height > 64) {
			return null;
		}

		GamePetition petition = new GamePetition();
		petition.setImage(icon);
		petition.setName(name);
		petition.setRequester(user);
		petition.setRealm(realm);
		petition.setStart(new Date());

		gamePetitionDAO.save(petition);

		return petition;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GamePetition createPetition(User user, String name, String realmName,
			byte[] icon) {
		GamePetition petition = new GamePetition();
		petition.setImage(icon);
		petition.setName(name);
		petition.setRequester(user);
		petition.setNewRealmName(realmName);
		petition.setStart(new Date());

		gamePetitionDAO.save(petition);

		return petition;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#createGameFromPetition(com.tysanclan.site.projectewok.entities.GamePetition)
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Game createGameFromPetition(GamePetition petition) {
		if (petition.getSignatures().size()
				>= getRequiredPetitionSignatures()) {
			Game game = new Game();
			game.setActive(true);
			game.setCoordinator(petition.getRequester());
			game.setImage(petition.getImage());
			game.setName(petition.getName());

			gameDAO.save(game);

			Realm realm;

			if (petition.getNewRealmName() != null) {
				realm = realmService
						.createRealm(petition.getNewRealmName(), game,
								petition.getRequester());
			} else {
				game.getRealms().add(petition.getRealm());
				gameDAO.update(game);
				realm = petition.getRealm();
			}

			addPlayedGame(petition.getRequester(), game, realm);
			for (User user : petition.getSignatures()) {
				addPlayedGame(user, game, realm);
			}

			notifyPetitionParticipants(petition,
					"New game " + petition.getName() + " has been accepted!");

			gamePetitionDAO.delete(petition);
		}

		return null;
	}

	static int petitionFormula(long mcount) {
		if (mcount > 75) {
			return 9;
		} else if (mcount < 25) {
			return 4;
		}

		return (int) (((mcount - 25) / 10) + 4);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#getPlayedGames(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	public List<UserGameRealm> getPlayedGames(User user) {
		UserGameRealmFilter filter = new UserGameRealmFilter();

		filter.user(user);

		return userGameRealmDAO.findByFilter(filter).toJavaList();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#addPlayedGame(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Game,
	 * com.tysanclan.site.projectewok.entities.Realm)
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

	@Transactional(propagation = Propagation.REQUIRED)
	public void removePlayedGame(User user, Game game, Realm realm) {
		UserGameRealmFilter filter = new UserGameRealmFilter();

		filter.user(user);
		filter.game(game);
		filter.realm(realm);

		if (userGameRealmDAO.countByFilter(filter) > 0) {
			for (UserGameRealm ugr : userGameRealmDAO.findByFilter(filter)) {
				userGameRealmDAO.delete(ugr);
			}
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#signPetition(com.tysanclan.site.projectewok.entities.GamePetition,
	 * com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void signPetition(GamePetition petition, User user) {
		if (!petition.getRequester().equals(user)) {
			petition.getSignatures().add(user);
			gamePetitionDAO.update(petition);
			// Trigger check
			createGameFromPetition(petition);
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#checkPetitionExpired(com.tysanclan.site.projectewok.entities.GamePetition)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkPetitionExpired(GamePetition petition) {
		if (petition.getExpires().before(new Date())) {
			logService.logSystemAction("Petitions",
					"Petition for game " + petition.getName()
							+ " has expired without reaching the required amount of signatures");
			String message = "The petition for new game " + petition.getName()
					+ " has expired without gaining enough signatures";
			notifyPetitionParticipants(petition, message);

			gamePetitionDAO.delete(petition);

		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#countPlayers(com.tysanclan.site.projectewok.entities.Game,
	 * com.tysanclan.site.projectewok.entities.Realm)
	 */
	@Override
	public int countPlayers(Game game, Realm realm) {
		return userGameRealmDAO.getActiveUsers(game, realm).size();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	protected void notifyPetitionParticipants(GamePetition petition,
			String message) {
		notificationService.notifyUser(petition.getRequester(), message);
		for (User user : petition.getSignatures()) {
			notificationService.notifyUser(user, message);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GameAccount createMinecraftAccount(UserGameRealm userGameRealm,
			String username) {
		MinecraftAccount account = new MinecraftAccount();

		account.setName(username);
		account.setUserGameRealm(userGameRealm);

		return trySaveAccount(account);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GameAccount createDiablo2Account(UserGameRealm userGameRealm,
			String username) {
		Diablo2Account account = new Diablo2Account();

		account.setName(username);
		account.setUserGameRealm(userGameRealm);

		return trySaveAccount(account);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GameAccount createRealIDAccount(UserGameRealm userGameRealm,
			String email) {
		RealIdAccount account = new RealIdAccount();

		account.setName(email);
		account.setUserGameRealm(userGameRealm);

		return trySaveAccount(account);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GameAccount createLeagueOfLegendsAccount(UserGameRealm userGameRealm,
			String username) {
		LeagueOfLegendsAccount account = new LeagueOfLegendsAccount();

		account.setName(username);
		account.setUserGameRealm(userGameRealm);

		return trySaveAccount(account);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GameAccount createStarCraft2Account(UserGameRealm userGameRealm,
			String username, int characterCode) {
		StarCraft2CharAccount account = new StarCraft2CharAccount();

		account.setCharacterCode(characterCode);
		account.setName(username);
		account.setUserGameRealm(userGameRealm);

		return trySaveAccount(account);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	protected GameAccount trySaveAccount(GameAccount account) {
		if (account.isValid() && isValidAccountType(
				account.getUserGameRealm().getGame(),
				account.getUserGameRealm().getRealm(), account.getType())) {
			gameAccountDAO.save(account);
		} else {
			return null;
		}

		return account;
	}

	@Override
	public boolean isValidAccountType(Game game, Realm realm,
			AccountType type) {
		AllowedAccountTypeFilter filter = new AllowedAccountTypeFilter();

		filter.game(game);
		filter.realm(realm);
		filter.type(type);

		return allowedAccountTypeDAO.countByFilter(filter) >= 1;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#deleteAccount(com.tysanclan.site.projectewok.entities.GameAccount)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAccount(GameAccount account) {
		gameAccountDAO.delete(account);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#removePlayedGame(com.tysanclan.site.projectewok.entities.UserGameRealm)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removePlayedGame(UserGameRealm userGameRealm) {
		for (GameAccount account : userGameRealm.getAccounts()) {
			deleteAccount(account);
		}

		userGameRealmDAO.delete(userGameRealm);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#countActivePlayers(com.tysanclan.site.projectewok.entities.Game)
	 */
	@Override
	public int countActivePlayers(Game game) {
		return userGameRealmDAO.countActivePlayers(game);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#isGameInactive(com.tysanclan.site.projectewok.entities.Game)
	 */
	@Override
	public boolean isGameInactive(Game game) {
		return countActivePlayers(game) < 5;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteGame(User user, Game _game) {
		gameDAO.load(_game.getId()).forEach(game -> {
			if (user.getRank() == Rank.CHANCELLOR) {
				gameDAO.delete(game);

				logService.logUserAction(user, "Realm", "Realm was removed");

				try {
					dispatcher.dispatchEvent(new GameDeletionEvent(game));
				} catch (EventException e) {
					log.error(e.getMessage(), e);
				}
			}
		});

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GameService#removeFromRealm(com.tysanclan.site.projectewok.entities.User,
	 * com.tysanclan.site.projectewok.entities.Realm,
	 * com.tysanclan.site.projectewok.entities.Game)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeFromRealm(User user, Realm _realm, Game _game) {
		realmDAO.load(_realm.getId())
				.forEach(realm -> gameDAO.load(_game.getId()).forEach(game -> {

					realm.getGames().remove(game);
					game.getRealms().remove(realm);

					logService.logUserAction(user, "Games",
							game.getName() + " is no longer played on " + realm
									.getName());

					if (realm.getGames().isEmpty()) {
						for (GamingGroup group : realm.getGroups()) {
							logService.logSystemAction("Groups",
									"Group " + group.getName()
											+ " has been disbanded due to its realm being removed");
							groupDAO.delete(group);
						}

						logService.logSystemAction("Realms",
								"Realm " + realm.getName()
										+ " no longer has any active games and has therefore been removed");

						realmDAO.delete(realm);

					}

					if (game.getRealms().isEmpty()) {
						for (GamingGroup group : game.getGroups()) {
							logService.logSystemAction("Groups",
									"Group " + group.getName()
											+ " has been disbanded due to its game being removed");
							groupDAO.delete(group);
						}

						logService.logSystemAction("Games",
								"Game " + game.getName()
										+ " no longer has any active realms and has therefore been removed");

						gameDAO.delete(game);
					}
				}));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void allowAccountType(Game game, Realm realm, AccountType type) {
		if (!isValidAccountType(game, realm, type)) {
			AllowedAccountType accountType = new AllowedAccountType();
			accountType.setType(type);
			accountType.setGame(game);
			accountType.setRealm(realm);
			allowedAccountTypeDAO.save(accountType);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void disallowAccountType(Game game, Realm realm, AccountType type) {
		if (isValidAccountType(game, realm, type)) {
			AllowedAccountTypeFilter filter = new AllowedAccountTypeFilter();

			filter.game(game);
			filter.realm(realm);
			filter.type(type);

			for (AllowedAccountType atype : allowedAccountTypeDAO
					.findByFilter(filter)) {
				allowedAccountTypeDAO.delete(atype);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setGameSupervisor(Game _game, User user) {
		gameDAO.load(_game.getId()).forEach(game -> {

			if (MemberUtil.isMember(user) && user.getRank() != Rank.TRIAL) {
				boolean wasReplaced = game.getCoordinator() == null;
				if (game.getCoordinator() != null && !game.getCoordinator()
						.equals(user)) {
					notificationService.notifyUser(game.getCoordinator(),
							"You are no longer supervisor for the game " + game
									.getName());
					wasReplaced = true;
				}

				game.setCoordinator(user);

				if (wasReplaced) {
					notificationService.notifyUser(game.getCoordinator(),
							"You are now supervisor for the game " + game
									.getName());
					logService.logUserAction(game.getCoordinator(), "Game",
							"User is now supervisor for game " + game
									.getName());
				}
				gameDAO.update(game);
				gameDAO.flush();
			}
		});

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void expirePetitions() {
		for (GamePetition petition : gamePetitionDAO.findAll()) {
			checkPetitionExpired(petition);
		}

	}
}
