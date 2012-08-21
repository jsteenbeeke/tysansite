package com.tysanclan.site.projectewok.tasks;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.entities.MumbleServer;
import com.tysanclan.site.projectewok.entities.dao.MumbleServerDAO;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;
import com.tysanclan.site.projectewok.ws.mumble.MMOMumbleServerStatus;

public class MumbleServerUpdateTask extends PeriodicTask {
	@SpringBean
	private MumbleServerDAO mumbleServerDAO;

	public MumbleServerUpdateTask() {
		super("Mumble Status Checker", "Communication",
				ExecutionMode.EVERY_FIVE_MINUTES);
	}

	@Override
	public void run() {
		for (MumbleServer server : mumbleServerDAO.findAll()) {
			MMOMumbleServerStatus.fetchStatus(server.getId(),
					server.getApiToken(), server.getApiSecret());
		}
	}
}
