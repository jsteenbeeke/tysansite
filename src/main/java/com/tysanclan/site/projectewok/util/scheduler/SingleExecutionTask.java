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
package com.tysanclan.site.projectewok.util.scheduler;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class SingleExecutionTask extends TysanTask {
	/**
     * 
     */
	protected SingleExecutionTask(String name,
	        String description) {
		super(name, description);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#getQuartzTrigger()
	 */
	@Override
	public Trigger getQuartzTrigger() {
		return new SimpleTrigger(getName(), getGroup(), 0,
		        0);
	}

}
