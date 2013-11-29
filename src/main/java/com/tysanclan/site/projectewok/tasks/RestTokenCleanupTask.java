package com.tysanclan.site.projectewok.tasks;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.entities.dao.RestTokenDAO;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

public class RestTokenCleanupTask extends PeriodicTask {
	@SpringBean
	private RestTokenDAO restTokenDAO;

	public RestTokenCleanupTask() {
		super("Rest Token Cleanup", "Webservices",
				ExecutionMode.EVERY_FIVE_MINUTES);
	}

	@Override
	public void run() {
		restTokenDAO.cleanExpiredTokens();
	}

}
