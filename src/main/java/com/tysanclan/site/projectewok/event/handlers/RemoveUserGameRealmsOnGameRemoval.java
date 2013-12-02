package com.tysanclan.site.projectewok.event.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO;
import com.tysanclan.site.projectewok.event.GameDeletionEvent;

public class RemoveUserGameRealmsOnGameRemoval implements
		EventHandler<GameDeletionEvent> {

	private UserGameRealmDAO gameRealmDAO;

	@Autowired
	public RemoveUserGameRealmsOnGameRemoval(UserGameRealmDAO gameRealmDAO) {
		super();
		this.gameRealmDAO = gameRealmDAO;
	}

	protected RemoveUserGameRealmsOnGameRemoval() {
	}

	@Override
	public EventResult onEvent(GameDeletionEvent event) {
		gameRealmDAO.removeUserGameRealmsByGame(event.getSubject());

		return EventResult.ok();
	}
}
