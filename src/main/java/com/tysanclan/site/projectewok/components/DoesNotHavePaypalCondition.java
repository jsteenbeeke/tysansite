/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.User;

public class DoesNotHavePaypalCondition implements IRequiresAttentionCondition,
		IDetachable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IModel<User> user;

	public DoesNotHavePaypalCondition(User user) {
		this.user = ModelMaker.wrap(user);
	}

	@Override
	public AttentionType requiresAttention() {
		User u = user.getObject();

		if (u != null && u.getPaypalAddress() == null) {
			return AttentionType.ERROR;
		}

		return null;
	}

	@Override
	public Long getDismissableId() {
		return null;
	}

	@Override
	public void detach() {
		user.detach();
	}
}
