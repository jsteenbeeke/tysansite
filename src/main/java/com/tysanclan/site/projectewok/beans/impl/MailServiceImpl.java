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

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
@Component
class MailServiceImpl implements
		com.tysanclan.site.projectewok.beans.MailService {
	private Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MailService#sendHTMLMail(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	public void sendHTMLMail(String to, String subject, String body) {
		MimeMessage msg = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(msg);

		try {
			helper.setFrom(MAIL_SENDER);
			helper.addTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);

			this.mailSender.send(msg);
		} catch (MailException ex) {
			logger.error(ex.getMessage(), ex);
		} catch (MessagingException e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public String getActivationMailBody(String username, String activationKey) {
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(username).append(",<br/><br/>");
		body.append("You have succesfully created an account for the Tysan Clan Forums. ");
		body.append("Before you can begin using this account however, you need to activate it first. Please ");
		body.append("visit https://www.tysanclan.com/activation/")
				.append(activationKey).append("/ to activate your account. ");
		body.append("Once this is done you can use your account normally.<br /><br />");
		body.append("With regards,<br/><br/>")
				.append("The Tysan Clan<br /><br />")
				.append("(THIS E-MAIL WAS AUTOMATICALLY GENERATED)");

		return body.toString();

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MailService#getConfirmationMailBody(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getConfirmationMailBody(String username, String activationKey) {
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(username).append(",<br/><br/>");

		body.append("You have requested to change your")
				.append(" e-mail address of your account on")
				.append(" the Tysan Clan website. ")
				.append("To confirm this change, log into your")
				.append(" account and enter the following confirmation")
				.append(" code:<br /><br />");
		body.append("Confirmation code: ").append(activationKey)
				.append("<br /><br />");
		body.append("If you do not do this within 3 days of your")
				.append(" request, your e-mail address will remain unchanged.<br /><br />");
		body.append("With regards,<br /><br />");
		body.append("The Tysan Clan<br /><br />");
		body.append("(THIS E-MAIL WAS AUTOMATICALLY GENERATED)");

		return body.toString();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MailService#getPasswordRequestMailBody(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getPasswordRequestMailBody(String username,
			String activationKey) {
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(username).append(",<br/><br/>");
		body.append("Someone has requested a password reset for your account. If you did not request such a reset, ");
		body.append("please disregard this e-mail. ");
		body.append("If you did request this reset, Then please ");
		body.append("visit https://www.tysanclan.com/resetpassword/")
				.append(activationKey).append("/ to proceed.<br /><br />");
		body.append("With regards,<br/><br/>")
				.append("The Tysan Clan<br /><br />")
				.append("(THIS E-MAIL WAS AUTOMATICALLY GENERATED)");

		return body.toString();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MailService#getJoinApplicationMail(com.tysanclan.site.projectewok.entities.JoinApplication,
	 *      boolean, int, int)
	 */
	@Override
	public String getJoinApplicationMail(JoinApplication application,
			boolean accepted, int inFavor, int total) {
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(application.getApplicant().getUsername())
				.append(",<br/><br/>");

		if (accepted) {
			body.append("We are pleased to inform you that your request")
					.append(" to join the Tysan Clan has been accepted. ")
					.append("Your username and password will remain the same ")
					.append("as your forum account, but now you will have ")
					.append("access to our special member area. ")
					.append("Please keep in mind that you must first complete ")
					.append("a 14 day trial period before you will be")
					.append(" permanently accepted as a member. ")
					.append("<br /><br />");
		} else {
			body.append("We regret to inform you that your request to join ")
					.append("the Tysan Clan has been denied. ")
					.append("<br /><br />");
			if (application.getMentor() == null) {
				body.append("As part of the join process, one of our ")
						.append("members must volunteer to become your mentor ")
						.append("within the clan. Unfortunately, no member has ")
						.append("volunteered, so we cannot accept your application ")
						.append("at this time.").append("<br /><br />");
			}

		}

		if (total == 0) {
			body.append(
					"None of our Senators have voted regarding your join application.")
					.append("<br /><br />");
		} else {
			int fraction = (100 * inFavor) / total;

			body.append(total)
					.append(" of our Senators have voted regarding your join application, ")
					.append("of which ").append(inFavor)
					.append(" were in your favor, giving you a percentage of ")
					.append(fraction).append("% (at least 66% is required).")
					.append("<br /><br />");
		}

		body.append("With regards,<br/><br/>")
				.append("The Tysan Clan<br /><br />")
				.append("(THIS E-MAIL WAS AUTOMATICALLY GENERATED)");

		return body.toString();

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MailService#getInactivityExpirationMail(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	public String getInactivityExpirationMail(User user) {
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(user.getUsername()).append(",<br/><br/>");
		body.append("We regret to inform you that you are no ")
				.append("longer a member of the Tysan Clan. ")
				.append("<br /><br />");
		if (user.getRank() == Rank.TRIAL) {
			body.append("As is written in the charter, trial members of ")
					.append("the Tysan Clan are required to log in to our site ")
					.append("at least once every 7 days. Because of this, your ")
					.append("membership has been terminated")
					.append("<br /><br />");

		} else if (user.isRetired()) {
			body.append("As is written in the charter, retired members of ")
					.append("the Tysan Clan are required to log in to our site ")
					.append("at least once a year. Because of this, your ")
					.append("membership has been terminated")
					.append("<br /><br />");
		} else if (user.isVacation()) {
			body.append("As is written in the charter, members of ")
					.append("the Tysan Clan who have activated vacation mode are required to log in to our site ")
					.append("at least once every 30 days. Because of this, your ")
					.append("membership has been terminated")
					.append("<br /><br />");
		} else {
			body.append("As is written in the charter, active members of ")
					.append("the Tysan Clan are required to log in to our site ")
					.append("at least once every 14 days. Because of this, your ")
					.append("membership has been terminated")
					.append("<br /><br />");
		}
		body.append("You will still be able to log in to our website, ")
				.append("but you will be an ordinary forum member. ")
				.append("You are of course welcome to reapply for membership. ")
				.append("<br /><br />");
		body.append("With regards,<br/><br/>")
				.append("The Tysan Clan<br /><br />")
				.append("(THIS E-MAIL WAS AUTOMATICALLY GENERATED)");

		return body.toString();
	}

	@Override
	public String getInactivityWarningMail(User user) {
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(user.getUsername()).append(",<br/><br/>");
		body.append("We noticed that you haven't logged on to our website ")
				.append("for quite a while. ").append("<br /><br />");
		if (user.getRank() == Rank.TRIAL) {
			body.append("As is written in the charter, trial members of ")
					.append("the Tysan Clan are required to log in to our site ")
					.append("at least once every 7 days, and we haven't seen you for at least 5 days.")
					.append("<br /><br />");

		} else if (user.isRetired()) {
			body.append("As is written in the charter, retired members of ")
					.append("the Tysan Clan are required to log in to our site ")
					.append("at least once a year, and we haven't seen you for at least 11 months.")
					.append("<br /><br />");
		} else if (user.isVacation()) {
			body.append("As is written in the charter, members of ")
					.append("the Tysan Clan who are on vacation have to log in to our site ")
					.append("at least once every 60 days, and we haven't seen you for at least 55 days.")
					.append("<br /><br />");

		} else {
			body.append("As is written in the charter, active members of ")
					.append("the Tysan Clan are required to log in to our site ")
					.append("at least once every 14 days. We haven't seen you for at least 12 days.")
					.append("<br /><br />");
		}
		body.append(
				"If you wish to remain a member, please go to our website at http://www.tysanclan.com/ at your earliest convenience. ")
				.append("<br /><br />");
		body.append("With regards,<br/><br/>")
				.append("The Tysan Clan<br /><br />")
				.append("(THIS E-MAIL WAS AUTOMATICALLY GENERATED)");

		return body.toString();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MailService#getAcceptanceVoteNotificationMail(com.tysanclan.site.projectewok.entities.User,
	 *      boolean, int, int)
	 */
	@Override
	public String getAcceptanceVoteNotificationMail(User user,
			boolean accepted, int inFavor, int total) {
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(user.getUsername()).append(",<br/><br/>");
		if (accepted) {
			body.append(
					"We are happy to inform you that your trial membership ")
					.append("has been completed, and that you have been granted a full membership ")
					.append("with the rank of Junior Member.<br /><br />");
		} else {
			body.append("We regret to inform you that your trial membership ")
					.append("has ended, but that you have not been granted a full membership. ")
					.append("Your account has been reverted to a forum account. <br /><br />");
		}

		if (total == 0) {
			body.append("No members have participated in the vote. This is ")
					.append("somewhat unusual, but we cannot accept members without ")
					.append("at least someone voting in their favor. We recommend asking ")
					.append("for clarification on our forums. <br /><br />");
		} else {
			body.append("There were ").append(total)
					.append(" members who participated in the vote, and ")
					.append(inFavor)
					.append(" of these votes were in your favor.<br /><br />");
		}

		body.append("With regards,<br/><br/>")
				.append("The Tysan Clan<br /><br />")
				.append("(THIS E-MAIL WAS AUTOMATICALLY GENERATED)");

		return body.toString();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.MailService#getEmailChangeMailBody(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public String getEmailChangeMailBody(String username, String activationKey) {
		StringBuilder body = new StringBuilder();
		body.append("Dear ").append(username).append(",<br/><br/>");
		body.append("You have requested to change your e-mail. To complete the change, you must log in ");
		body.append("to the site and enter the following code:<br/><br/>");
		body.append(activationKey);
		body.append("<br /><br />");
		body.append("With regards,<br/><br/>")
				.append("The Tysan Clan<br /><br />")
				.append("(THIS E-MAIL WAS AUTOMATICALLY GENERATED)");

		return body.toString();

	}
}
