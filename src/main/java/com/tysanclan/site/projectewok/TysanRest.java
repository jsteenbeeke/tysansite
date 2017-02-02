package com.tysanclan.site.projectewok;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.tysanclan.site.projectewok.rs.services.RestTokenService;

@ApplicationPath("/rs")
public class TysanRest extends Application {
	private Set<Object> singletons = new HashSet<Object>();

	public TysanRest() {
		singletons.add(new RestTokenService());

	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
