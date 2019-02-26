package com.tysanclan.site.projectewok;

public enum TysanApplicationReference {
	INSTANCE;

	private TysanApplication application;

	public TysanApplication getApplication() {
		return application;
	}

	public void setApplication(TysanApplication application) {
		this.application = application;
	}
}
