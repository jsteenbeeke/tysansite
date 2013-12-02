package com.tysanclan.site.projectewok.event.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO;
import com.tysanclan.site.projectewok.event.RealmDeletionEvent;

public class UserGameRealmRealmsOnRealmRemoval implements EventHandler<RealmDeletionEvent> {
	private UserGameRealmDAO gameRealmDAO;

	@Autowired
	public UserGameRealmRealmsOnRealmRemoval(UserGameRealmDAO gameRealmDAO) {
		this.gameRealmDAO = gameRealmDAO;
	}

	protected UserGameRealmRealmsOnRealmRemoval() {

	}

	@Override
	public EventResult onEvent(RealmDeletionEvent event) {
		gameRealmDAO.removeUserGameRealmsByRealm(event.getSubject());

		return EventResult.ok();
	}
}
