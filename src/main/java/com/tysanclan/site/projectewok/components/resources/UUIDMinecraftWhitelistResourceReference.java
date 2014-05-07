package com.tysanclan.site.projectewok.components.resources;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class UUIDMinecraftWhitelistResourceReference extends ResourceReference {
	private static final long serialVersionUID = 1L;

	public UUIDMinecraftWhitelistResourceReference() {
		super(UUIDMinecraftWhitelistResourceReference.class, "uuid-whitelist");
	}

	@Override
	public IResource getResource() {

		return new UUIDMinecraftWhitelistResource();
	}
}
