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

import java.io.Serializable;

import org.hibernate.Criteria;

@Deprecated
public abstract class Order<T extends Serializable> extends Expression<T> {
	private static final long serialVersionUID = 1L;

	public static <T extends Serializable> Order<T> ascending(
			final String property) {
		return new Order<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.addOrder(org.hibernate.criterion.Order.asc(property));
			}

			@Override
			public boolean retain(Object object) {
				return true;
			}
		};
	}

	public static <T extends Serializable> Order<T> descending(
			final String property) {
		return new Order<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.addOrder(org.hibernate.criterion.Order.desc(property));
			}

			@Override
			public boolean retain(Object object) {
				return true;
			}

		};
	}

}
