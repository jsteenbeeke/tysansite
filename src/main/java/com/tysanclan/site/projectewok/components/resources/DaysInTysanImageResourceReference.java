package com.tysanclan.site.projectewok.components.resources;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class DaysInTysanImageResourceReference extends ResourceReference {

	private static final long serialVersionUID = 1L;

	public DaysInTysanImageResourceReference() {
		super(DaysInTysanImageResourceReference.class, "daysInTysan");
	}

	@Override
	public IResource getResource() {
		return new DaysInTysanImageResource();
	}
}
