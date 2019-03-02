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
package com.tysanclan.site.projectewok;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;
import com.tysanclan.site.projectewok.util.forum.MemberForumViewContext;
import com.tysanclan.site.projectewok.util.forum.PublicForumViewContext;
import com.tysanclan.site.projectewok.util.forum.ShadowForumViewContext;
import io.vavr.control.Option;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Session for Tysan site logins
 *
 * @author Jeroen Steenbeeke
 */
public class TysanSession extends WebSession {
	private static final long serialVersionUID = 1L;

	private Set<SiteWideNotification> seenNotifications;

	private Date previousLogin;

	private Long userId;

	public TysanSession(Request request) {
		super(request);

		seenNotifications = new HashSet<>();
	}

	/**
	 * @return the user
	 */
	public Option<User> getUser() {
		// Lazy initialization
		if (userId != null) {
			TysanApplication application = TysanApplication.get();

			ApplicationContext applicationContext = application
					.getApplicationContext();
			UserDAO bean = applicationContext.getBean(UserDAO.class);
			Option<User> userOption = bean.load(userId);
			return userOption;
		}

		return Option.none();

	}

	public static ForumViewContext getForumContext() {
		TysanSession tsession = get();
		if (tsession == null)
			return new PublicForumViewContext();

		Option<User> u = tsession.getUser();

		if (u.isEmpty() || u.filter(_u -> _u.getRank() == Rank.FORUM)
				.isDefined())
			return new PublicForumViewContext();

		if (u.filter(_u -> _u.getRank() == Rank.BANNED).isDefined())
			return new ShadowForumViewContext();

		return new MemberForumViewContext();

	}

	public void setCurrentUserId(Long currentUserId) {
		this.userId = currentUserId;
	}

	public static TysanSession get() {
		return (TysanSession) Session.get();
	}

	public static Option<TysanSession> session() {
		return Option.of(get());
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

	@Override
	public void onInvalidate() {
		super.onInvalidate();
		setCurrentUserId(null);
	}
}
