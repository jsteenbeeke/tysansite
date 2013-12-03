package com.tysanclan.site.projectewok.event.handlers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.events.EventHandler;
import com.jeroensteenbeeke.hyperion.events.EventResult;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.RealmFilter;
import com.tysanclan.site.projectewok.event.MembershipTerminatedEvent;

public class ResetOverseerOnTermination implements
		EventHandler<MembershipTerminatedEvent> {
	@Autowired
	private RealmDAO realmDAO;

	public void setRealmDAO(RealmDAO realmDAO) {
		this.realmDAO = realmDAO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public EventResult onEvent(MembershipTerminatedEvent event) {
		User user = event.getSubject();

		RealmFilter rfilter = new RealmFilter();
		rfilter.setOverseer(user);

		List<Realm> realms = realmDAO.findByFilter(rfilter);
		for (Realm realm : realms) {
			realm.setOverseer(null);

			realmDAO.update(realm);
		}
		return null;
	}
}
