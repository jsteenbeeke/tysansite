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

import org.apache.wicket.markup.html.panel.Panel;

import com.tysanclan.site.projectewok.entities.Election;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class ElectedSincePanel<T extends Election>
        extends Panel {
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
	public ElectedSincePanel(String id, User user) {
		super(id);

		List<T> elections = getElectionsSortedByDate();

		Date date = getInitialDate(user);

		for (T election : elections) {
			if (!isWinner(election, user)) {
				break;
			}

			Calendar calendar2 = DateUtil
			        .getCalendarInstance();
			calendar2.setTime(election.getStart());
			calendar2.add(Calendar.WEEK_OF_YEAR, 2);
			calendar2.add(Calendar.DAY_OF_YEAR, 1);

			date = calendar2.getTime();
		}

		add(new DateLabel("electionDate", date));
	}

	public abstract Date getInitialDate(User user);

	public abstract List<T> getElectionsSortedByDate();

	public abstract boolean isWinner(T election, User user);
}
