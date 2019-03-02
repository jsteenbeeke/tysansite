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
package com.tysanclan.site.projectewok;

/**
 * @author Jeroen Steenbeeke
 */
public class TysanSecurityException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of <code>TysanSecurityException</code> without
	 * detail message.
	 */
	public TysanSecurityException() {
	}

	/**
	 * Constructs an instance of <code>TysanSecurityException</code> with the
	 * specified detail message.
	 *
	 * @param msg
	 *            the detail message.
	 */
	public TysanSecurityException(String msg) {
		super(msg);
	}
}
