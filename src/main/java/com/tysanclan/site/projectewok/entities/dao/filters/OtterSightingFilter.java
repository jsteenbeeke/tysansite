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

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.OtterSighting;
import com.tysanclan.site.projectewok.entities.User;

public class OtterSightingFilter extends SearchFilter<OtterSighting> {
	private static final long serialVersionUID = 1L;

	private IModel<User> user = new Model<User>();

	private Integer otterNumber;

	public User getUser() {
		return user.getObject();
	}

	public void setUser(User user) {
		this.user = ModelMaker.wrap(user);
	}

	public Integer getOtterNumber() {
		return otterNumber;
	}

	public void setOtterNumber(Integer otterNumber) {
		this.otterNumber = otterNumber;
	}

	@Override
	public void detach() {
		super.detach();
		this.user.detach();
	}
}
