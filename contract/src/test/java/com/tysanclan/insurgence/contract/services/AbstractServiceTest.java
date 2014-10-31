package com.tysanclan.insurgence.contract.services;

import org.mockito.Mockito;

public abstract class AbstractServiceTest<T> {

	private final Class<T> serviceClass;

	protected AbstractServiceTest(Class<T> serviceClass) {
		super();
		this.serviceClass = serviceClass;
	}

	private T service = null;

	protected void setupMock(T base) {

	}

	public T service() {
		if (service == null) {
			service = createService();
		}

		return service;
	}

	protected T createService() {
		// Protected so all declared tests can be overridden in actual backend
		// implementation and reused
		T svc = Mockito.mock(serviceClass);
		setupMock(svc);

		return svc;
	}

}
