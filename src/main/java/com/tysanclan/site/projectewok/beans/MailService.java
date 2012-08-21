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

import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface MailService {
	public final static String MAIL_SENDER = "automailer@tysanclan.com";

	/**
	 * Sends an e-mail to the given address
	 * 
	 * @param to
	 *            The recipient of the e-mail
	 * @param subject
	 *            The subject of the e-mail
	 * @param body
	 *            The HTML body of the e-mail
	 */
	public void sendHTMLMail(String to, String subject, String body);

	/**
	 * Generates the mail body for the activation mail sent to new users.
	 * 
	 * @param username
	 *            The username to address the user with
	 * @param activationKey
	 *            The key needed to activate the e-mail address
	 * @return The full HTML mail body to send to the user
	 */
	public String getActivationMailBody(String username, String activationKey);

	/**
	 * Generates the mail body for the activation mail sent to users who want to
	 * change their e-mail address.
	 * 
	 * @param username
	 *            The username to address the user with
	 * @param activationKey
	 *            The key needed to activate the e-mail address
	 * @return The full HTML mail body to send to the user
	 */
	public String getConfirmationMailBody(String username, String activationKey);

	/**
	 * Generates the mail body for the password request mail sent to users who
	 * want to reset their password.
	 * 
	 * @param username
	 *            The username to address the user with
	 * @param activationKey
	 *            The key needed to activate the e-mail address
	 * @return The full HTML mail body to send to the user
	 */
	public String getPasswordRequestMailBody(String username,
			String activationKey);

	public String getJoinApplicationMail(JoinApplication application,
			boolean accepted, int inFavor, int total);

	public String getAcceptanceVoteNotificationMail(User user,
			boolean accepted, int inFavor, int total);

	public String getInactivityExpirationMail(User user);

	public String getInactivityWarningMail(User user);

	public String getEmailChangeMailBody(String username, String activationKey);
}
