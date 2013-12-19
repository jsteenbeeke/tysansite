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
package com.tysanclan.site.projectewok.entities.dao.filters;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.MobileUserAgent;

/**
 * @author Jeroen Steenbeeke
 */
public class MobileUserAgentFilter extends SearchFilter<MobileUserAgent> {
	private static final long serialVersionUID = 1L;
	private boolean searchUnknownType = false;

	private String identifier;

	private Boolean mobile;

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setMobile(Boolean mobile) {
		this.mobile = mobile;
	}

	public String getIdentifier() {
		return identifier;
	}

	public Boolean getMobile() {
		return mobile;
	}

	public void setSearchUnknownType(boolean searchUnknownType) {
		this.searchUnknownType = searchUnknownType;
	}

	public boolean isSearchUnknownType() {
		return searchUnknownType;
	}
}
