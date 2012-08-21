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
