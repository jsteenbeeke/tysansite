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

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

public class RemoveAcceptanceVotesOnTermination
		implements EventHandler<MembershipTerminatedEvent> {
	@Autowired
	private DemocracyService democracyService;

	public void setDemocracyService(DemocracyService democracyService) {
		this.democracyService = democracyService;
	}

	@Override
	@Nonnull
	public EventResult onEvent(@Nonnull MembershipTerminatedEvent event) {
		democracyService.removeAcceptanceVotes(event.getSubject());

		return EventResult.ok();
	}
}
