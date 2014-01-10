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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

import com.tysanclan.rest.api.data.Rank;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@DiscriminatorValue("NewsForum")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "forum")
public class NewsForum extends Forum {
	public static final long serialVersionUID = 1L;

	// $P$

	/**
	 * Creates a new NewsForum object
	 */
	public NewsForum() {
		// $H$
	}

	@Override
	public boolean canCreateThread(User u) {
		return u != null
				&& (getModerators().contains(u)
						|| u.getRank() == Rank.CHANCELLOR
						|| u.getRank() == Rank.SENATOR || u.getRank() == Rank.TRUTHSAYER);
	}

	@Override
	public boolean canView(User user) {
		return true;
	}

	// $GS$
}
