package com.tysanclan.site.projectewok.event.handlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.GameFilter;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class ResetGameCoordinatorOnTermination implements
		EventHandler<MembershipTerminatedEvent> {
	private GameDAO gameDAO;

	@Autowired
	public ResetGameCoordinatorOnTermination(GameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}

	protected ResetGameCoordinatorOnTermination() {
	}

	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		GameFilter gfilter = new GameFilter();
		gfilter.setCoordinator(event.getSubject());

		List<Game> games = gameDAO.findByFilter(gfilter);
		for (Game game : games) {
			game.setCoordinator(null);

			gameDAO.update(game);
		}

		return null;
	}
}
