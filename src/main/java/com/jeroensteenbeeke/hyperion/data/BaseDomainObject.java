/*
 * Copyright 2010-2011 Jeroen Steenbeeke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
