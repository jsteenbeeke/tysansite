package com.tysanclan.site.projectewok.event.handlers;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class CheckSenateElectionOnMembershipTermination implements
		EventHandler<MembershipTerminatedEvent> {
	private DemocracyService democracyService;

	public void setDemocracyService(DemocracyService democracyService) {
		this.democracyService = democracyService;
	}

	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		User user = event.getSubject();

		democracyService.resetSenateElectionIfUserIsParticipating(user);

		return EventResult.ok();
	}
}
