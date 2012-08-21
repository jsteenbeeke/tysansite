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
package com.tysanclan.site.projectewok.entities.dao.filters;

import java.util.Date;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.SenateElection;

/**
 * @author Jeroen Steenbeeke
 */
public class SenateElectionFilter extends SearchFilter<SenateElection> {
	private static final long serialVersionUID = 1L;

	private Date startBefore;
	private Date startAfter;

	/**
	 * @return the startBefore
	 */
	public Date getStartBefore() {
		return startBefore;
	}

	/**
	 * @param startBefore
	 *            the startBefore to set
	 */
	public void setStartBefore(Date startBefore) {
		this.startBefore = startBefore;
	}

	/**
	 * @return the startAfter
	 */
	public Date getStartAfter() {
		return startAfter;
	}

	/**
	 * @param startAfter
	 *            the startAfter to set
	 */
	public void setStartAfter(Date startAfter) {
		this.startAfter = startAfter;
	}
}
