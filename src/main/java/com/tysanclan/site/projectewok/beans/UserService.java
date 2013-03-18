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

import com.tysanclan.site.projectewok.entities.Activation;
import com.tysanclan.site.projectewok.entities.EmailChangeConfirmation;
import com.tysanclan.site.projectewok.entities.PasswordRequest;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;

/**
 * A service for various User-related actions
 * 
 * @author Jeroen Steenbeeke
 */
public interface UserService {
	/**
	 * Creates a new website user, granting the rank of forum user
	 * 
	 * @param username
	 *            The username of the created user
	 * @param password
	 *            The password of the created user
	 * @param email
	 *            The email of the created user
	 * @return The created user, or <code>null</code> if the user already exists
	 */
	public User createUser(String username, String password, String email);

	/**
	 * Checks if a given username is taken
	 * 
	 * @param username
	 *            The username to check for
	 * @return <code>true</code> If the user exists, <code>false</code>
	 *         otherwise
	 */
	public boolean hasUser(String username);

	/**
	 * Sets a user's rank and join time as indicated by an import
	 * 
	 * @param user_id
	 *            The ID of the user to modify
	 * @param rank
	 *            The rank to assign to the user
	 * @param joinTime
	 *            The join time of the user
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
	public boolean setUserImportData(long user_id, Rank rank, long joinTime);

	/**
	 * Sets the retirement status of a user
	 * 
	 * @param user_id
	 *            The ID of the user to modify
	 * @param retirement
	 *            Whether or not the user should be retired
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
	public boolean setUserRetirement(long user_id, boolean retirement);

	public boolean setUserAvatar(long user_id, String avatarURL);

	public boolean setUserCustomTitle(long user_id, String customTitle);

	public boolean setUserSignature(long user_id, String signature);

	public Activation getActivationByUser(User user);

	public boolean activateAccount(Activation activation);

	public void expireActivation(Activation activation);

	public boolean setUserMail(User user, String mail);

	public PasswordRequest generatePasswordRequest(User user);

	public void processPasswordReset(PasswordRequest request, String password);

	public void expireRequest(PasswordRequest passwordRequest);

	public void expireConfirmation(EmailChangeConfirmation confirmation);

	public EmailChangeConfirmation createEmailChangeRequest(User user,
			String email);

	public void setUserPassword(User user, String newPassword);

	List<User> getInactiveMembers();

	List<User> getMembers();

	List<User> getMembersOnline();

	/**
	 	 */
	long countMembers();

	public void setUserTimezone(Long userId, String timezone);

	public void activateVacationMode(User user);

	public void unbanUser(User unbanner, User user);

	public void banUser(User banner, User user);

	void warnUserForInactivity(Long userId);

	void setUserCollapseForums(Long user_id, boolean collapse);

	public void setPaypalAddress(User user, String paypalAddress);
}
