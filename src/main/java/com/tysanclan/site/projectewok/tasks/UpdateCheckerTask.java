package com.tysanclan.site.projectewok.tasks;

import com.jeroensteenbeeke.hyperion.tardis.scheduler.HyperionTask;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.ServiceProvider;
import com.tysanclan.site.projectewok.SiteWideNotification;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.TysanTaskGroup;
import org.apache.wicket.util.time.Duration;

import java.io.File;

public class UpdateCheckerTask extends HyperionTask {

	public UpdateCheckerTask() {
		super("Check if site is being updated", TysanTaskGroup.NOTIFY);
	}

	@Override
	public void run(ServiceProvider provider) {
		File tempDir = new File(System.getProperty("java.io.tmpdir"));

		if (tempDir.exists()) {
			File monitorFile = new File(tempDir, "ewok.deploy");

			if (monitorFile.exists()) {
				TysanApplication.get().notify(new SiteWideNotification(SiteWideNotification.Category.WARNING,
																	   "Site update started. All sessions will be logged out in less than 5 minutes", Duration.ONE_HOUR
				));

				monitorFile.delete();
			}
		}
	}
}
