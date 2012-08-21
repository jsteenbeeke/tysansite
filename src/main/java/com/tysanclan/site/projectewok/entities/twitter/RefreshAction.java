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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;

import com.tysanclan.site.projectewok.beans.ActionResolver;
import com.tysanclan.site.projectewok.beans.twitter.RefreshActionResolver;
import com.tysanclan.site.projectewok.entities.Profile;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@DiscriminatorValue("RefreshAction")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class RefreshAction extends TwitterAction {
	public static final long serialVersionUID = 1L;

	@ManyToOne
	private Profile profile;

	// $P$

	/**
	 * Creates a new RefreshAction object
	 */
	public RefreshAction() {
		// $H$
	}

	/**
	 * @param profile
	 *            the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}

	@Override
	public Class<? extends ActionResolver<?>> getResolverType() {
		return RefreshActionResolver.class;
	}

	// $GS$
}
