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
package com.tysanclan.site.projectewok.tasks;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.entities.GamePetition;
import com.tysanclan.site.projectewok.entities.RealmPetition;
import com.tysanclan.site.projectewok.entities.dao.GamePetitionDAO;
import com.tysanclan.site.projectewok.entities.dao.RealmPetitionDAO;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

/**
 * @author Jeroen Steenbeeke
 */
public class PetitionExpireTask extends PeriodicTask {
	@SpringBean
	private GamePetitionDAO gamePetitionDAO;

	@SpringBean
	private GameService gameService;

	@SpringBean
	private RealmPetitionDAO realmPetitionDAO;

	@SpringBean
	private RealmService realmService;

	/**
     * 
     */
	public PetitionExpireTask() {
		super("Expires", "Petitions", ExecutionMode.DAILY);
	}

	/**
	 * @see com.tysanclan.site.projectewok.util.scheduler.TysanTask#run()
	 */
	@Override
	public void run() {
		for (GamePetition petition : gamePetitionDAO
		        .findAll()) {
			gameService.checkPetitionExpired(petition);
		}

		for (RealmPetition petition : realmPetitionDAO
		        .findAll()) {
			realmService.checkPetitionExpired(petition);
		}

	}

}
