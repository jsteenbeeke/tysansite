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
package com.tysanclan.site.projectewok.beans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class AuthenticationServiceImpl implements
		com.tysanclan.site.projectewok.beans.AuthenticationService {
	@Autowired
	private UserDAO userDAO;

	/**
	 * Create a new mock authentication service
	 */
	public AuthenticationServiceImpl() {
	}

	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.AuthenticationService#isValidMember(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isValidMember(String username, String password) {
		User u = userDAO.load(username, password);

		if (u != null) {
			return u.getRank() != Rank.BANNED && u.getRank() != Rank.FORUM
					&& u.getRank() != Rank.HERO;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.AuthenticationService#isValidUser(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean isValidUser(String username, String password) {
		User u = userDAO.load(username, password);

		if (u != null) {
			return u.getRank() == Rank.BANNED || u.getRank() == Rank.FORUM
					&& u.getRank() != Rank.HERO;
		}

		return false;
	}

	/**
	 * @param userDAO
	 *            the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
