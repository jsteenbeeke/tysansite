package com.tysanclan.site.projectewok.event.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.dao.InactivityNotificationDAO;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class RemoveInactivityNotificationsOnMembershipTermination implements
		EventHandler<MembershipTerminatedEvent> {
	@Autowired
	private InactivityNotificationDAO inactivityNotificationDAO;

	public void setInactivityNotificationDAO(
			InactivityNotificationDAO inactivityNotificationDAO) {
		this.inactivityNotificationDAO = inactivityNotificationDAO;
	}

	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		// Remove inactivity notification
		inactivityNotificationDAO.deleteNotificationForUser(event.getSubject());

		return EventResult.ok();
	}
}
