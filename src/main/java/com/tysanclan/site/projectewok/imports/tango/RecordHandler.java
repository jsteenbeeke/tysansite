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
package com.tysanclan.site.projectewok.imports.tango;

/**
 * An import record processor interface.
 * 
 * @author Jeroen Steenbeeke
 */
public interface RecordHandler {
	public static final String VALUE_TRUE = "TRUE";

	/**
	 * Process a given record
	 * 
	 * @param data
	 *            The record to process
	 * @param callback
	 *            A callback mechanism to obtain information about earlier
	 *            imports
	 * @return <code>true</code> if succesful, <code>false</code> otherwise
	 */
	public boolean handle(String[] data, TangoImporterCallback callback);

	/**
	 * @return The String that is used as the first part of the record handled
	 *         by this recordhandler
	 */
	public String getRecordDescriptor();

	public void cleanup();
}
