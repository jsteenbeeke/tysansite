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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.tysanclan.site.projectewok.beans.twitter.HomeUpdateActionResolver;
import com.tysanclan.site.projectewok.entities.dao.TweetDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.TweetFilter;
import com.tysanclan.site.projectewok.entities.twitter.HomeUpdateAction;
import com.tysanclan.site.projectewok.entities.twitter.Tweet;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class HomeUpdateActionResolverImpl implements HomeUpdateActionResolver {
	private static Logger log = LoggerFactory
			.getLogger(HomeUpdateActionResolverImpl.class);

	@Autowired
	private Twitter twitter;

	@Autowired
	private TweetDAO tweetDAO;

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

	public void setTweetDAO(TweetDAO tweetDAO) {
		this.tweetDAO = tweetDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void performAction(HomeUpdateAction action) throws TwitterException {
		log.info("Updating home user timeline");

		List<Status> statuses = twitter.getHomeTimeline();

		for (Status status : statuses) {
			String text = status.getText();

			if (text.length() > 140) {
				text = text.substring(0, 140);
			}

			TweetFilter filter = new TweetFilter();
			filter.setSearchUserNull(true);
			filter.setContents(text);

			if (tweetDAO.countByFilter(filter) == 0) {
				Tweet tweet = new Tweet();

				tweet.setContents(text);
				tweet.setName(status.getUser().getName());
				tweet.setPosted(status.getCreatedAt());
				tweet.setScreenName(status.getUser().getScreenName());
				tweet.setSource(status.getSource());
				tweet.setUser(null);
				tweetDAO.save(tweet);
			}
		}

		TweetFilter filter = new TweetFilter();

		filter.setSearchUserNull(true);
		filter.addOrderBy("posted", false);

		int i = 0;
		for (Tweet tweet : tweetDAO.findByFilter(filter)) {
			if (tweet.getUser() == null) {
				if (i++ > 30) {
					tweetDAO.delete(tweet);
				}
			}
		}

	}
}
