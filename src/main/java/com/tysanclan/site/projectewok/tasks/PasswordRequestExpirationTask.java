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

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.PasswordRequest;
import com.tysanclan.site.projectewok.entities.dao.PasswordRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.PasswordRequestFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class PasswordRequestExpirationTask extends
        PeriodicTask {
	/**
     * 
     */
	public PasswordRequestExpirationTask() {
		super("Password expiration cleanup", "Expiration",
		        ExecutionMode.DAILY);

	}

	@SpringBean
	private PasswordRequestDAO passwordRequestDAO;

	@SpringBean
	private UserService userService;

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.DAY_OF_YEAR, -3);

		PasswordRequestFilter filter = new PasswordRequestFilter();
		filter.setDateBefore(cal.getTime());

		List<PasswordRequest> expiredRequests = passwordRequestDAO
		        .findByFilter(filter);
		for (PasswordRequest passwordRequest : expiredRequests) {
			userService.expireRequest(passwordRequest);
		}
	}

}
