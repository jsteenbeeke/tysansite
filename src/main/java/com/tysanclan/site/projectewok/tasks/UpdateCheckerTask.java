package com.tysanclan.site.projectewok.tasks;

import com.jeroensteenbeeke.hyperion.tardis.scheduler.HyperionTask;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.ServiceProvider;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.wicket.HyperionScheduler;
import com.tysanclan.site.projectewok.SiteWideNotification;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.TysanTaskGroup;
import org.apache.wicket.util.time.Duration;

import java.io.File;

public class UpdateCheckerTask extends HyperionTask {

	public static final String WARNING_MESSAGE = "Site update started. All sessions will be logged out in less than 5 minutes";

	public UpdateCheckerTask() {
		super("Check if site is being updated", TysanTaskGroup.NOTIFY);
	}

	@Override
	public void run(ServiceProvider provider) {
		File tempDir = new File(System.getProperty("java.io.tmpdir"));

		if (tempDir.exists()) {
			File monitorFile = new File(tempDir, "ewok.deploy");

			if (monitorFile.exists()) {
				if (TysanApplication
						.get()
						.getActiveNotifications()
						.stream()
						.map(SiteWideNotification::getMessage)
						.noneMatch(
								WARNING_MESSAGE::equals
						)) {

					TysanApplication.get().notify(new SiteWideNotification(SiteWideNotification.Category.WARNING,
																		   WARNING_MESSAGE, Duration.ONE_HOUR
					));
				}
			}
		}
	}
}
