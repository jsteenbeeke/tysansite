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

import java.util.List;

import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.twitter.Tweet;
import com.tysanclan.site.projectewok.entities.twitter.TwitterFollowing;
import com.tysanclan.site.projectewok.entities.twitter.TwitterQuery;
import com.tysanclan.site.projectewok.entities.twitter.TwitterSearchResult;

public interface TwitterService {
	void enqueueMessage(String message);

	void resolveQueue();

	List<Tweet> getPublicMessages(User user, int count);

	List<Tweet> getTysanAccountMessages(int count);

	List<TwitterSearchResult> getSearchResults();

	void checkTimelineUpdate();

	void checkQueryUpdate();

	void addQuery(String searchString);

	void deleteQuery(TwitterQuery query);

	void followUser(String userId);

	void removeResult(TwitterSearchResult result);

	/**
	 	 */
	List<TwitterQuery> getQueries();

	/**
	 	 */
	List<TwitterFollowing> getFollowing();

	void removeFollower(TwitterFollowing result);
}
