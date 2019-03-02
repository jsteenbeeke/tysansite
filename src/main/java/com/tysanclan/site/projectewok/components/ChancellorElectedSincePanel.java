/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
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

import com.tysanclan.site.projectewok.entities.ChancellorElection;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ChancellorElectionDAO;
import com.tysanclan.site.projectewok.entities.filter.ChancellorElectionFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
public class ChancellorElectedSincePanel
		extends ElectedSincePanel<ChancellorElection> {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ChancellorElectionDAO chancellorElectionDAO;

	/**
	 *
	 */
	public ChancellorElectedSincePanel(String id, User user) {
		super(id, user);
	}

	/**
	 * @see com.tysanclan.site.projectewok.components.ElectedSincePanel#getElectionsSortedByDate()
	 */
	@Override
	public List<ChancellorElection> getElectionsSortedByDate() {
		ChancellorElectionFilter filter = new ChancellorElectionFilter();
		filter.start().orderBy(false);

		return chancellorElectionDAO.findByFilter(filter).asJava();
	}

	/**
	 * @see com.tysanclan.site.projectewok.components.ElectedSincePanel#isWinner(com.tysanclan.site.projectewok.entities.Election,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	public boolean isWinner(ChancellorElection election, User user) {
		return election != null && election.getWinner() != null && election
				.getWinner().equals(user);
	}

	@Override
	public Date getInitialDate(User user) {
		Calendar calendar = DateUtil.getMidnightCalendarInstance();
		calendar.set(Calendar.YEAR, 2008);
		calendar.set(Calendar.MONTH, Calendar.SEPTEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, 10);

		return calendar.getTime();
	}
}
