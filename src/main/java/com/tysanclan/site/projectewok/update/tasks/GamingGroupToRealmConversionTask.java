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
package com.tysanclan.site.projectewok.update.tasks;

import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.update.DefaultUpdateTask;
import com.tysanclan.site.projectewok.update.Update;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * @author Jeroen Steenbeeke
 */
public class GamingGroupToRealmConversionTask extends DefaultUpdateTask {
	/**
	 *
	 */
	public GamingGroupToRealmConversionTask() {
		super("1.1");
	}

	@SuppressWarnings("unchecked")
	@Update
	public void convertGamingGroups(Session session) {
		Realm realm = new Realm();
		realm.setChannel("Op Tysan");
		realm.setGames(session.createCriteria(Game.class)
				.add(Restrictions.eq("name", "Diablo 2")).list());
		realm.setName("USEast");
		realm.setOverseer(null);

		session.save(realm);

	}
}
