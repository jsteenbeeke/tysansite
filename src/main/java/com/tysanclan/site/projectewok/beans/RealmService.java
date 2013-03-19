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
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.RealmPetition;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface RealmService {
	List<Realm> getRealms();

	Realm createRealmFromPetition(RealmPetition petition);

	RealmPetition createRealmPetition(String name, User user, Game game);

	@Deprecated
	Realm createRealm(String newRealmName, Game game, User requester);

	int countActivePlayers(Realm realm);

	void deleteRealm(Realm realm, User user);

	boolean isRealmInactive(Realm realm);

	void setSupervisor(Realm realm, User user);

	RealmPetition createRealmPetition(Realm realm, User user, Game game);

	void linkRealmToGame(RealmPetition petition);

	void checkPetitionExpired(RealmPetition petition);

	void signPetition(RealmPetition petition, User user);

	void addPlayedGame(User user, Game game, Realm realm);

	int getRequiredPetitionSignatures();

	void expirePetitions();

}
