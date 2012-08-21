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
package com.tysanclan.site.projectewok.beans;

import java.util.List;

import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.GamePetition;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.UserGameRealm;

/**
 * @author Jeroen Steenbeeke
 */
public interface GameService {

	public Game createGame(String name, byte[] image);

	public List<Game> getActiveGames();

	int getRequiredPetitionSignatures();

	GamePetition createPetition(User user, String name, Realm realm, byte[] icon);

	GamePetition createPetition(User user, String name, String realmName,
			byte[] icon);

	Game createGameFromPetition(GamePetition petition);

	void signPetition(GamePetition petition, User user);

	List<UserGameRealm> getPlayedGames(User user);

	void addPlayedGame(User user, Game game, Realm realm);

	public void checkPetitionExpired(GamePetition petition);

	public int countPlayers(Game game, Realm realm);

	public void deleteAccount(GameAccount account);

	GameAccount createDiablo2Account(UserGameRealm userGameRealm,
			String username);

	GameAccount createStarCraft2Account(UserGameRealm userGameRealm,
			String username, int characterCode);

	GameAccount createRealIDAccount(UserGameRealm userGameRealm, String email);

	public void removePlayedGame(UserGameRealm userGameRealm);

	public int countActivePlayers(Game game);

	public boolean isGameInactive(Game game);

	public void deleteGame(User user, Game _game);

	public void removeFromRealm(User user, Realm realm, Game game);

	public boolean isValidAccountType(Game game, Realm realm, AccountType type);

	void allowAccountType(Game game, Realm realm, AccountType type);

	void disallowAccountType(Game game, Realm realm, AccountType type);

	GameAccount createMinecraftAccount(UserGameRealm userGameRealm,
			String username);

	GameAccount createLeagueOfLegendsAccount(UserGameRealm ugr, String name);
}
