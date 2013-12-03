package com.tysanclan.site.projectewok.event.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class ResetRolesAndRemoveNominationsOnTermination implements
		EventHandler<MembershipTerminatedEvent> {
	@Autowired
	private RoleService roleService;

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		User user = event.getSubject();

		roleService.removeRoles(user);
		roleService.removeTransfers(user);

		return EventResult.ok();
	}

}
