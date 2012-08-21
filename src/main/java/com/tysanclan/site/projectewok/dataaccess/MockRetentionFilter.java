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
package com.tysanclan.site.projectewok.dataaccess;

import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 * A class that filters results based on the filter implementing it
 * 
 * @author Jeroen Steenbeeke
 * @param <T>
 *            The type of object used for this retentionfilter
 */
public abstract class MockRetentionFilter<T extends DomainObject> {
	/**
	 * Determines whether or not this object was part of the filter
	 * 
	 * @param t
	 *            The object to check
	 * @return <code>true</code> if the object matches the filter, or
	 *         <code>false</code> otherwise
	 */
	public abstract boolean retain(T t);
}
