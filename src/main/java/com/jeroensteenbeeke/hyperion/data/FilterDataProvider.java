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
	public Iterator<? extends T> iterator(long first, long count) {
		List<T> values = dao.findByFilter(filter, first, count);

		return values.iterator();
	}

	@Override
	public long size() {
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
