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
package com.tysanclan.site.projectewok.entities.dao;

import java.util.List;

import com.tysanclan.site.projectewok.dataaccess.EwokDAO;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;

/**
 * A DAO for handling users
 * 
 * @author Jeroen Steenbeeke
 */
public interface UserDAO extends EwokDAO<User> {
	/**
	 * Counts the number of users holding a given rank
	 * 
	 * @param rank
	 *            The rank to count
	 * @return The number of users holding that rank
	 */
	public int countByRank(Rank rank);

	/**
	 * Loads the user based on the username and password
	 * 
	 * @param username
	 *            The username of the requested user
	 * @param password
	 *            The password of the requested user
	 * @return The requested user, or <code>null</code> if not found
	 */
	public abstract User load(String username, String password);

	public abstract List<User> getTrialMembersReadyForVote();

	public List<User> findByRank(Rank senator);
}
