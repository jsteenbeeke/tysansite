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

import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface BugTrackerService {
	Bug reportCrash(User user, String page, Exception exception);

	Bug requestFeature(User user, String title, String description);

	Bug reportBug(User user, String title, String description);

	boolean isKnownIssue(Exception exception);

	void addCommentToBug(Bug bug, User user, String comment);

	void assignBug(Bug bug, User assignTo);

	void bugResolved(Bug bug, String resolution, String fixedInVersion);

	void markAsDuplicate(Bug bug, Bug duplicateOf);

	void grantBugRights(User user);

	void revokeBugRights(User user);

	void closeBug(Bug bug);

	void reopenBug(Bug bug, User user);
}
