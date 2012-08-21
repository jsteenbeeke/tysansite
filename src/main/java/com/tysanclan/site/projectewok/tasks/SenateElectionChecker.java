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
import com.tysanclan.site.projectewok.beans.WrapperService;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.SenateElectionFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class SenateElectionChecker extends PeriodicTask {

	@SpringBean
	private DemocracyService democracyService;

	@SpringBean
	private SenateElectionDAO senateElectionDAO;

	@SpringBean
	private WrapperService wrapperService;

	@SpringBean
	private UserDAO userDAO;

	/**
     * 
     */
	public SenateElectionChecker() {
		super("Senate election", "Democracy",
		        ExecutionMode.DAILY);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		Calendar calendar = DateUtil.getCalendarInstance();
		calendar.add(Calendar.MONTH, -6);

		SenateElectionFilter filter = new SenateElectionFilter();
		filter.setStartBefore(calendar.getTime());
		filter.addOrderBy("start", false);

		SenateElectionFilter filter2 = new SenateElectionFilter();
		filter2.setStartAfter(calendar.getTime());
		filter2.addOrderBy("start", false);

		SenateElection current = democracyService
		        .getCurrentSenateElection();

		if (senateElectionDAO.countAll() == 0
		        || (senateElectionDAO.countByFilter(filter) > 0
		                && senateElectionDAO
		                        .countByFilter(filter2) == 0 && current == null)) {
			democracyService.createSenateElection();
		} else {
			SenateElectionFilter filter3 = new SenateElectionFilter();
			filter3.addOrderBy("start", false);
			List<SenateElection> elections = senateElectionDAO
			        .findByFilter(filter3);

			UserFilter filter4 = new UserFilter();
			filter4.addRank(Rank.SENATOR);

			for (SenateElection election : elections) {
				int seats = wrapperService
				        .countElectionWinner(election);

				if (seats > 0) {
					int senators = userDAO
					        .countByFilter(filter4);

					int fraction = (senators * 100) / seats;

					if (fraction < 40) {
						democracyService
						        .createSenateElection();
					}
				}
				break;

			}
		}

	}
}
