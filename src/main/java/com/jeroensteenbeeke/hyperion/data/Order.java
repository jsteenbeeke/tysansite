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
