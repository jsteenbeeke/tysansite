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
package com.tysanclan.site.projectewok.beans.impl;

import com.tysanclan.site.projectewok.entities.Notification;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.NotificationDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.NotificationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class NotificationServiceImpl
		implements com.tysanclan.site.projectewok.beans.NotificationService {
	@Autowired
	private NotificationDAO notificationDAO;

	@Autowired
	private UserDAO userDAO;

	/**
	 * @param notificationDAO the notificationDAO to set
	 */
	public void setNotificationDAO(NotificationDAO notificationDAO) {
		this.notificationDAO = notificationDAO;
	}

	/**
	 * @param userDAO the userDAO to set
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.NotificationService#dismissNotification(com.tysanclan.site.projectewok.entities.Notification)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void dismissNotification(Notification notification) {
		notificationDAO.load(notification.getId())
				.forEach(notificationDAO::delete);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.NotificationService#getNotificationsForUser(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<Notification> getNotificationsForUser(User user) {
		NotificationFilter filter = new NotificationFilter();
		filter.user(user);

		return notificationDAO.findByFilter(filter).toJavaList();
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.NotificationService#notifyUser(com.tysanclan.site.projectewok.entities.User,
	 * java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Notification notifyUser(User user, String message) {
		return userDAO.load(user.getId()).map(_user -> {
			Notification notification = new Notification();
			notification.setUser(_user);
			notification.setMessage(message);
			notification.setDate(new Date());

			notificationDAO.save(notification);

			return notification;
		}).getOrElseThrow(IllegalStateException::new);
	}

}
