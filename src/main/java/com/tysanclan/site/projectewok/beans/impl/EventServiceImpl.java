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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.entities.Event;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.EventDAO;
import com.tysanclan.site.projectewok.util.StringUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class EventServiceImpl implements
		com.tysanclan.site.projectewok.beans.EventService {
	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private com.tysanclan.site.projectewok.beans.LogService logService;

	@Autowired
	private com.tysanclan.site.projectewok.beans.ForumService forumService;

	/**
	 * @see com.tysanclan.site.projectewok.beans.EventService#scheduleEvent(com.tysanclan.site.projectewok.entities.User,
	 *      java.util.Date, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Event scheduleEvent(User user, Date date, String title,
			String description) {

		Forum forum = forumService.getInteractionForum();

		ForumThread thread = forumService.createForumThread(forum,
				StringUtil.combineStrings("Event: ", title), description, user);

		Event event = new Event();
		event.setDate(date);
		event.setOrganizer(user);
		event.setEventThread(thread);

		eventDAO.save(event);

		logService.logUserAction(user, "Events",
				StringUtil.combineStrings("User has scheduled ", title));

		return event;
	}

	/**
	 * @param eventDAO
	 *            the eventDAO to set
	 */
	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	/**
	 * @param logService
	 *            the logService to set
	 */
	public void setLogService(
			com.tysanclan.site.projectewok.beans.LogService logService) {
		this.logService = logService;
	}

	/**
	 * @param forumService
	 *            the forumService to set
	 */
	public void setForumService(
			com.tysanclan.site.projectewok.beans.ForumService forumService) {
		this.forumService = forumService;
	}

}
