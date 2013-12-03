package com.tysanclan.site.projectewok.event.handlers;

import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.entities.MobileUserAgent;
import com.tysanclan.site.projectewok.entities.dao.MobileUserAgentDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.MobileUserAgentFilter;
import com.tysanclan.site.projectewok.event.LoginEvent;

public class CheckUserAgentOnLogin implements EventHandler<LoginEvent> {
	private static final Logger log = LoggerFactory
			.getLogger(CheckUserAgentOnLogin.class);

	@Autowired
	private MobileUserAgentDAO userAgentDAO;

	public void setUserAgentDAO(MobileUserAgentDAO userAgentDAO) {
		this.userAgentDAO = userAgentDAO;
	}

	@Override
	public EventResult onEvent(LoginEvent event) {
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

		return EventResult.ok();
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
