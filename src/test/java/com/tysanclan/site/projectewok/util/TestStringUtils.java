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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tysanclan.rest.api.util.HashException;
import com.tysanclan.rest.api.util.HashUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class TestStringUtils {
	private static String[][] testValues = new String[][] {
			{ "test", "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3" },
			{ "tysan", "abd779725d749f99831a7b45c2690490da588372" },
			{ "your mom", "deec6a3e9326a373b299fbead22c0339a2e18836" },
			{ "ewok", "7c8f3b3c7be7783f3a7c51250552b7d21dd3c3b5" } };

	@Test
	public void checkHashing() throws HashException {
		for (String[] test : testValues) {
			assertEquals(test[1], HashUtil.sha1Hash(test[0]));
		}
	}
}
