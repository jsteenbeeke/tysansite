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
package com.tysanclan.site.projectewok.entities;

import java.util.regex.Pattern;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@DiscriminatorValue("RealIdAccount")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class RealIdAccount extends GameAccount {
	public static final long serialVersionUID = 1L;

	private static final Pattern pattern = Pattern
			.compile(
					"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z]{2,}){1}$)",
					Pattern.CASE_INSENSITIVE);

	// $P$

	/**
	 * Creates a new RealIdAccount object
	 */
	public RealIdAccount() {
		// $H$
	}

	@Override
	protected String render() {
		return getName();
	}

	@Override
	public boolean isValid() {
		return pattern.matcher(getName()).matches();
	}

	@Override
	public AccountType getType() {
		return AccountType.REALID;
	}

	// $GS$
}
