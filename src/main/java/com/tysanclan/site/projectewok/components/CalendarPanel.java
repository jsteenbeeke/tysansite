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

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class CalendarPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private final Calendar calendar;

	/**
	 * 
	 */
	public CalendarPanel(String id, final Calendar calendar) {
		super(id);
		this.calendar = calendar;

		SortedMap<Integer, List<Date>> layout = getCalendarLayout(calendar);
		List<Integer> weeks = new LinkedList<Integer>();
		weeks.addAll(layout.keySet());

		add(new ListView<Integer>("weeks", weeks) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Integer> item) {
				SortedMap<Integer, List<Date>> calendarLayout = getCalendarLayout(calendar);
				List<Date> days = calendarLayout.get(item.getModelObject());

				item.add(createDaysListView(days, item.getModelObject()));

			}

		});
	}

	/**
	 * @return the calendar
	 */
	public Calendar getCalendar() {
		return calendar;
	}

	private ListView<Date> createDaysListView(List<Date> days, final int week) {
		return new ListView<Date>("days", days) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Date> item) {
				item.add(getDateComponent("content", item.getModelObject()));
				if (item.getModelObject() != null) {
					item.add(AttributeModifier.replace("class",
							new Model<String>("Calendar")));
				}
			}

		};
	}

	public abstract Component getDateComponent(String id, Date date);

	private SortedMap<Integer, List<Date>> getCalendarLayout(
			Calendar originalCalendar) {
		Calendar cal = (Calendar) originalCalendar.clone();
		cal.setTime(DateUtil.getMidnightDate(cal.getTime()));

		cal.set(Calendar.DAY_OF_MONTH, 1);

		SortedMap<Integer, List<Date>> weeksMap = new TreeMap<Integer, List<Date>>();

		int month = cal.get(Calendar.MONTH);

		while (cal.get(Calendar.MONTH) == month) {
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			if (week == 1 && month == Calendar.DECEMBER) {
				week = 53;
			}

			int of_week = cal.get(Calendar.DAY_OF_WEEK);
			int firstday = cal.getFirstDayOfWeek();

			if (weeksMap.containsKey(week)) {
				weeksMap.get(week).add(cal.getTime());
			} else {
				List<Date> newList = new LinkedList<Date>();
				if (of_week != firstday) {
					for (int i = firstday; i < of_week; i++) {
						newList.add(null);
					}
				}

				newList.add(cal.getTime());
				weeksMap.put(week, newList);
			}

			cal.add(Calendar.DAY_OF_MONTH, 1);

			if (cal.get(Calendar.MONTH) != month && weeksMap.containsKey(week)) {
				while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
					weeksMap.get(week).add(null);
					cal.add(Calendar.DAY_OF_MONTH, 1);
				}
			}
		}

		return weeksMap;
	}
}
