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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.jeroensteenbeeke.hyperion.reflection.Reflector;

/**
 * Class for dealing with properties that can be set to null
 * 
 * @author Jeroen Steenbeeke
 */
@Deprecated
public abstract class Nullable<T extends Serializable> extends Expression<T> {
	private static final long serialVersionUID = 1L;

	private Nullable() {
		super();
	}

	private Nullable(T object) {
		super(object);
	}

	public static <T extends Serializable> Nullable<T> isNull(
			final String property, Class<T> returnType) {
		return new Nullable<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				Criterion c = Restrictions.isNull(property);
				criteria.add(c);
			}

			@Override
			public boolean retain(Object object) {
				Object res = Reflector.invokeGetter(object, property);

				return res == null;
			}
		};
	}

	public static <T extends Serializable> Nullable<T> isNotNull(
			final String property, Class<T> requiredType) {
		return new Nullable<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.add(Restrictions.isNotNull(property));
			}

			@Override
			public boolean retain(Object object) {
				Object res = Reflector.invokeGetter(object, property);

				return res != null;
			}
		};
	}

	public static <T extends Serializable> Nullable<T> equals(
			final String property, T value) {
		return new Nullable<T>(value) {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.add(Restrictions.eq(property, getValue()));
			}

			@Override
			public boolean retain(Object object) {
				return getValue().equals(
						Reflector.invokeGetter(object, property));
			}
		};
	}
}
