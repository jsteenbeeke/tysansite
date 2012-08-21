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

import org.apache.wicket.markup.html.WebMarkupContainer;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class PupilPanel extends TysanOverviewPanel<User> {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PupilPanel(String id, User pupil) {
		super(id, ModelMaker.wrap(pupil), "Mentor");

		if (pupil == null || pupil.getMentor() == null) {
			add(new WebMarkupContainer("contact"));
		} else {
			add(new UserContactInfoPanel("contact", pupil.getMentor()));
		}

		setVisible(getUser().getRank() == Rank.TRIAL);

		if (getUser().getRank() == Rank.TRIAL) {
			Calendar cal = DateUtil.getCalendarInstance();
			cal.add(Calendar.DAY_OF_MONTH, -3);

			if (cal.getTime().before(getUser().getJoinDate())) {
				requiresAttention();
			}

		}
	}
}
