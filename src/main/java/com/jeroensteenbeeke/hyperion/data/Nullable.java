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
