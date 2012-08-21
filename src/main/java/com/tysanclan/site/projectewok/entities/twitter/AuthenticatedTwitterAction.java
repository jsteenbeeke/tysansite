/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.entities.twitter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AccessType("field")
@DiscriminatorValue("AuthenticatedTwitterAction")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public abstract class AuthenticatedTwitterAction extends
        TwitterAction {
	public static final long serialVersionUID = 1L;

	// $P$

	/**
	 * Creates a new AuthenticatedTwitterAction object
	 */
	public AuthenticatedTwitterAction() {
		// $H$
	}

	// $GS$
}
