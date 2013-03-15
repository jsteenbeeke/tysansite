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
package com.tysanclan.site.projectewok;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;
import com.tysanclan.site.projectewok.util.forum.MemberForumViewContext;
import com.tysanclan.site.projectewok.util.forum.PublicForumViewContext;
import com.tysanclan.site.projectewok.util.forum.ShadowForumViewContext;

/**
 * Session for Tysan site logins
 * 
 * @author Jeroen Steenbeeke
 */
public class TysanSession extends WebSession {
	private static final long serialVersionUID = 1L;

	private Set<SiteWideNotification> seenNotifications;

	private Date previousLogin;

	public TysanSession(Request request) {
		super(request);

		seenNotifications = new HashSet<SiteWideNotification>();
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		// Lazy initialization
		Long userId = (Long) getAttribute("userid");

		if (userId != null)

			return TysanApplication.getApplicationContext()
					.getBean(UserDAO.class).get(userId);

		return null;

	}

	public static ForumViewContext getForumContext() {
		TysanSession tsession = get();
		if (tsession == null)
			return new PublicForumViewContext();

		User u = tsession.getUser();

		if (u == null || u.getRank() == Rank.FORUM)
			return new PublicForumViewContext();

		if (u.getRank() == Rank.BANNED)
			return new ShadowForumViewContext();

		return new MemberForumViewContext();

	}

	public void setCurrentUserId(Long currentUserId) {
		setAttribute("userid", currentUserId);
	}

	public static TysanSession get() {
		return (TysanSession) Session.get();
	}

	public boolean notificationSeen(SiteWideNotification swn) {
		if (!seenNotifications.contains(swn)) {
			seenNotifications.add(swn);
			return false;
		}

		return true;
	}

	public void setPreviousLogin(Date previousLogin) {
		this.previousLogin = previousLogin;
	}

	public Date getPreviousLogin() {
		return previousLogin;
	}
}
