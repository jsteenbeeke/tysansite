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
package com.tysanclan.site.projectewok.beans.impl.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.tysanclan.site.projectewok.beans.twitter.UnfollowActionResolver;
import com.tysanclan.site.projectewok.entities.twitter.UnfollowAction;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("component")
class UnfollowActionResolverImpl implements UnfollowActionResolver {
	private static final Logger log = LoggerFactory
			.getLogger(UnfollowActionResolverImpl.class);

	@Autowired
	private Twitter twitter;

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

	@Override
	public void performAction(UnfollowAction action) throws TwitterException {
		log.info("Unfollowing user with ID " + action.getUserId());

		twitter.destroyFriendship(action.getUserId());
	}
}
