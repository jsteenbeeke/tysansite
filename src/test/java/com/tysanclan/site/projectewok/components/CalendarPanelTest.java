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

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * @author Jeroen Steenbeeke
 */
public class CalendarPanelTest {
	@Test
	public void makeCalendar() {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH, 1);

		Map<Integer, List<Integer>> weeksMap = new TreeMap<Integer, List<Integer>>();

		int month = cal.get(Calendar.MONTH);
		while (cal.get(Calendar.MONTH) == month) {
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			int day = cal.get(Calendar.DAY_OF_MONTH);

			if (weeksMap.containsKey(week)) {
				weeksMap.get(week).add(day);
			} else {
				List<Integer> newList = new LinkedList<Integer>();
				newList.add(day);
				weeksMap.put(week, newList);
			}

			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		for (int week : weeksMap.keySet()) {
			System.out.println("Week " + week);
			for (int day : weeksMap.get(week)) {
				System.out.print("\t" + day);
			}
			System.out.println();
			System.out.println();
		}

		assertTrue(true);
	}
}
