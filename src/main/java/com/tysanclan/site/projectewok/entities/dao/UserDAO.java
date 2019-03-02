/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.entities.dao;

import com.jeroensteenbeeke.hyperion.meld.DAO;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;

import java.util.List;

/**
 * A DAO for handling users
 *
 * @author Jeroen Steenbeeke
 */
public interface UserDAO extends DAO<User, UserFilter> {
	/**
	 * Counts the number of users holding a given rank
	 *
	 * @param rank
	 *            The rank to count
	 * @return The number of users holding that rank
	 */
	long countByRank(Rank rank);

	/**
	 * Loads the user based on the username and password
	 *
	 * @param username
	 *            The username of the requested user
	 * @param password
	 *            The password of the requested user
	 * @return The requested user, or <code>null</code> if not found
	 */
	User load(String username, String password);

	List<User> getTrialMembersReadyForVote();

	List<User> findByRank(Rank senator);
}
