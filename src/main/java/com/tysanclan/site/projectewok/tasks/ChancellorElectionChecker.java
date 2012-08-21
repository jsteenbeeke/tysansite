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

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.entities.ChancellorElection;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.dao.ChancellorElectionDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ChancellorElectionFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class ChancellorElectionChecker extends PeriodicTask {
	@SpringBean
	private DemocracyService democracyService;

	@SpringBean
	private ChancellorElectionDAO chancellorElectionDAO;

	@SpringBean
	private UserDAO userDAO;

	/**
     * 
     */
	public ChancellorElectionChecker() {
		super("Chancellor election", "Democracy",
		        ExecutionMode.DAILY);

	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		Calendar calendar = DateUtil.getCalendarInstance();
		calendar.add(Calendar.MONTH, -6);

		ChancellorElectionFilter filter = new ChancellorElectionFilter();
		filter.setStartBefore(calendar.getTime());
		filter.addOrderBy("start", false);

		ChancellorElectionFilter filter2 = new ChancellorElectionFilter();
		filter2.setStartAfter(calendar.getTime());
		filter2.addOrderBy("start", false);

		ChancellorElection current = democracyService
		        .getCurrentChancellorElection();

		UserFilter ufilter = new UserFilter();
		ufilter.addRank(Rank.CHANCELLOR);

		if (chancellorElectionDAO.countAll() == 0
		        || (chancellorElectionDAO
		                .countByFilter(filter) > 0
		                && chancellorElectionDAO
		                        .countByFilter(filter2) == 0 && current == null)
		        || userDAO.countByFilter(ufilter) == 0) {
			democracyService.createChancellorElection();
		}
	}

}
