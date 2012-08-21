package com.tysanclan.site.projectewok.beans.mock;

import java.util.List;

import com.fortuityframework.core.dispatch.EventException;
import com.fortuityframework.core.dispatch.IEventBroker;
import com.fortuityframework.core.event.Event;

public class MockEventBroker implements IEventBroker {
	@Override
	public void dispatchEvent(Event<?> event) throws EventException {

	}

	@Override
	public void dispatchEvents(List<Event<?>> events) throws EventException {

	}
}
