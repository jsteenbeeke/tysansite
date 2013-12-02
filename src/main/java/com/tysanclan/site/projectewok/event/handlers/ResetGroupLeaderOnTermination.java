package com.tysanclan.site.projectewok.event.handlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.event.GroupWithoutLeaderEvent;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class ResetGroupLeaderOnTermination implements
		EventHandler<MembershipTerminatedEvent> {
	private GroupService groupService;

	@Autowired
	public ResetGroupLeaderOnTermination(GroupService groupService) {
		this.groupService = groupService;
	}

	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		User user = event.getSubject();

		List<Group> leaderless = groupService.clearGroupLeaderStatus(user);

		GroupWithoutLeaderEvent[] events = new GroupWithoutLeaderEvent[leaderless
				.size()];

		int i = 0;
		for (Group g : leaderless) {
			events[i++] = new GroupWithoutLeaderEvent(g);
		}

		return EventResult.ok(events);
	}
}
