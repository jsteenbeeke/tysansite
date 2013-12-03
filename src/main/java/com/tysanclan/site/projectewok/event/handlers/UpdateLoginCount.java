package com.tysanclan.site.projectewok.event.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.event.LoginEvent;
import com.tysanclan.site.projectewok.util.MemberUtil;

public class UpdateLoginCount implements EventHandler<LoginEvent> {
	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public EventResult onEvent(LoginEvent event) {
		User user = event.getSubject();

		if (MemberUtil.isMember(user)) {
			user.setLoginCount(user.getLoginCount() + 1);
			userDAO.update(user);
		}

		return EventResult.ok();
	}

}
