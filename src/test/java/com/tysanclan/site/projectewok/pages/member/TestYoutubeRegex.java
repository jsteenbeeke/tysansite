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
package com.tysanclan.site.projectewok.pages.member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Test;

/**
 * @author Jeroen Steenbeeke
 */
public class TestYoutubeRegex {
	public static final String url1 = "http://www.youtube.com/watch?v=j2-mi2WH-cg";
	public static final String url2 = "http://www.youtube.com/watch?v=0ETrXgNDG_U&feature=related";

	@Test
	public void testRegex() {
		Matcher m1 = YouTubeUrlValidator.REGEX
		        .matcher(url1);
		Matcher m2 = YouTubeUrlValidator.REGEX
		        .matcher(url2);

		assertTrue(m1.matches());
		assertTrue(m2.matches());

		assertEquals(3, m1.groupCount());

		assertEquals("j2-mi2WH-cg", m1.group(1));
	}
}
