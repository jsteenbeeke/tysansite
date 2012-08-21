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
package com.tysanclan.site.projectewok.util.scheduler;

import org.quartz.Trigger;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class TysanTask {
	private final String name;
	private final String group;

	/**
     * 
     */
	protected TysanTask(String name, String group) {
		this.name = name;
		this.group = group;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @return The Quartz trigger for executing
	 */
	public abstract Trigger getQuartzTrigger();

	/**
	 * Executes the task
	 */
	public abstract void run();

	/**
	 * Called at the end of task execution
	 */
	public void cleanUp() {
		// Do nothing, overriding method may do cleanup actions here
	}
}
