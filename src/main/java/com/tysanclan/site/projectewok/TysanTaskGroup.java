package com.tysanclan.site.projectewok;

import com.jeroensteenbeeke.hyperion.tardis.scheduler.TaskGroup;

public enum TysanTaskGroup implements TaskGroup {
	DEBUG("Debug"), CLEANUP("Cleanup"), MEMBERS("Members"), FINANCE(
			"Finance"), DEMOCRACY("Democracy"), ORGANIZATIONAL(
			"Organizational"), JUSTICE("Judicial"), NOTIFY("Notifications");

	private final String descriptor;

	TysanTaskGroup(String descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public String getDescriptor() {
		return descriptor;
	}
}
