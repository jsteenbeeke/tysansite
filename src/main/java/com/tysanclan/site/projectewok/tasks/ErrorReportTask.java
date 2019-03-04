package com.tysanclan.site.projectewok.tasks;

import com.jeroensteenbeeke.hyperion.rollbar.RollBarReference;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.HyperionTask;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.ServiceProvider;
import com.tysanclan.site.projectewok.TysanTaskGroup;
import io.vavr.collection.HashMap;

import java.io.Serializable;
import java.util.Map;

public class ErrorReportTask extends HyperionTask {
	private final ErrorData errorData;

	public ErrorReportTask(ErrorData errorData) {
		super("Send error to Rollbar", TysanTaskGroup.DEBUG);
		this.errorData = errorData;
	}

	@Override
	public void run(ServiceProvider provider) {
		RollBarReference.instance.errorCaught(errorData.error, errorData.customData);
	}

	public static class ErrorData implements Serializable {
		public static final int serialVersionUID = 1;

		private final Throwable error;

		private final Map<String,Object> customData;

		public ErrorData(Throwable error, String target, String referrer) {
			this.error = error;
			this.customData = HashMap.<String, Object>
					of("target", target, "referrer", referrer)
					.toJavaMap();
		}

		public Map<String, Object> getCustomData() {
			return customData;
		}
	}
}
