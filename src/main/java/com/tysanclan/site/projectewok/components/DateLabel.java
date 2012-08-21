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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class DateLabel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public DateLabel(String id, Date date) {
		super(id);

		String postfix = "th";

		if (date != null) {
			Calendar cal = DateUtil.getCalendarInstance();
			cal.setTime(date);

			int day = cal.get(Calendar.DAY_OF_MONTH) + 1;

			switch (day) {
				case 1:
				case 21:
				case 31:
					postfix = "st";
					break;
				case 2:
				case 22:
					postfix = "nd";
					break;
				case 3:
				case 23:
					postfix = "rd";
					break;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("d'" + postfix
					+ " of' MMMM yyyy", Locale.US);

			add(new Label("label", sdf.format(date)));
		} else {
			add(new Label("label", "-"));
		}

	}
}
