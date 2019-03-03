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

import com.tysanclan.site.projectewok.entities.LogItem;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.LogItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class LogServiceImpl
		implements com.tysanclan.site.projectewok.beans.LogService {
	@Autowired
	private LogItemDAO logItemDAO;

	/**
	 *
	 */
	public LogServiceImpl() {
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LogService#logSystemAction(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void logSystemAction(String category, String action) {
		logUserAction(null, category, action);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.LogService#logUserAction(com.tysanclan.site.projectewok.entities.User,
	 *      java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void logUserAction(User user, String category, String action) {
		LogItem logItem = new LogItem();
		logItem.setCategory(category);
		logItem.setMessage(action);
		logItem.setLogTime(System.currentTimeMillis());
		logItem.setUser(user);
		logItemDAO.save(logItem);
	}

	/**
	 * @param logItemDAO
	 *            the logItemDAO to set
	 */
	public void setLogItemDAO(LogItemDAO logItemDAO) {
		this.logItemDAO = logItemDAO;
	}

}
