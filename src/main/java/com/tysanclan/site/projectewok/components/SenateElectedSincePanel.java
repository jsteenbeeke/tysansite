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
package com.tysanclan.site.projectewok.components;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.SenateElectionFilter;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class SenateElectedSincePanel extends
        ElectedSincePanel<SenateElection> {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private SenateElectionDAO senateElectionDAO;

	/**
     * 
     */
	public SenateElectedSincePanel(String id, User user) {
		super(id, user);
	}

	/**
	 * @see com.tysanclan.site.projectewok.components.ElectedSincePanel#getElectionsSortedByDate()
	 */
	@Override
	public List<SenateElection> getElectionsSortedByDate() {
		SenateElectionFilter filter = new SenateElectionFilter();
		filter.addOrderBy("start", false);

		return senateElectionDAO.findByFilter(filter);
	}

	/**
	 * @see com.tysanclan.site.projectewok.components.ElectedSincePanel#isWinner(com.tysanclan.site.projectewok.entities.Election,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	public boolean isWinner(SenateElection election,
	        User user) {
		return election.getWinners().contains(user);
	}

	@Override
	public Date getInitialDate(User user) {
		Calendar calendar = DateUtil
		        .getMidnightCalendarInstance();
		calendar.set(Calendar.YEAR, 2009);
		calendar.set(Calendar.MONTH, Calendar.FEBRUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 13);

		if (user.getJoinDate().after(calendar.getTime())) {
			calendar.setTime(user.getJoinDate());
			calendar.add(Calendar.DAY_OF_YEAR, 3);
		}

		return calendar.getTime();
	}

}
