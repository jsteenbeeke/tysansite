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

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.jeroensteenbeeke.hyperion.reflection.Reflector;

/**
 * @author Jeroen Steenbeeke
 */
@Deprecated
public abstract class Expression<T extends Serializable> implements IDetachable {

	private static final long serialVersionUID = 1L;

	private IModel<T> model;

	@SuppressWarnings("unchecked")
	protected Expression(T object) {
		if (object instanceof DomainObject) {
			model = (IModel<T>) ModelMaker.wrap((DomainObject) object);
		} else {
			model = new Model<T>(object);
		}
	}

	protected Expression() {
		model = new Model<T>();
	}

	public IModel<T> getModel() {
		return model;
	}

	public T getValue() {
		return model.getObject();
	}

	@Override
	public void detach() {
		model.detach();

	}

	public static <T extends Comparable<T> & Serializable> Expression<T> lessThan(
			final String property, T value) {
		return new Expression<T>(value) {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.add(Restrictions.lt(property, getValue()));
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean retain(Object object) {
				T objectValue = (T) Reflector.invokeGetter(object, property);

				return objectValue.compareTo(getValue()) < 0;
			}

		};
	}

	public static <T extends Comparable<T> & Serializable> Expression<T> greaterThan(
			final String property, T value) {
		return new Expression<T>(value) {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.add(Restrictions.gt(property, getValue()));
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean retain(Object object) {
				T objectValue = (T) Reflector.invokeGetter(object, property);

				return objectValue.compareTo(getValue()) > 0;
			}
		};
	}

	public static <T extends Comparable<T> & Serializable> Expression<T> lessThanOrEquals(
			final String property, final T value) {
		return new Expression<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.add(Restrictions.le(property, value));
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean retain(Object object) {
				T objectValue = (T) Reflector.invokeGetter(object, property);

				return objectValue.compareTo(value) <= 0;
			}
		};
	}

	public static <T extends Comparable<T> & Serializable> Expression<T> greaterThanOrEquals(
			final String property, T value) {
		return new Expression<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.add(Restrictions.ge(property, getValue()));
			}

			@SuppressWarnings("unchecked")
			@Override
			public boolean retain(Object object) {
				T objectValue = (T) Reflector.invokeGetter(object, property);

				return objectValue.compareTo(getValue()) >= 0;
			}

		};
	}

	public abstract void decorateCriteria(Criteria criteria);

	public abstract boolean retain(Object object);

	public static Expression<?> simpleSearchString(final String property,
			final String searchString) {
		return new Expression<String>() {
			private static final long serialVersionUID = 1L;

			private String criteriaFilter(String input) {
				StringBuilder filtered = new StringBuilder();

				for (char n : input.toCharArray()) {
					if (Character.isWhitespace(n)
							|| Character.isLetterOrDigit(n)) {
						filtered.append(n);
					} else if (n == '?') {
						filtered.append('_');
					} else if (n == '*') {
						filtered.append('%');
					}
				}

				return filtered.toString();
			}

			private String retainFilter(String input) {
				StringBuilder filtered = new StringBuilder();

				for (char n : input.toCharArray()) {
					if (Character.isWhitespace(n)
							|| Character.isLetterOrDigit(n)) {
						filtered.append(n);
					} else if (n == '?') {
						filtered.append('.');
					} else if (n == '*') {
						filtered.append(".*");
					}
				}

				return filtered.toString();
			}

			@Override
			public void decorateCriteria(Criteria criteria) {
				criteria.add(Restrictions.ilike(property,
						criteriaFilter(searchString)));
			}

			@Override
			public boolean retain(Object object) {
				String objectValue = Reflector.invokeGetter(object, property);

				return objectValue.matches(retainFilter(searchString));
			}

		};
	}
}
