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

import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.UserAgentService;
import com.tysanclan.site.projectewok.entities.MobileUserAgent;
import com.tysanclan.site.projectewok.entities.dao.MobileUserAgentDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.MobileUserAgentFilter;
import com.tysanclan.site.projectewok.event.LoginEvent;

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
		MobileUserAgent agent = userAgentDAO.load(_agent.getId());

		agent.setMobile(approved);

		userAgentDAO.update(agent);

		log.info("User agent " + agent.getIdentifier() + " is "
				+ (approved ? "" : "not ") + " a mobile phone");

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@OnFortuityEvent(LoginEvent.class)
	public void checkUserAgent(EventContext<LoginEvent> context) {
		WebClientInfo info = TysanSession.get().getClientInfo();

		String userAgent = info.getUserAgent();

		if (userAgent.length() > 255) {
			userAgent = userAgent.substring(0, 255);
		}

		MobileUserAgentFilter filter = new MobileUserAgentFilter();

		filter.setIdentifier(userAgent);

		if (userAgentDAO.countByFilter(filter) == 0) {
			MobileUserAgent agent = new MobileUserAgent();
			agent.setIdentifier(userAgent);
			switch (guessStatus(userAgent)) {
				case MOBILE:
					agent.setMobile(true);
					break;
				case PC:
					agent.setMobile(false);
					break;
				case UNKNOWN:
					agent.setMobile(null);
					break;

			}

			userAgentDAO.save(agent);
			log.info("Added new user agent: " + userAgent);

		}

	}

	private GuessedStatus guessStatus(String userAgent) {
		if (userAgent.contains("BlackBerry")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("Android")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("iPhone")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("iPod")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("iPad")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("SymbianOS")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("Maemo")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("armv6l")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("Windows CE")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("nook")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("Nokia")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("Wii")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("MIDP") || userAgent.contains("CLDC")) {
			return GuessedStatus.MOBILE;
		}

		if (userAgent.contains("Windows NT")) {
			return GuessedStatus.PC;
		}

		if (userAgent.contains("Intel Mac OS X")) {
			return GuessedStatus.PC;
		}

		if (userAgent.contains("Macintosh")) {
			return GuessedStatus.PC;
		}

		if (userAgent.contains("X11") && userAgent.contains("Linux")) {
			return GuessedStatus.PC;
		}

		return GuessedStatus.UNKNOWN;
	}

	private static enum GuessedStatus {
		PC, MOBILE, UNKNOWN;
	}
}
