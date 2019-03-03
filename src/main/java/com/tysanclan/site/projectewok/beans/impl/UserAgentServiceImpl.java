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

import com.tysanclan.site.projectewok.beans.UserAgentService;
import com.tysanclan.site.projectewok.entities.MobileUserAgent;
import com.tysanclan.site.projectewok.entities.dao.MobileUserAgentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jeroen Steenbeeke
 *
 */
@Component
@Scope("request")
class UserAgentServiceImpl implements UserAgentService {
	private static Logger log = LoggerFactory
			.getLogger(UserAgentServiceImpl.class);

	@Autowired
	private MobileUserAgentDAO userAgentDAO;

	public void setUserAgentDAO(MobileUserAgentDAO userAgentDAO) {
		this.userAgentDAO = userAgentDAO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setAgentStatus(MobileUserAgent _agent, boolean approved) {
		userAgentDAO.load(_agent.getId()).forEach(agent -> {

			agent.setMobile(approved);

			userAgentDAO.update(agent);

			log.info(
					"User agent " + agent.getIdentifier() + " is " + (approved ?
							"" :
							"not ") + " a mobile phone");
		});

	}

}
