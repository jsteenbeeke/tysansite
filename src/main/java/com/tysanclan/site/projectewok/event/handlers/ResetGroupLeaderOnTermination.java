/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
