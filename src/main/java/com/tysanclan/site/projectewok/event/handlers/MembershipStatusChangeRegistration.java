package com.tysanclan.site.projectewok.event.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.beans.MembershipStatusBean;
import com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.event.MemberStatusEvent;

public class MembershipStatusChangeRegistration implements
		EventHandler<MemberStatusEvent> {
	@Autowired
	private MembershipStatusBean statusService;

	public void setStatusService(MembershipStatusBean statusService) {
		this.statusService = statusService;
	}

	@Override
	public EventResult onEvent(MemberStatusEvent event) {
		User subject = event.getSubject();
		ChangeType type = event.getType();

		statusService.addStatus(type, subject);

		return EventResult.ok();
	}
}
