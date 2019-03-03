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
package com.tysanclan.site.projectewok.event.handlers;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;
import com.tysanclan.site.projectewok.entities.filter.RealmFilter;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;
import io.vavr.collection.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ResetOverseerOnTermination
		implements EventHandler<MembershipTerminatedEvent> {
	@Autowired
	private RealmDAO realmDAO;

	public void setRealmDAO(RealmDAO realmDAO) {
		this.realmDAO = realmDAO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		User user = event.getSubject();

		RealmFilter rfilter = new RealmFilter();
		rfilter.overseer(user);

		Seq<Realm> realms = realmDAO.findByFilter(rfilter);
		for (Realm realm : realms) {
			realm.setOverseer(null);

			realmDAO.update(realm);
		}

		return EventResult.ok();
	}
}
