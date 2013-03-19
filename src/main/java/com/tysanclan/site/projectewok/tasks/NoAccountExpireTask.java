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

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * Special task that bumps account activity every hour, so no expired mails go
 * out
 * 
 * @author Jeroen Steenbeeke
 */
public class NoAccountExpireTask extends PeriodicTask {
	@SpringBean
	private MembershipService membershipService;

	/**
	 * 
	 */
	public NoAccountExpireTask() {
		super("Account bumper", "Debug", ExecutionMode.HOURLY);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		membershipService.bumpAccounts();

	}

}
