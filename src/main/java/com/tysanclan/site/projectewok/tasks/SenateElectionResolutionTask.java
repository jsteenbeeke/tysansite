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
import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.SenateElectionFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class SenateElectionResolutionTask extends PeriodicTask {
	@SpringBean
	private SenateElectionDAO senateElectionDAO;

	@SpringBean
	private DemocracyService democracyService;

	/**
	 * 
	 */
	public SenateElectionResolutionTask() {
		super("Senate Election Resolution", "Democracy",
				ExecutionMode.ONCE_EVERY_FOUR_HOURS);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		SenateElectionFilter filter = new SenateElectionFilter();
		Calendar twoWeeksAgo = DateUtil.getCalendarInstance();
		Calendar threeWeeksAgo = DateUtil.getCalendarInstance();
		twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);
		threeWeeksAgo.add(Calendar.WEEK_OF_YEAR, -3);
		filter.setStartBefore(twoWeeksAgo.getTime());
		filter.setStartAfter(threeWeeksAgo.getTime());
		List<SenateElection> elections = senateElectionDAO.findByFilter(filter);
		for (SenateElection election : elections) {
			democracyService.resolveSenateElection(election);
		}

	}

}
