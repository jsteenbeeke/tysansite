package com.tysanclan.site.projectewok.components.resources;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class MinecraftWhitelistResourceReference extends ResourceReference {
	private static final long serialVersionUID = 1L;

	public MinecraftWhitelistResourceReference() {
		super(MinecraftWhitelistResourceReference.class, "whitelist");
	}

	@Override
	public IResource getResource() {

		return new MinecraftWhitelistResource();
	}
}
