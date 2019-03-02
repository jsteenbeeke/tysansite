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
package com.tysanclan.site.projectewok.update;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Jeroen Steenbeeke
 */
public class VersionComparatorTest {
	private VersionComparator comparator;

	@Before
	public void makeComparator() {
		comparator = new VersionComparator();
	}

	@After
	public void setComparatorNull() {
		comparator = null;
	}

	@Test
	public void compareMajors() {
		assertTrue(comparator.compare("3.0", "2.0") > 0);
		assertTrue(comparator.compare("3.0", "1.0") > 0);
	}

	@Test
	public void compareMinors() {
		assertTrue(comparator.compare("3.1", "3.0") > 0);
		assertTrue(comparator.compare("2.5", "2.4") > 0);
	}

	@Test
	public void compareMajorMinor() {
		assertTrue(comparator.compare("3.0", "2.7") > 0);
		assertTrue(comparator.compare("3.0", "1.0") > 0);
	}

	@Test
	public void compareMajorMinorTailLength() {
		assertTrue(comparator.compare("3", "2.7") > 0);
		assertTrue(comparator.compare("3.0", "2.5.1") > 0);
	}

	// Unrelated but so what
	@Test
	public void testScanning() {
		String fqdn = getClass().getName();

		System.out.println(fqdn);
	}

}
