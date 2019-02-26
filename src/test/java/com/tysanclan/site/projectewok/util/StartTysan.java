/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.util;

import com.jeroensteenbeeke.hyperion.solitary.InMemory;

public class StartTysan {
	/**
	 * Main function, starts the jetty server.
	 *
	 * @param args Program arguments
	 */
	public static void main(String[] args) throws Exception {
		InMemory.run("ProjectEwok")
				.withContextPath("/tysan")
				.atPort(8081)
				.ifPresent(InMemory.Handler::waitForKeyPress);
	}
}
