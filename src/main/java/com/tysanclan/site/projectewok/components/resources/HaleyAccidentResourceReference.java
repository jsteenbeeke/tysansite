package com.tysanclan.site.projectewok.components.resources;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class HaleyAccidentResourceReference extends ResourceReference {
	private static final long serialVersionUID = 1L;

	public HaleyAccidentResourceReference() {
		super(HaleyAccidentResourceReference.class, "haley");
	}

	@Override
	public IResource getResource() {

		return new HaleyAccidentImage();
	}
}
