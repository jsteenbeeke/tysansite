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
package com.tysanclan.site.projectewok.entities.dao.filters;

import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.RoleTransfer;
import com.tysanclan.site.projectewok.entities.User;

public class RoleTransferFilter extends SearchFilter<RoleTransfer> {
	private static final long serialVersionUID = 1L;

	private RoleType roleType;

	private Boolean accepted;

	private IModel<User> user = new Model<User>();

	private Date startBefore;

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setUser(User user) {
		this.user = ModelMaker.wrap(user);
	}

	public User getUser() {
		return user.getObject();
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public void setStartBefore(Date startBefore) {
		this.startBefore = startBefore;
	}

	public Date getStartBefore() {
		return startBefore;
	}

	@Override
	public void detach() {
		super.detach();

		user.detach();
	}
}
