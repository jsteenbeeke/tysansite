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
package com.tysanclan.site.projectewok.beans.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.Event;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.GroupForum;
import com.tysanclan.site.projectewok.entities.NewsForum;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.UnreadForumPost;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.EventDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumCategoryDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumPostDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO;
import com.tysanclan.site.projectewok.entities.dao.GroupDAO;
import com.tysanclan.site.projectewok.entities.dao.NewsForumDAO;
import com.tysanclan.site.projectewok.entities.dao.TrialDAO;
import com.tysanclan.site.projectewok.entities.dao.UnreadForumPostDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ForumFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.ForumPostFilter;
import com.tysanclan.site.projectewok.util.MemberUtil;
import com.tysanclan.site.projectewok.util.StringUtil;
import com.tysanclan.site.projectewok.util.bbcode.BBCodeUtil;

@Component
@Scope("request")
class ForumServiceImpl implements
		com.tysanclan.site.projectewok.beans.ForumService {
	@Autowired
	private ForumDAO forumDAO;
	@Autowired
	private UnreadForumPostDAO unreadForumPostDAO;
	@Autowired
	private ForumPostDAO forumPostDAO;
	@Autowired
	private ForumThreadDAO forumThreadDAO;
	@Autowired
	private ForumCategoryDAO forumCategoryDAO;
	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;
	@Autowired
	private NewsForumDAO newsForumDAO;
	@Autowired
	private EventDAO eventDAO;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private TrialDAO trialDAO;

	@Autowired
	private UserDAO userDAO;

	/**
	 * @param eventDAO
	 *            the eventDAO to set
	 */
	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	/**
	 * @param groupDAO
	 *            the groupDAO to set
	 */
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	/**
	 * @param trialDAO
	 *            the trialDAO to set
	 */
	public void setTrialDAO(TrialDAO trialDAO) {
		this.trialDAO = trialDAO;
	}

	/**
	 * @param logService
	 *            the logService to set
	 */
	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	public ForumServiceImpl() {
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean canEditPost(User user, ForumPost post) {
		if (user != null) {
			ForumThread thread = post.getThread();
			Trial trial = trialDAO.getTrialByThread(thread);

			if (trial == null || thread.getPosts().indexOf(post) > 0) {

				if (user.equals(post.getPoster())) {

					return true;
				}

				return isModerator(user, post.getThread().getForum());
			}
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean canView(User _user, Forum _forum) {

		Forum forum = forumDAO.load(_forum.getId());
		User user = _user != null ? userDAO.load(_user.getId()) : null;

		return forum.canView(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean canReply(User _user, Forum _forum) {
		Forum forum = forumDAO.load(_forum.getId());
		User user = _user != null ? userDAO.load(_user.getId()) : null;

		return forum.canReply(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ForumThread createForumThread(Forum forum, String title,
			String content, User user) {
		ForumThread nt = createEmptyForumThread(forum, title, user);
		Forum f2 = forumDAO.load(forum.getId());

		ForumPost post = new ForumPost();
		post.setPoster(user);
		post.setContent(BBCodeUtil.stripTags(content));
		post.setShadow(user != null && user.getRank() == Rank.BANNED);
		post.setTime(new Date());
		post.setThread(nt);

		List<ForumPost> posts = new LinkedList<ForumPost>();
		posts.add(post);

		nt.setPosts(posts);

		f2.getThreads().add(nt);

		forumDAO.update(f2);
		forumThreadDAO.update(nt);
		forumPostDAO.save(post);

		return nt;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ForumThread createEmptyForumThread(Forum forum, String title,
			User user) {
		ForumThread nt = new ForumThread();
		nt.setForum(forum);
		nt.setPoster(user);
		nt.setShadow(user != null && user.getRank() == Rank.BANNED);
		nt.setPostTime(new Date());
		nt.setTitle(BBCodeUtil.stripTags(title));
		nt.setLastPost(nt.getPostTime());
		forumThreadDAO.save(nt);
		return nt;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deletePost(ForumPost post, User user) {
		if (post.getPoster().equals(user)
				|| isModerator(user, post.getThread().getForum())) {
			forumPostDAO.delete(post);
			post.getThread().getPosts().remove(post);
			forumThreadDAO.update(post.getThread());

			logService.logUserAction(user, "Forum", StringUtil.combineStrings(
					"Post ", post.getId(), " by ", post.getPoster()
							.getUsername(), " deleted"));

			if (post.getThread().getPosts().isEmpty()) {
				return deleteThread(post.getThread(), null);
			}
		}
		return true;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#deleteThread(com.tysanclan.site.projectewok.entities.ForumThread,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteThread(ForumThread thread, User user) {
		if (isModerator(user, thread.getForum()) || thread.getPosts().isEmpty()) {
			Event event = eventDAO.getEventByThread(thread);
			Trial trial = trialDAO.getTrialByThread(thread);

			if (trial != null) {
				return false;
			}

			if (event != null) {
				eventDAO.delete(event);
				logService.logUserAction(user, "Events", StringUtil
						.combineStrings(thread.getTitle(), " deleted"));
			}

			thread.getForum().getThreads().remove(thread);
			forumDAO.update(thread.getForum());
			forumThreadDAO.delete(thread);

			logService.logUserAction(user, "Forum", StringUtil.combineStrings(
					"Thread ", thread.getTitle(), " deleted"));

			return true;
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void editPost(ForumPost post, String content, User currentUser) {
		if (post.getPoster().equals(currentUser)
				|| post.getThread().getForum().getModerators()
						.contains(currentUser)
				|| currentUser.getRank() == Rank.TRUTHSAYER) {
			post.setContent(BBCodeUtil.stripTags(content));
			forumPostDAO.update(post);
		}
	}

	/**
	 * @return the newsForumDAO
	 */
	public NewsForumDAO getNewsForumDAO() {
		return newsForumDAO;
	}

	/**
	 * @param newsForumDAO
	 *            the newsForumDAO to set
	 */
	public void setNewsForumDAO(NewsForumDAO newsForumDAO) {
		this.newsForumDAO = newsForumDAO;
	}

	/**
	 * @return the forumDAO
	 */
	public ForumDAO getForumDAO() {
		return forumDAO;
	}

	/**
	 * @return the forumPostDAO
	 */
	public ForumPostDAO getForumPostDAO() {
		return forumPostDAO;
	}

	/**
	 * @return the forumThreadDAO
	 */
	public ForumThreadDAO getForumThreadDAO() {
		return forumThreadDAO;
	}

	@Override
	public Forum getNewsForum() {
		List<NewsForum> forums = newsForumDAO.findAll();

		return forums.isEmpty() ? null : newsForumDAO.findAll().get(0);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#getValidDestinationForums(com.tysanclan.site.projectewok.entities.Forum,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Forum> getValidDestinationForums(Forum _forum, User u) {
		Forum forum = forumDAO.load(_forum.getId());

		List<Forum> forums = new LinkedList<Forum>();
		for (Forum f : forumDAO.findAll()) {
			if (isModerator(u, f) && !f.equals(forum) && f.canView(u)) {
				forums.add(f);
			}
		}

		return forums;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean isModerator(User user, Forum forum) {
		Forum f2 = forumDAO.load(forum.getId());

		if (user != null) {
			if (f2.getModerators().contains(user)) {
				return true;
			}
			if (user.getRank() == Rank.TRUTHSAYER) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#lockThread(com.tysanclan.site.projectewok.entities.ForumThread,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean lockThread(ForumThread thread, User user) {
		if (user == null || isModerator(user, thread.getForum())) {
			thread.setLocked(true);
			forumThreadDAO.update(thread);

			logService.logUserAction(user, "Forum", StringUtil.combineStrings(
					"Thread ", thread.getTitle(), " locked"));

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#moveThread(com.tysanclan.site.projectewok.entities.ForumThread,
	 *      com.tysanclan.site.projectewok.entities.Forum,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean moveThread(ForumThread _thread, Forum _forum, User _user) {
		ForumThread thread = forumThreadDAO.load(_thread.getId());
		Forum forum = forumDAO.load(_forum.getId());
		User user = userDAO.load(_user.getId());

		if (isModerator(user, forum) && isModerator(user, thread.getForum())) {
			forum.getThreads().add(thread);
			thread.getForum().getThreads().remove(thread);

			forumDAO.update(thread.getForum());

			thread.setForum(forum);

			forumDAO.update(forum);
			forumThreadDAO.update(thread);

			logService.logUserAction(user, "Forum", StringUtil.combineStrings(
					"Thread ", thread.getTitle(), " moved"));

			return true;
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ForumPost replyToThread(ForumThread thread, String content, User user) {
		Date posttime = new Date();

		ForumPost post = new ForumPost();
		post.setPoster(user);
		post.setContent(BBCodeUtil.stripTags(content));
		post.setShadow(user != null && user.getRank() == Rank.BANNED);
		post.setTime(posttime);
		post.setThread(thread);

		thread.getPosts().add(post);

		if (!post.isShadow()) {
			thread.setLastPost(posttime);
		}

		forumThreadDAO.update(thread);
		forumPostDAO.save(post);

		return post;
	}

	/**
	 * @param forumDAO
	 *            the forumDAO to set
	 */
	public void setForumDAO(ForumDAO forumDAO) {
		this.forumDAO = forumDAO;
	}

	/**
	 * @param forumPostDAO
	 *            the forumPostDAO to set
	 */
	public void setForumPostDAO(ForumPostDAO forumPostDAO) {
		this.forumPostDAO = forumPostDAO;
	}

	/**
	 * @param forumThreadDAO
	 *            the forumThreadDAO to set
	 */
	public void setForumThreadDAO(ForumThreadDAO forumThreadDAO) {
		this.forumThreadDAO = forumThreadDAO;
	}

	/**
	 * @param forumCategoryDAO
	 *            the forumCategoryDAO to set
	 */
	public void setForumCategoryDAO(ForumCategoryDAO forumCategoryDAO) {
		this.forumCategoryDAO = forumCategoryDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ForumThread splitThread(ForumThread source,
			List<ForumPost> splitPosts, String splitTitle, String splitOpening,
			User user) {
		if (isModerator(user, source.getForum())) {
			ForumThread target = createForumThread(source.getForum(),
					splitTitle, splitOpening, user);

			if (target != null) {
				source.getPosts().removeAll(splitPosts);
				target.getPosts().addAll(splitPosts);
				target.setBranchFrom(source);

				for (ForumPost post : splitPosts) {
					post.setThread(target);
					forumPostDAO.update(post);
				}

				forumThreadDAO.update(source);
				forumThreadDAO.update(target);

				ForumPost notification = replyToThread(source, "", user);

				if (notification != null) {
					notification.setBranchTo(target);
					forumPostDAO.update(notification);

					logService.logUserAction(
							user,
							"Forum",
							StringUtil.combineStrings("Thread ",
									source.getTitle(), " split"));

					return target;
				}
			}
		}

		return null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#getInteractionForum()
	 */
	@Override
	public Forum getInteractionForum() {
		ForumFilter filter = new ForumFilter();
		filter.setInteraction(true);

		List<Forum> forums = forumDAO.findByFilter(filter);

		return !forums.isEmpty() ? forums.get(0) : null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#stickyThread(com.tysanclan.site.projectewok.entities.ForumThread,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean stickyThread(ForumThread thread, User user) {
		if (user == null || isModerator(user, thread.getForum())) {
			thread.setPostSticky(true);
			forumThreadDAO.update(thread);

			logService.logUserAction(user, "Forum", StringUtil.combineStrings(
					"Thread ", thread.getTitle(), " stickied"));

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#unlockThread(com.tysanclan.site.projectewok.entities.ForumThread,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean unlockThread(ForumThread thread, User user) {
		if (isModerator(user, thread.getForum())) {
			thread.setLocked(false);
			forumThreadDAO.update(thread);

			logService.logUserAction(user, "Forum", StringUtil.combineStrings(
					"Thread ", thread.getTitle(), " unlocked"));

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#unstickyThread(com.tysanclan.site.projectewok.entities.ForumThread,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean unstickyThread(ForumThread thread, User user) {
		if (isModerator(user, thread.getForum())) {
			thread.setPostSticky(false);
			forumThreadDAO.update(thread);

			logService.logUserAction(user, "Forum", StringUtil.combineStrings(
					"Thread ", thread.getTitle(), " unstickied"));

			return true;
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ForumCategory createCategory(User user, String name,
			boolean allowPublicGroups) {
		ForumCategory forumCategory = new ForumCategory();
		forumCategory.setName(name);
		forumCategory.setAllowPublicGroupForums(allowPublicGroups);
		forumCategoryDAO.save(forumCategory);

		logService.logUserAction(user, "Forum",
				StringUtil.combineStrings("Category ", name, " created"));

		return forumCategory;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Forum createForum(String name, String description,
			boolean allowPublicAccess, ForumCategory category, User user) {
		int maxPos = 1;

		for (Forum forum : category.getForums()) {
			if (forum.getPosition() >= maxPos) {
				maxPos = forum.getPosition();
			}
		}

		maxPos++;

		Forum forum = new Forum();
		forum.setCategory(category);
		forum.setDescription(description);
		forum.setName(name);
		forum.setMembersOnly(false);
		forum.setPublicAccess(allowPublicAccess);
		forum.setPosition(maxPos);
		forumDAO.save(forum);

		category.getForums().add(forum);

		logService.logUserAction(user, "Forum",
				StringUtil.combineStrings("Forum ", name, " created"));

		return forum;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#createGroupForum(java.lang.String,
	 *      java.lang.String,
	 *      com.tysanclan.site.projectewok.entities.ForumCategory,
	 *      com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GroupForum createGroupForum(String name, String description,
			ForumCategory category, Group group) {
		int maxPos = 1;

		for (Forum forum : category.getForums()) {
			if (forum.getPosition() > maxPos) {
				maxPos = forum.getPosition();
			}
		}

		maxPos++;

		GroupForum forum = new GroupForum();
		forum.setCategory(category);
		forum.setDescription(description);
		forum.setName(name);
		forum.setPublicAccess(false);
		forum.setGroup(group);
		forum.setPosition(maxPos);
		forumDAO.save(forum);

		category.getForums().add(forum);

		logService.logSystemAction("Forum", StringUtil.combineStrings(
				"Group forum ", name, " for group ", group.getName(),
				" created"));

		return forum;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#createNewsForum(java.lang.String,
	 *      java.lang.String, boolean,
	 *      com.tysanclan.site.projectewok.entities.ForumCategory)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public NewsForum createNewsForum(String name, String description,
			boolean allowPublicAccess, ForumCategory category) {
		int maxPos = 1;

		for (Forum forum : category.getForums()) {
			if (forum.getPosition() > maxPos) {
				maxPos = forum.getPosition();
			}
		}

		maxPos++;

		NewsForum forum = new NewsForum();
		forum.setCategory(category);
		forum.setDescription(description);
		forum.setName(name);
		forum.setPosition(maxPos);
		forum.setPublicAccess(allowPublicAccess);
		forumDAO.save(forum);

		category.getForums().add(forum);

		logService.logSystemAction("Forum",
				StringUtil.combineStrings("News forum ", name, " created"));

		return forum;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#makeMembersOnly(com.tysanclan.site.projectewok.entities.Forum,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean makeMembersOnly(Forum forum, User user) {
		forum.setMembersOnly(true);
		forumDAO.update(forum);

		logService.logUserAction(user, "Forum", StringUtil.combineStrings(
				"Forum ", forum.getName(), " is now members-only"));

		return true;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#unmakeMembersOnly(com.tysanclan.site.projectewok.entities.Forum,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean unmakeMembersOnly(Forum forum, User user) {
		forum.setMembersOnly(true);
		forumDAO.update(forum);

		logService.logUserAction(user, "Forum", StringUtil.combineStrings(
				"Forum ", forum.getName(), " is no longer members-only"));

		return true;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#setInteractive(com.tysanclan.site.projectewok.entities.Forum,
	 *      boolean, com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setInteractive(Forum forum, boolean interactive, User user) {
		forum.setInteractive(interactive);
		forumDAO.update(forum);

		logService.logUserAction(user, "Forum", StringUtil.combineStrings(
				"Forum ", forum.getName(), " is now ", interactive ? ""
						: "not ", "interactive"));

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#getUnreadPosts(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	public List<ForumPost> getUnreadPosts(User user) {
		ForumPostFilter filter = new ForumPostFilter();
		filter.setPostAfter(user.getLastAction());

		return forumPostDAO.findByFilter(filter);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#isGroupMember(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean isGroupMember(User user, Group group) {
		Group _group = groupDAO.load(group.getId());

		return _group.getGroupMembers().contains(user);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#deleteCategory(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.ForumCategory)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteCategory(User user, ForumCategory category) {
		ForumCategory _category = forumCategoryDAO.load(category.getId());

		if (_category.getForums().isEmpty()) {
			forumCategoryDAO.delete(_category);

			logService.logUserAction(user, "Forum",
					"Category " + _category.getName() + " deleted");
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#setForumName(com.tysanclan.site.projectewok.entities.Forum,
	 *      java.lang.String, com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setForumName(Forum forum, String newName, User user) {
		Forum _forum = forumDAO.load(forum.getId());

		String oldName = _forum.getName();

		_forum.setName(newName);
		forumDAO.update(_forum);

		logService.logUserAction(user, "Forum", "Name of forum " + oldName
				+ " changed to " + newName);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#setForumDescription(com.tysanclan.site.projectewok.entities.Forum,
	 *      java.lang.String, com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setForumDescription(Forum forum, String description, User user) {
		Forum _forum = forumDAO.load(forum.getId());

		_forum.setDescription(BBCodeUtil.stripTags(description));
		forumDAO.update(_forum);

		logService.logUserAction(user, "Forum", "Description of forum "
				+ _forum.getName() + " changed");

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setMembersOnly(User user, Forum forum, boolean membersOnly) {
		Forum _forum = forumDAO.load(forum.getId());

		_forum.setMembersOnly(membersOnly);

		forumDAO.update(_forum);

		logService.logUserAction(user, "Forum", "Forum " + _forum.getName()
				+ " is now " + (membersOnly ? " " : "not ")
				+ " visible for non-members");
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#setModeratorOnlyRestriction(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Forum, boolean)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setModeratorOnlyRestriction(User user, Forum forum,
			boolean membersOnly) {
		Forum _forum = forumDAO.load(forum.getId());

		_forum.setPublicAccess(membersOnly);

		forumDAO.update(_forum);

		logService.logUserAction(user, "Forum", "Forum " + _forum.getName()
				+ " is now " + (membersOnly ? " " : "not ")
				+ " accessible for non-members");

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#deleteForum(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Forum)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteForum(User user, Forum forum) {
		Forum _forum = forumDAO.load(forum.getId());

		if (_forum.getThreads().isEmpty()) {

			logService.logUserAction(user, "Forum", "Forum " + forum.getName()
					+ " deleted");

			forumCategoryDAO.evict(_forum.getCategory());
			forumDAO.evict(_forum);
			forumDAO.delete(_forum);

			return true;
		}

		return false;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#moveDown(com.tysanclan.site.projectewok.entities.Forum)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void moveDown(Forum forum) {
		Forum _forum = forumDAO.load(forum.getId());

		ForumCategory category = _forum.getCategory();

		int oldPosition = _forum.getPosition();
		int newPosition = oldPosition + 1;

		Forum forumOnNewPosition = null;

		for (Forum next : category.getForums()) {
			if (next.getPosition() == newPosition) {
				forumOnNewPosition = next;
				break;
			}
		}

		_forum.setPosition(newPosition);
		forumDAO.update(_forum);

		if (forumOnNewPosition != null) {
			forumOnNewPosition.setPosition(oldPosition);
			forumDAO.update(forumOnNewPosition);
		}

		forumCategoryDAO.evict(category);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#moveUp(com.tysanclan.site.projectewok.entities.Forum)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void moveUp(Forum forum) {
		Forum _forum = forumDAO.load(forum.getId());

		ForumCategory category = _forum.getCategory();

		int oldPosition = _forum.getPosition();
		int newPosition = oldPosition - 1;

		Forum forumOnNewPosition = null;

		for (Forum next : category.getForums()) {
			if (next.getPosition() == newPosition) {
				forumOnNewPosition = next;
				break;
			}
		}

		_forum.setPosition(newPosition);
		forumDAO.update(_forum);

		if (forumOnNewPosition != null) {
			forumOnNewPosition.setPosition(oldPosition);
			forumDAO.update(forumOnNewPosition);
		}

		forumCategoryDAO.evict(category);

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#moveToCategory(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Forum,
	 *      com.tysanclan.site.projectewok.entities.ForumCategory)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void moveToCategory(User user, Forum forum,
			ForumCategory forumCategory) {
		Forum _forum = forumDAO.load(forum.getId());
		ForumCategory _category = forumCategoryDAO.load(forumCategory.getId());

		if (!_category.equals(_forum.getCategory())) {
			_forum.setCategory(_category);
			int maxPos = 1;
			for (Forum next : _category.getForums()) {
				if (maxPos < next.getPosition()) {
					maxPos = next.getPosition();
				}
			}
			maxPos++;

			_forum.setPosition(maxPos);

			forumDAO.update(_forum);

			logService.logUserAction(user, "Forum", "Forum " + _forum.getName()
					+ " moved to category " + _category.getName());

			forumCategoryDAO.evict(_category);
			forumCategoryDAO.evict(forumCategory);
		}

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#addModerator(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Forum,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Forum addModerator(User adder, Forum forum, User moderator) {
		Forum _forum = forumDAO.load(forum.getId());

		_forum.getModerators().add(moderator);

		forumDAO.update(_forum);

		logService.logUserAction(adder, "Forum",
				"Assigned " + moderator.getUsername() + " as moderator of "
						+ forum.getName());

		return _forum;

	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#removeModerator(com.tysanclan.site.projectewok.entities.User,
	 *      com.tysanclan.site.projectewok.entities.Forum,
	 *      com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Forum removeModerator(User remover, Forum forum, User moderator) {
		Forum _forum = forumDAO.load(forum.getId());

		_forum.getModerators().remove(moderator);

		forumDAO.update(_forum);

		logService.logUserAction(remover, "Forum",
				"Removed " + moderator.getUsername() + " as moderator of "
						+ forum.getName());

		return _forum;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.ForumService#setThreadTitle(com.tysanclan.site.projectewok.entities.ForumThread,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setThreadTitle(ForumThread _thread, String newTitle) {
		ForumThread thread = forumThreadDAO.load(_thread.getId());

		thread.setTitle(newTitle);

		forumThreadDAO.update(thread);

	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public boolean isPostUnread(User user, ForumPost post) {
		if (user != null) {

			return forumDAO.isPostUnread(user, post);

			// for (UnreadForumPost unreadpost : user.getUnreadForumPosts()) {
			// if (unreadpost.getForumPost().equals(post)) {
			// return true;
			// }
			// }
		}
		return false;
	}

	@Override
	public int countUnread(User user) {
		if (user.getUnreadForumPosts() == null) {
			return 0;
		}
		return user.getUnreadForumPosts().size();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void clearUnreadPosts(User user) {
		for (UnreadForumPost unreadpost : user.getUnreadForumPosts()) {
			unreadForumPostDAO.delete(unreadpost);
		}
		user.getUnreadForumPosts().clear();
		// userDAO.update(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void clearUnreadPosts(User user, Forum forum) {
		ArrayList<UnreadForumPost> postToBeRemoved = new ArrayList<UnreadForumPost>();
		for (UnreadForumPost post : user.getUnreadForumPosts()) {
			if (post.getForumPost().getThread().getForum().equals(forum)) {
				postToBeRemoved.add(post);
			}
		}
		user.getUnreadForumPosts().removeAll(postToBeRemoved);
		for (UnreadForumPost unreadpost : postToBeRemoved) {
			unreadForumPostDAO.delete(unreadpost);
		}
		userDAO.update(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addUnreadPosts(User user) {

		List<ForumPost> unreadPosts = getUnreadPosts(user);
		for (ForumPost post : unreadPosts) {
			ForumThread thread = post.getThread();

			// Shadow threads are never unread
			if (thread.isShadow() || post.isShadow()) {
				continue;
			}

			// Same goes for posts in trial threads, unless certain conditions
			// apply
			Trial t = trialDAO.getTrialByThread(thread);
			if (t != null) {
				if (!t.getAccused().equals(user)) {
					Rank r = user.getRank();

					if (r != Rank.TRUTHSAYER && r != Rank.CHANCELLOR
							&& r != Rank.SENATOR) {
						continue;
					}

				}
			}

			Forum forum = thread.getForum();

			if (forum.canView(user)) {
				if (post.getPoster() == null
						|| !(post.getPoster().equals(user))) {
					UnreadForumPost upost = new UnreadForumPost(user, post);
					unreadForumPostDAO.save(upost);
					user.getUnreadForumPosts().add(upost);
				}
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void markForumPostRead(User user, ForumPost post) {
		if (user == null)
			return;

		unreadForumPostDAO.markAsRead(user, post);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int getForumThreadUnreadCount(User user, ForumThread thread) {
		int count = 0;
		for (UnreadForumPost post : user.getUnreadForumPosts()) {
			if (post.getForumPost().getThread().equals(thread)) {
				count++;
			}
		}
		return count;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int getForumUnreadCount(User user, Forum forum) {
		int count = 0;
		for (UnreadForumPost post : user.getUnreadForumPosts()) {
			if (post.getForumPost().getThread().getForum().equals(forum)) {
				count++;
			}
		}
		return count;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ForumPost> filterPosts(User user, boolean publicView,
			List<ForumPost> originalPosts) {
		List<ForumPost> pList = new LinkedList<ForumPost>();
		for (ForumPost _fp : originalPosts) {
			ForumPost fp = forumPostDAO.load(_fp.getId());

			if (publicView) {
				if (!fp.getThread().getForum().isMembersOnly()
						&& !fp.isShadow()) {
					pList.add(fp);
				}
			} else {
				if (user != null) {
					Forum f = forumDAO.load(fp.getThread().getForum().getId());

					if (f.isAccessible(user)
							&& (!fp.isShadow() || (fp.isShadow() && fp
									.getPoster().equals(user)))) {
						pList.add(fp);
					}
				}
			}
		}

		return pList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ForumThread> filterThreads(User user, Forum _forum,
			boolean publicView) {
		Forum forum = forumDAO.load(_forum.getId());
		List<ForumThread> tList = new LinkedList<ForumThread>();

		for (ForumThread ft : forum.getThreads()) {
			if (publicView) {
				if (!forum.isMembersOnly() && !ft.isShadow()
						&& forum.isAccessible(user)) {
					tList.add(ft);
				}
			} else {
				if (user != null) {
					if (forum.isAccessible(user)
							&& (!ft.isShadow() || (ft.isShadow() && ft
									.getPoster().equals(user)))) {
						tList.add(ft);
					}
				}
			}
		}

		Collections.sort(tList, forumThreadComparator);

		return tList;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ForumCategory> filterCategories(User user,
			List<ForumCategory> in, final boolean publicView) {
		List<ForumCategory> out = new LinkedList<ForumCategory>();

		for (ForumCategory fc : in) {
			if (filterForums(user, fc.getForums(), publicView).size() > 0) {
				out.add(fc);
			}
		}

		return out;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Forum> filterForums(User user, List<Forum> in,
			final boolean publicView) {
		List<Forum> out = new LinkedList<Forum>();

		for (Forum f : in) {
			if (!f.isMembersOnly() || MemberUtil.isMember(user)) {
				if (Hibernate.getClass(f) == GroupForum.class) {
					if (f.canCreateThread(user)) {
						out.add(f);
					}
				} else {
					out.add(f);
				}
			}
		}

		return out;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ForumPost getFirstPost(ForumThread thread) {
		return thread.getPosts().get(0);
	}

	private static class ForumThreadComparator implements
			Comparator<ForumThread>, Serializable {
		private static final long serialVersionUID = 1L;

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(ForumThread o1, ForumThread o2) {
			if (o1 == null || o2 == null) {
				return 0;
			}

			if (o1.isPostSticky() != o2.isPostSticky()) {
				return o1.isPostSticky() ? -1 : 1;
			}

			return -o1.getPostTime().compareTo(o2.getPostTime());
		}
	}

	private static ForumThreadComparator forumThreadComparator = new ForumThreadComparator();

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ForumThread> fiterAndSortThreads(User user, Forum forum,
			boolean publicView) {
		// Filter shadow threads
		List<ForumThread> tList = filterThreads(user, forum, publicView);

		Collections.sort(tList, new Comparator<ForumThread>() {

			@Override
			public int compare(ForumThread o1, ForumThread o2) {
				if (o1.isPostSticky() != o2.isPostSticky()) {
					if (o1.isPostSticky()) {
						return -1;
					}
					return 1;

				}

				Date d1 = extractLastPostTime(o1);
				Date d2 = extractLastPostTime(o2);

				return d2.compareTo(d1);
			}

			private Date extractLastPostTime(ForumThread o1) {
				Date d1 = o1.getPostTime();
				if (!o1.getPosts().isEmpty()) {
					int size = o1.getPosts().size();
					d1 = o1.getPosts().get(size - 1).getTime();
				}
				return d1;
			}

		});
		return tList;
	}

}
