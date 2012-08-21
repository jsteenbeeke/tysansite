/*
 * Copyright 2010-2011 Jeroen Steenbeeke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
