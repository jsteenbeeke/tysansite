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
package com.tysanclan.site.projectewok.tasks;

import com.jeroensteenbeeke.hyperion.tardis.scheduler.HyperionTask;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.ServiceProvider;
import com.tysanclan.site.projectewok.TysanTaskGroup;
import com.tysanclan.site.projectewok.beans.PopulationService;

/**
 * Background task for installing various debug users
 * 
 * @author Jeroen Steenbeeke
 */
public class DebugSiteCreationTask extends HyperionTask {
	public DebugSiteCreationTask() {
		super("Create debug users", TysanTaskGroup.DEBUG);
	}

	@Override
	public void run(ServiceProvider provider) {
		provider.getService(PopulationService.class).createDebugSite();

	}

}
