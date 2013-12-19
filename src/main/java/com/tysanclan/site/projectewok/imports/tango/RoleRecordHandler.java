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
package com.tysanclan.site.projectewok.imports.tango;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.Role;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class RoleRecordHandler implements RecordHandler {
	@SpringBean
	private RoleService roleService;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "UR";
	}

	@Override
	public boolean handle(String[] data, TangoImporterCallback callback) {
		String name = data[2], description = data[3];
		long key = Long.parseLong(data[1]), assignedToKey = Long
				.parseLong(data[4]);
		Role role = roleService.createRole(null, name, description,
				RoleType.NORMAL);

		User user = callback.getImportedObject(assignedToKey, User.class);

		if (user != null) {
			callback.registerImportedObject(key, role);

			return roleService.assignTo(null, role.getId(), user.getId());
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		roleService = null;
	}

}
