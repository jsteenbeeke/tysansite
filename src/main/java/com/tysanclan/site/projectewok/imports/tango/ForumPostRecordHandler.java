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
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ForumPostDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumPostRecordHandler implements RecordHandler {
	@SpringBean
	private ForumService forumService;

	@SpringBean
	private ForumPostDAO forumPostDAO;

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#cleanup()
	 */
	@Override
	public void cleanup() {
		forumService = null;
		forumPostDAO = null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#getRecordDescriptor()
	 */
	@Override
	public String getRecordDescriptor() {
		return "FP";
	}

	/**
	 * @see com.tysanclan.site.projectewok.imports.tango.RecordHandler#handle(java.lang.String[],
	 *      com.tysanclan.site.projectewok.imports.tango.TangoImporterCallback)
	 */
	@Override
	@Deprecated
	public boolean handle(String[] data, TangoImporterCallback callback) {
		// FP key content poster thread time
		long key = Long.parseLong(data[1]), posterKey = data[3].isEmpty() ? -1
				: Long.parseLong(data[3]), threadKey = Long.parseLong(data[4]), time = Long
				.parseLong(data[5]);
		String content = data[2];

		ForumThread forumThread = callback.getImportedObject(threadKey,
				ForumThread.class);
		User poster = callback.getImportedObject(posterKey, User.class);

		if (forumThread != null) {
			ForumPost forumPost = forumService.replyToThread(forumThread,
					content, poster);
			// Break separation of concerns
			forumPost.setTime(new Date(time));
			forumPostDAO.update(forumPost);

			callback.registerImportedObject(key, forumPost);
			return true;
		}

		return false;
	}

}
