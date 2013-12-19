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
package com.jeroensteenbeeke.hyperion.data;

import org.hibernate.Hibernate;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class BaseDomainObject implements DomainObject {
	private static final long serialVersionUID = 1L;

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((getDomainObjectId() == null) ? 0 : getDomainObjectId()
						.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (Hibernate.getClass(this) != Hibernate.getClass(obj)) {
			return false;
		}
		BaseDomainObject other = (BaseDomainObject) obj;
		if (getDomainObjectId() == null) {
			if (other.getDomainObjectId() != null) {
				return false;
			}
		} else if (!getDomainObjectId().equals(other.getDomainObjectId())) {
			return false;
		}
		return true;
	}

}
