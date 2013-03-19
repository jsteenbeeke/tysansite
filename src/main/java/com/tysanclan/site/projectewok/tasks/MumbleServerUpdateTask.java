package com.tysanclan.site.projectewok.tasks;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.MumbleService;
import com.tysanclan.site.projectewok.util.scheduler.PeriodicTask;

public class MumbleServerUpdateTask extends PeriodicTask {
	@SpringBean
	private MumbleService mumbleService;

	public MumbleServerUpdateTask() {
		super("Mumble Status Checker", "Communication",
				ExecutionMode.EVERY_FIVE_MINUTES);
	}

	@Override
	public void run() {
		mumbleService.updateStatuses();
	}
}
