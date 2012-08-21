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
package com.tysanclan.site.projectewok.imports.tango;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.ForumCategory;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumCategoryRecordHandler implements RecordHandler {

	@SpringBean
	private ForumService forumService;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "FC";
	}

	@Override
	public boolean handle(String[] data, TangoImporterCallback callback) {
		String name = data[2], allowPublicGroupsStr = data[3];
		Long key = Long.parseLong(data[1]);

		boolean allowPublicGroups = allowPublicGroupsStr.equals("TRUE");

		ForumCategory category = forumService.createCategory(null, name,
				allowPublicGroups);

		if (category != null) {
			callback.registerImportedObject(key, category);
		}

		return category != null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		forumService = null;

	}

}
