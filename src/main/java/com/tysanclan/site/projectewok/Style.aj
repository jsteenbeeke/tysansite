/*
 * Tysan Clan Website
 * Copyright (C) 2008-2010 Jeroen Steenbeeke
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
package com.tysanclan.site.projectewok;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Scope;

import com.jeroensteenbeeke.hyperion.data.DAO;
import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;


/**
 * @author jeroen
 */
public aspect Style {
	public pointcut isDeprecated(): @withincode(Deprecated) || @within(Deprecated);
	
	public pointcut isMarkedTransactional(): @withincode(Transactional) || @within(Transactional);
	
	public pointcut isMarkedComponent(): @withincode(Component) || @within(Component) || @withincode(Scope) || @within(Scope);
	
	public pointcut isEntity(): withincode(* DomainObject.*(..)) || within(DomainObject);
	
	public pointcut isDAO(): withincode(* EwokHibernateDAO.*(..)) || within(EwokHibernateDAO);
	
	public pointcut isStartupResourceProvider(): withincode(* com.tysanclan.site.projectewok.StartupResourceProvider.*(..)) || within(com.tysanclan.site.projectewok.StartupResourceProvider);
	
	declare error: call(* DAO.save(..)) && !isMarkedTransactional() && !isDeprecated(): "Only call save() from within methods marked @Transactional";
	
	declare error: call(* DAO.update(..)) && !isMarkedTransactional() && !isDeprecated(): "Only call update() from within methods marked @Transactional";
	
	declare error: call(* DAO.update(..)) && !isMarkedTransactional() && !isDeprecated(): "Only call delete() from within methods marked @Transactional";
	
	declare error: call(* DAO.*(..)) && isEntity(): "Do not call DAO methods from entities";
	
	declare error: isMarkedComponent() && call(* org.apache.wicket.Session+.*(..)): "Don not refer to the Wicket session from components";
	
	declare error: !isMarkedComponent() && isMarkedTransactional() && !isStartupResourceProvider() && !isDAO(): "Do not use the @Transaction annotations for non-Spring beans";
}
