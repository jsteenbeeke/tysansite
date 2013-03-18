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
package com.tysanclan.site.projectewok.beans.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.tysanclan.site.projectewok.beans.ActionResolver;
import com.tysanclan.site.projectewok.beans.TwitterService;
import com.tysanclan.site.projectewok.entities.Profile;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.TweetDAO;
import com.tysanclan.site.projectewok.entities.dao.TwitterActionDAO;
import com.tysanclan.site.projectewok.entities.dao.TwitterFollowingDAO;
import com.tysanclan.site.projectewok.entities.dao.TwitterQueryDAO;
import com.tysanclan.site.projectewok.entities.dao.TwitterSearchResultDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.TweetFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.TwitterActionFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.TwitterSearchResultFilter;
import com.tysanclan.site.projectewok.entities.twitter.FollowAction;
import com.tysanclan.site.projectewok.entities.twitter.GetFollowingAction;
import com.tysanclan.site.projectewok.entities.twitter.HomeUpdateAction;
import com.tysanclan.site.projectewok.entities.twitter.RefreshAction;
import com.tysanclan.site.projectewok.entities.twitter.SearchAction;
import com.tysanclan.site.projectewok.entities.twitter.Tweet;
import com.tysanclan.site.projectewok.entities.twitter.TweetAction;
import com.tysanclan.site.projectewok.entities.twitter.TwitterAction;
import com.tysanclan.site.projectewok.entities.twitter.TwitterFollowing;
import com.tysanclan.site.projectewok.entities.twitter.TwitterQuery;
import com.tysanclan.site.projectewok.entities.twitter.TwitterSearchResult;
import com.tysanclan.site.projectewok.entities.twitter.UnfollowAction;
import com.tysanclan.site.projectewok.event.TwitterEvent;

@Component
@Scope("request")
class TwitterServiceImpl implements TwitterService, ApplicationContextAware {
	private static final Logger log = LoggerFactory
			.getLogger(TwitterService.class);

	@Autowired
	private Twitter twitter;

	@Autowired
	private TwitterActionDAO twitterActionDAO;

	@Autowired
	private TweetDAO tweetDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.UserService userService;

	@Autowired
	private TwitterQueryDAO twitterQueryDAO;

	@Autowired
	private TwitterSearchResultDAO twitterSearchResultDAO;

	@Autowired
	private TwitterFollowingDAO twitterFollowingDAO;

	private ApplicationContext applicationContext;

	public void setTwitter(Twitter twitter) {
		this.twitter = twitter;
	}

	public void setUserService(
			com.tysanclan.site.projectewok.beans.UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param twitterActionDAO
	 *            the twitterActionDAO to set
	 */
	public void setTwitterActionDAO(TwitterActionDAO twitterActionDAO) {
		this.twitterActionDAO = twitterActionDAO;
	}

	/**
	 * @param tweetDAO
	 *            the tweetDAO to set
	 */
	public void setTweetDAO(TweetDAO tweetDAO) {
		this.tweetDAO = tweetDAO;
	}

	/**
	 * @param twitterQueryDAO
	 *            the twitterQueryDAO to set
	 */
	public void setTwitterQueryDAO(TwitterQueryDAO twitterQueryDAO) {
		this.twitterQueryDAO = twitterQueryDAO;
	}

	/**
	 * @param twitterSearchResultDAO
	 *            the twitterSearchResultDAO to set
	 */
	public void setTwitterSearchResultDAO(
			TwitterSearchResultDAO twitterSearchResultDAO) {
		this.twitterSearchResultDAO = twitterSearchResultDAO;
	}

	/**
	 * @param twitterFollowingDAO
	 *            the twitterFollowingDAO to set
	 */
	public void setTwitterFollowingDAO(TwitterFollowingDAO twitterFollowingDAO) {
		this.twitterFollowingDAO = twitterFollowingDAO;
	}

	@OnFortuityEvent(TwitterEvent.class)
	@Transactional(propagation = Propagation.REQUIRED)
	public void onTwitterEvent(EventContext<TwitterEvent> context) {
		String message = context.getEvent().getSource();

		enqueueMessage(message);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void enqueueMessage(String message) {
		TweetAction tweet = new TweetAction();
		tweet.setMessage(message);
		tweet.setQueueTime(new Date());
		twitterActionDAO.save(tweet);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void resolveQueue() {
		TwitterActionFilter filter = new TwitterActionFilter();
		filter.addOrderBy("queueTime", true);

		List<TwitterAction> actions = twitterActionDAO.findByFilter(filter);

		for (TwitterAction action : actions) {
			try {

				resolveAction(action);

				twitterActionDAO.delete(action);
			} catch (TwitterException e) {
				log.error(e.getMessage(), e);

				if (e.exceededRateLimitation()) {
					return;
				}
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private <T extends TwitterAction> void resolveAction(T action)
			throws TwitterException {
		@SuppressWarnings("unchecked")
		ActionResolver<T> resolver = (ActionResolver<T>) applicationContext
				.getBean(action.getResolverType());

		resolver.performAction(action);

	}

	@Override
	public List<Tweet> getPublicMessages(User user, int count) {
		TweetFilter filter = new TweetFilter();

		filter.setUser(user);
		filter.addOrderBy("posted", false);

		List<Tweet> tweets = tweetDAO.findByFilter(filter);

		if (tweets.size() > count) {
			tweets = tweets.subList(0, count);
		}

		return tweets;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#checkTimelineUpdate()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkTimelineUpdate() {
		for (User u : userService.getMembers()) {
			Profile p = u.getProfile();
			if (p != null && p.getTwitterUID() != null
					&& !p.getTwitterUID().isEmpty()) {
				RefreshAction ra = new RefreshAction();
				ra.setProfile(p);
				ra.setQueueTime(new Date());
				twitterActionDAO.save(ra);
			}
		}

		HomeUpdateAction action = new HomeUpdateAction();

		action.setQueueTime(new Date());

		twitterActionDAO.save(action);

		GetFollowingAction action2 = new GetFollowingAction();

		action2.setQueueTime(new Date());

		twitterActionDAO.save(action2);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#addQuery(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addQuery(String searchString) {
		TwitterQuery query = new TwitterQuery();
		query.setQueryString(searchString);
		twitterQueryDAO.save(query);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#checkQueryUpdate()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void checkQueryUpdate() {
		for (TwitterQuery query : twitterQueryDAO.findAll()) {
			SearchAction action = new SearchAction();
			action.setQueueTime(new Date());
			action.setSearchQuery(query.getQueryString());
			twitterActionDAO.save(action);
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#deleteQuery(com.tysanclan.site.projectewok.entities.twitter.TwitterQuery)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteQuery(TwitterQuery query) {
		twitterQueryDAO.delete(query);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#followUser(java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void followUser(String userId) {
		FollowAction action = new FollowAction();
		action.setQueueTime(new Date());
		action.setUserId(userId);
		twitterActionDAO.save(action);

		GetFollowingAction action2 = new GetFollowingAction();
		action2.setQueueTime(new Date());
		twitterActionDAO.save(action2);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#getTysanAccountMessages(int)
	 */
	@Override
	public List<Tweet> getTysanAccountMessages(int count) {
		TweetFilter filter = new TweetFilter();

		filter.setSearchUserNull(true);
		filter.addOrderBy("posted", false);

		List<Tweet> tweets = tweetDAO.findByFilter(filter);

		if (tweets.size() > count) {
			tweets = tweets.subList(0, count);
		}

		return tweets;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#getSearchResults()
	 */
	@Override
	public List<TwitterSearchResult> getSearchResults() {
		TwitterSearchResultFilter filter = new TwitterSearchResultFilter();

		filter.addOrderBy("time", false);

		return twitterSearchResultDAO.findByFilter(filter);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#getQueries()
	 */
	@Override
	public List<TwitterQuery> getQueries() {
		return twitterQueryDAO.findAll();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#removeResult(com.tysanclan.site.projectewok.entities.twitter.TwitterSearchResult)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeResult(TwitterSearchResult result) {
		twitterSearchResultDAO.delete(result);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#getFollowing()
	 */
	@Override
	public List<TwitterFollowing> getFollowing() {
		return twitterFollowingDAO.findAll();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.TwitterService#removeFollower(com.tysanclan.site.projectewok.entities.twitter.TwitterFollowing)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void removeFollower(TwitterFollowing result) {
		UnfollowAction action = new UnfollowAction();
		action.setQueueTime(new Date());
		action.setUserId(result.getUserId());
		twitterActionDAO.save(action);

		twitterFollowingDAO.delete(result);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
