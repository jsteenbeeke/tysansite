package com.tysanclan.site.projectewok.event.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.dao.InactivityNotificationDAO;
import com.tysanclan.site.projectewok.event.LoginEvent;

public class RemoveInactivityNotificationOnLogin implements
		EventHandler<LoginEvent> {
	@Autowired
	private InactivityNotificationDAO inactivityNotificationDAO;

	public void setInactivityNotificationDAO(
			InactivityNotificationDAO inactivityNotificationDAO) {
		this.inactivityNotificationDAO = inactivityNotificationDAO;
	}

	@Override
	public EventResult onEvent(LoginEvent event) {
		inactivityNotificationDAO.deleteNotificationForUser(event.getSubject());

		return EventResult.ok();
	}
}
