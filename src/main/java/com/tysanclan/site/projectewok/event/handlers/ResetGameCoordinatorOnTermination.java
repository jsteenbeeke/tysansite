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
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.entities.filter.GameFilter;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;
import io.vavr.collection.Seq;

public class ResetGameCoordinatorOnTermination
		implements EventHandler<MembershipTerminatedEvent> {
	private GameDAO gameDAO;

	public void setGameDAO(GameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}

	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		GameFilter gfilter = new GameFilter();
		gfilter.coordinator(event.getSubject());

		Seq<Game> games = gameDAO.findByFilter(gfilter);
		for (Game game : games) {
			game.setCoordinator(null);

			gameDAO.update(game);
		}

		return EventResult.ok();
	}
}
