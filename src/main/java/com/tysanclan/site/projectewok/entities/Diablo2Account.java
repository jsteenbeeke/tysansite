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
package com.tysanclan.site.projectewok.entities;

import org.hibernate.annotations.Cache;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@DiscriminatorValue("Diablo2Account")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Diablo2Account extends GameAccount {
	public static final long serialVersionUID = 1L;

	// $P$

	/**
	 * Creates a new Diablo2Account object
	 */
	public Diablo2Account() {
		// $H$
	}

	@Override
	protected String render() {
		return "*" + getName();
	}

	@Override
	public boolean isValid() {
		for (char c : getName().toCharArray()) {
			if (Character.isWhitespace(c)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public AccountType getType() {
		return AccountType.DIABLO2;
	}

	// $GS$
}
