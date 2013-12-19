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

import java.util.Date;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumThreadRecordHandler implements RecordHandler {
	@SpringBean
	private ForumService forumService;

	@SpringBean
	private ForumThreadDAO forumThreadDAO;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "FT";
	}

	@Override
	@Deprecated
	public boolean handle(String[] data, TangoImporterCallback callback) {
		// FT key title forumkey posterkey posted sticky locked
		long key = Long.parseLong(data[1]), forumKey = Long.parseLong(data[3]), posterKey = (data[4]
				.isEmpty()) ? -1 : Long.parseLong(data[4]), postTime = Long
				.parseLong(data[5]);
		boolean isSticky = data[6].equals(VALUE_TRUE), isLocked = data[7]
				.equals(VALUE_TRUE);
		String title = data[2];

		Forum forum = callback.getImportedObject(forumKey, Forum.class);
		if (forum == null) {
			forum = callback.getImportedObject(forumKey, NewsForum.class);
		}
		if (forum == null) {
			forum = callback.getImportedObject(forumKey, GroupForum.class);
		}
		User poster = callback.getImportedObject(posterKey, User.class);

		if (forum != null) {
			ForumThread thread = forumService.createEmptyForumThread(forum,
					title, poster);
			if (isSticky) {
				forumService.stickyThread(thread, null);
			}
			if (isLocked) {
				forumService.lockThread(thread, null);
			}

			// Ok, this is not a cleanly separated concern, so sue me
			thread.setPostTime(new Date(postTime));
			forumThreadDAO.update(thread);

			callback.registerImportedObject(key, thread);
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
		forumThreadDAO = null;

	}

}
