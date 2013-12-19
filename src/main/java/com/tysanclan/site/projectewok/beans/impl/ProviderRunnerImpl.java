/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.beans.impl;

import org.apache.wicket.SharedResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.StartupResourceProvider;
import com.tysanclan.site.projectewok.beans.ProviderRunner;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ProviderRunnerImpl implements ProviderRunner, ApplicationContextAware {
	private static Logger log = LoggerFactory
			.getLogger(ProviderRunnerImpl.class);

	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.context = applicationContext;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void run(SharedResources resources, StartupResourceProvider provider) {
		log.info("Loading resources from " + provider.getClass().getName());

		context.getAutowireCapableBeanFactory().autowireBean(provider);

		provider.registerResources(resources);

		log.info("Loading resources from " + provider.getClass().getName()
				+ " completed!");
	}

}
