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

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

public class FilterDataProvider<S extends SearchFilter<T>, T extends DomainObject>
		implements IDataProvider<T> {
	private static final long serialVersionUID = 1L;

	private DAO<T> dao;

	private S filter;

	private FilterDataProvider(S filter, DAO<T> dao) {
		this.filter = filter;
		this.dao = dao;
	}

	@Override
	public Iterator<? extends T> iterator(int first, int count) {
		List<T> values = dao.findByFilter(filter, first, count);

		return values.iterator();
	}

	@Override
	public int size() {
		return dao.countByFilter(filter);
	}

	@Override
	public void detach() {
		filter.detach();

	}

	@Override
	public IModel<T> model(T object) {
		return ModelMaker.wrap(object);
	}

	public static <S extends SearchFilter<T>, T extends DomainObject> FilterDataProvider<S, T> of(
			S filter, DAO<T> dao) {
		return new FilterDataProvider<S, T>(filter, dao);
	}
}
