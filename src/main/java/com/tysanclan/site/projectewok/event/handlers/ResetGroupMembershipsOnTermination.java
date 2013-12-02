package com.tysanclan.site.projectewok.event.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class ResetGroupMembershipsOnTermination implements
		EventHandler<MembershipTerminatedEvent> {
	private GroupService groupService;

	@Autowired
	public ResetGroupMembershipsOnTermination(GroupService groupService) {
		this.groupService = groupService;
	}

	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		User user = event.getSubject();

		groupService.clearGroupMemberships(user);

		return EventResult.ok();
	}

}
