package com.tysanclan.site.projectewok.event.handlers;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class ResetMentorOnTermination implements
		EventHandler<MembershipTerminatedEvent> {
	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	@Nonnull
	public EventResult onEvent(@Nonnull MembershipTerminatedEvent event) {
		User user = event.getSubject();

		if (user.getMentor() != null) {
			user.getMentor().getPupils().remove(user);

			userDAO.update(user.getMentor());

			user.setMentor(null);

			userDAO.update(user);

		}

		return EventResult.ok();
	}
}
