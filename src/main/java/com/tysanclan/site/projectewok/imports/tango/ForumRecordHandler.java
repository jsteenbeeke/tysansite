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
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.Group;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumRecordHandler implements RecordHandler {
	@SpringBean
	private ForumService forumService;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "F";
	}

	@Override
	public boolean handle(String[] data, TangoImporterCallback callback) {
		// F Key Name Desc Cat IsNews Group AllowPublicAccess MembersOnly
		// Interaction

		String name = data[2], description = data[3];
		boolean isNews = data[5].equals(VALUE_TRUE), allowPublicAccess = data[7]
				.equals(VALUE_TRUE), membersOnly = data[8].equals(VALUE_TRUE), interactive = data[9]
				.equals(VALUE_TRUE);
		long categoryKey = Long.parseLong(data[4]), key = Long
				.parseLong(data[1]);
		long groupKey = data[6].isEmpty() ? -1 : Long.parseLong(data[6]);

		ForumCategory category = callback.getImportedObject(categoryKey,
				ForumCategory.class);

		if (category != null) {
			Forum forum = null;
			if (isNews) {
				forum = forumService.createNewsForum(name, description,
						allowPublicAccess, category);
			} else if (groupKey != -1) {
				Group group = callback.getImportedObject(groupKey, Group.class);
				forum = forumService.createGroupForum(name, description,
						category, group);
			} else {
				forum = forumService.createForum(name, description,
						allowPublicAccess, category, null);
			}

			if (membersOnly) {
				forumService.makeMembersOnly(forum, null);
			}

			if (interactive) {
				forumService.setInteractive(forum, interactive, null);
			}

			if (forum != null) {
				callback.registerImportedObject(key, forum);
			}

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		forumService = null;

	}
}
