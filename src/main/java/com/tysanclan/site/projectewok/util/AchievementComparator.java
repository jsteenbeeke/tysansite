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
package com.tysanclan.site.projectewok.util;

import java.util.Comparator;

import com.tysanclan.site.projectewok.entities.Achievement;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Group;

/**
 * @author Jeroen Steenbeeke
 */
public class AchievementComparator implements Comparator<Achievement> {
	public static final AchievementComparator INSTANCE = new AchievementComparator();

	@Override
	public int compare(Achievement o1, Achievement o2) {
		Game g1 = o1.getGame();
		Game g2 = o2.getGame();

		Group gr1 = o1.getGroup();
		Group gr2 = o2.getGroup();

		int cat1 = g1 == null ? (gr1 == null ? 0 : 2) : 1;
		int cat2 = g2 == null ? (gr2 == null ? 0 : 2) : 1;

		int comp = Integer.valueOf(cat1).compareTo(cat2);

		if (comp == 0) {
			switch (cat1) {
				case 0:
					return o1.getName().compareTo(o2.getName());
				case 1:
					if (g1 != null && g2 != null) {
						comp = g1.getName().compareTo(g2.getName());
						if (comp == 0)
							return o1.getName().compareTo(o2.getName());
					}
					break;
				case 2:
					if (gr1 != null && gr2 != null) {
						comp = gr1.getName().compareTo(gr2.getName());
						if (comp == 0)
							return o1.getName().compareTo(o2.getName());
					}
					break;

			}
		}

		return comp;
	}
}
