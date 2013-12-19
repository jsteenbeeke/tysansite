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

import org.apache.wicket.SharedResources;

/**
 * Resource provider that registers Wicket resources on application startup.
 * This is a convenience interface - implementations of this interface can be
 * autowired and use SpringBean annotations
 * 
 * @author Jeroen Steenbeeke
 */
public interface StartupResourceProvider {
	/**
	 * Called to let the provider register resources with the SharedResources
	 * instance
	 * 
	 * @param resources
	 *            The resources to add this provider's resources to
	 */
	void registerResources(SharedResources resources);
}
