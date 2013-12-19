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
package com.jeroensteenbeeke.hyperion.util;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class MapUtil {
	private MapUtil() {

	}

	public static <K, V> void listAdd(Map<K, List<V>> map, K key, V value) {
		List<V> list;

		if (map.containsKey(key)) {
			list = map.get(key);
		} else {
			list = new LinkedList<V>();
		}

		list.add(value);
		map.put(key, list);
	}

	public static <K> void bdAdd(Map<K, BigDecimal> map, K key, BigDecimal value) {
		BigDecimal old;

		if (map.containsKey(key)) {
			old = map.get(key);
		} else {
			old = BigDecimal.ZERO;
		}

		map.put(key, old.add(value));
	}
}
