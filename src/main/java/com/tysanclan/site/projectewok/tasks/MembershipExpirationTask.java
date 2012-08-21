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
package com.tysanclan.site.projectewok.tasks;

import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.fortuityframework.core.dispatch.IEventBroker;
import com.tysanclan.site.projectewok.beans.MailService;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.event.MemberStatusEvent;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class MembershipExpirationTask extends PeriodicTask {
	@SpringBean
	private UserService userService;
	@SpringBean
	private MembershipService membershipService;

	@SpringBean
	private MailService mailService;

	@SpringBean
	private IEventBroker broker;

	/**
	 * Creates a new task that checks for expired memberships
	 */
	public MembershipExpirationTask() {
		super("Membership Expiration", "Members", ExecutionMode.HOURLY);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		List<User> expiredMembers = userService.getInactiveMembers();
		for (User user : expiredMembers) {
			membershipService.terminateMembership(user);
			String mailBody = mailService.getInactivityExpirationMail(user);

			mailService.sendHTMLMail(user.getEMail(),
					"Tysan Clan Membership Expired", mailBody);

			broker.dispatchEvent(new MemberStatusEvent(
					com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType.INACTIVITY_TIMEOUT,
					user));
		}
	}
}
