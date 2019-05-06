package com.tysanclan.site.projectewok.beans.impl;

import com.jeroensteenbeeke.hyperion.solstice.spring.IEntityPopulator;
import com.tysanclan.site.projectewok.beans.PopulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PopulationServiceInvoker implements IEntityPopulator {
	@Autowired
	private PopulationService populationService;

	@Override
	public void createEntities() {
		 populationService.createDebugSite();
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
