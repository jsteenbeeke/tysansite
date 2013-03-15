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

import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.dao.InactivityNotificationDAO;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class WarnInactiveMembersTask extends PeriodicTask {
	@SpringBean
	private InactivityNotificationDAO inactivityDAO;

	@SpringBean
	private UserService userService;

	public WarnInactiveMembersTask() {
		super("Send warning e-mail to inactive members", "Membership",
				ExecutionMode.HOURLY);
	}

	@Override
	public void run() {
		List<Long> inactives = inactivityDAO.getUnnotifiedInactiveUsers();

		for (Long userId : inactives) {
			userService.warnUserForInactivity(userId);
		}
	}
}
