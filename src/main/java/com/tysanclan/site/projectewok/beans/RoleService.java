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
package com.tysanclan.site.projectewok.beans;

import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.RoleTransfer;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface RoleService {
	public Role createRole(User user, String name, String description,
			RoleType roleType);

	public boolean assignTo(Long assigner_id, Long role_id, Long user_id);

	public User getHerald();

	public User getSteward();

	public User getTreasurer();

	public void deleteRole(User user, Role role);

	public void setRoleName(User user, Role role, String name);

	public void setRoleDescription(User user, Role role, String description);

	RoleTransfer getCurrentTransfer(RoleType type);

	void objectToTransfer(User user, RoleTransfer transfer);

	void acceptNomination(RoleTransfer transfer);

	void rejectNomination(RoleTransfer transfer);

	void resolveTransfer(RoleTransfer transfer);

	public void initiateTransfer(RoleType type, User candidate);

	Role getRoleByType(RoleType type);

	void approveCandidate(RoleTransfer object, User user);

	public void resolveTransfers();

	public void removeRoles(User user);

	public void removeTransfers(User user);
}
