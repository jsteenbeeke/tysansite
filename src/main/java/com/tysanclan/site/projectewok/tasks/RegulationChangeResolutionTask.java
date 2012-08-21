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
import com.tysanclan.site.projectewok.entities.RegulationChange;
import com.tysanclan.site.projectewok.entities.dao.RegulationChangeDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.RegulationChangeFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class RegulationChangeResolutionTask extends PeriodicTask {
	@SpringBean
	private RegulationChangeDAO regulationChangeDAO;

	@SpringBean
	private DemocracyService democracyService;

	/**
	 * 
	 */
	public RegulationChangeResolutionTask() {
		super("Resolve regulation changes", "Democracy",
				ExecutionMode.ONCE_EVERY_FOUR_HOURS);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		RegulationChangeFilter filter = new RegulationChangeFilter();
		Calendar cal = DateUtil.getMidnightCalendarInstance();
		cal.add(Calendar.WEEK_OF_YEAR, -1);
		filter.setStartBefore(cal.getTime());

		for (RegulationChange change : regulationChangeDAO.findByFilter(filter)) {
			democracyService.resolveRegulationVote(change);
		}

	}

}
