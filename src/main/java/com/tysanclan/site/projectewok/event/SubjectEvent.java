package com.tysanclan.site.projectewok.event;

import com.jeroensteenbeeke.hyperion.events.Event;

public abstract class SubjectEvent<T> implements Event<T> {
	private final T subject;

	protected SubjectEvent(T subject) {
		super();
		this.subject = subject;
	}

	public final T getSubject() {
		return subject;
	}
}
