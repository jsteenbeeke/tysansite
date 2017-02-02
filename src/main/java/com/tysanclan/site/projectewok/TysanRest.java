package com.tysanclan.site.projectewok;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.common.collect.ImmutableSet;
import com.tysanclan.site.projectewok.rs.services.OverviewResource;
import com.tysanclan.site.projectewok.rs.services.RestTokenService;

@ApplicationPath("/rs")
public class TysanRest extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		return ImmutableSet.of(RestTokenService.class, OverviewResource.class);
	}
}
