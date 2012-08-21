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

import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.entities.GroupLeaderElection;
import com.tysanclan.site.projectewok.entities.dao.GroupLeaderElectionDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.GroupLeaderElectionFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class GroupLeaderElectionResolutionTask extends
        PeriodicTask {

	@SpringBean
	private GroupLeaderElectionDAO groupLeaderElectionDAO;

	@SpringBean
	private DemocracyService democracyService;

	/**
     * 
     */
	public GroupLeaderElectionResolutionTask() {
		super("Group Leader Election Resolution",
		        "Democracy", ExecutionMode.DAILY);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		GroupLeaderElectionFilter filter = new GroupLeaderElectionFilter();
		Calendar twoWeeksAgo = DateUtil
		        .getCalendarInstance();
		Calendar threeWeeksAgo = DateUtil
		        .getCalendarInstance();
		twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);
		threeWeeksAgo.add(Calendar.WEEK_OF_YEAR, -3);
		filter.setStartBefore(twoWeeksAgo.getTime());
		filter.setStartAfter(threeWeeksAgo.getTime());

		List<GroupLeaderElection> elections = groupLeaderElectionDAO
		        .findByFilter(filter);
		for (GroupLeaderElection election : elections) {
			democracyService
			        .resolveGroupLeaderElection(election);
		}
	}

}
