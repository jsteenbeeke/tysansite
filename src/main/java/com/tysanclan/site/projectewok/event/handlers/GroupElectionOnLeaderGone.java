package com.tysanclan.site.projectewok.event.handlers;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.event.GroupWithoutLeaderEvent;

public class GroupElectionOnLeaderGone implements
		EventHandler<GroupWithoutLeaderEvent> {
	@Autowired
	private DemocracyService democracyService;

	public void setDemocracyService(DemocracyService democracyService) {
		this.democracyService = democracyService;
	}

	@Override
	@Nonnull
	public EventResult onEvent(@Nonnull GroupWithoutLeaderEvent event) {

		democracyService.createGroupLeaderElection(event.getSubject());

		return EventResult.ok();
	}
}
