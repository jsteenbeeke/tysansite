/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.auth;

import java.util.Arrays;

import io.vavr.control.Option;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.application.IComponentInstantiationListener;

import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * Component instantiation listener that takes care of security checks for
 * various pages and components
 *
 * @author Jeroen Steenbeeke
 */
public class TysanSecurity implements IComponentInstantiationListener {
	/**
	 * Create a new security object
	 */
	public TysanSecurity() {
	}

	/**
	 * Checks whether or not the current user has access to the given component
	 *
	 * @param componentClass The class of the component to instantiate
	 * @return <code>true</code> if the user is authorized, <code>false</code>
	 * otherwise
	 */
	public boolean authorize(Class<? extends Component> componentClass) {
		// Check for rank-based security policy
		TysanRankSecured sec1 = componentClass
				.getAnnotation(TysanRankSecured.class);
		// Check for member-based security policy
		TysanMemberSecured sec2 = componentClass
				.getAnnotation(TysanMemberSecured.class);
		// Check for non-member based security policy
		TysanNonMemberSecured sec3 = componentClass
				.getAnnotation(TysanNonMemberSecured.class);
		// Check for a logged in user-based security policy
		TysanLoginSecured sec4 = componentClass
				.getAnnotation(TysanLoginSecured.class);

		// Get the current user
		Option<TysanSession> session = TysanSession.session();
		Option<User> users = session.flatMap(TysanSession::getUser);
		return users.map(user -> {
			// There is a user, check for security policies
			if (sec1 != null) {
				// Check if the user has the correct rank
				return Arrays.asList(sec1.value()).contains(user.getRank());
			} else if (sec2 != null) {
				// Check if the user is a Tysan member
				return MemberUtil.isMember(user);
			} else // User is logged in
				if (sec3 != null) {
					// Check if the user is not a Tysan member
					return !MemberUtil.isMember(user);
				} else {
					// LoginSecured only requires us to be logged in, which we are
					return true;
				}

		}).getOrElse(() -> {
			// No user, but existing security policy
			return sec1 == null && sec2 == null && sec3 == null && sec4 == null;
		});

	}

	/**
	 * @see org.apache.wicket.application.IComponentInstantiationListener#onInstantiation(org.apache.wicket.Component)
	 */
	@Override
	public void onInstantiation(Component component) {
		// Check authorization
		if (!authorize(component.getClass())) {
			// If not authorized, redirect to access denied page
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

	}
}
