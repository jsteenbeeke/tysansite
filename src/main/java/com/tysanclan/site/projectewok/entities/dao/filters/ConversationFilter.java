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
package com.tysanclan.site.projectewok.entities.dao.filters;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.model.IModel;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.Conversation;
import com.tysanclan.site.projectewok.entities.User;

public class ConversationFilter extends SearchFilter<Conversation> {

	private static final long serialVersionUID = 1L;

	private IModel<List<User>> participants = ModelMaker
			.wrap(new LinkedList<User>());

	private boolean sortByLastResponse = false;

	public List<User> getParticipants() {
		return participants.getObject();
	}

	public void setSortByLastResponse(boolean sortByLastResponse) {
		this.sortByLastResponse = sortByLastResponse;
	}

	public boolean isSortByLastResponse() {
		return sortByLastResponse;
	}

	public void addParticipant(User user) {
		List<User> u = participants.getObject();
		List<User> u2 = new LinkedList<User>();

		u2.addAll(u);
		u2.add(user);

		this.participants = ModelMaker.wrap(u2);
	}

	@Override
	public void detach() {
		super.detach();
		participants.detach();
	}
}
