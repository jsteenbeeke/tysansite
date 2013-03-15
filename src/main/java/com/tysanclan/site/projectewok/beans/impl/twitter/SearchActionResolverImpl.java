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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.*;

import com.tysanclan.site.projectewok.beans.twitter.SearchActionResolver;
import com.tysanclan.site.projectewok.entities.dao.TwitterSearchResultDAO;
import com.tysanclan.site.projectewok.entities.twitter.SearchAction;
import com.tysanclan.site.projectewok.entities.twitter.TwitterSearchResult;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class SearchActionResolverImpl implements SearchActionResolver {
	private static final Logger log = LoggerFactory
			.getLogger(SearchActionResolverImpl.class);

	@Autowired
	private Twitter twitter;

	@Autowired
	private TwitterSearchResultDAO resultDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void performAction(SearchAction action) throws TwitterException {
		log.info("Performing Twitter search for " + action.getSearchQuery());

		Query q = new Query(action.getSearchQuery());

		QueryResult res = twitter.search(q);

		for (twitter4j.Tweet t : res.getTweets()) {
			TwitterSearchResult result = new TwitterSearchResult();

			GeoLocation loc = t.getGeoLocation();

			if (loc != null) {
				result.setLatitude(loc.getLatitude());
				result.setLongitude(loc.getLongitude());
			}

			result.setMessage(t.getText());
			result.setTime(t.getCreatedAt());
			result.setUserId(t.getFromUser());

			resultDAO.save(result);

		}
	}
}
