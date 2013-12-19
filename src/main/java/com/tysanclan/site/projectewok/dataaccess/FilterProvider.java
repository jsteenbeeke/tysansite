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
package com.tysanclan.site.projectewok.dataaccess;

import java.util.Iterator;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.TysanApplication;

/**
 * @author Jeroen Steenbeeke
 */
public class FilterProvider<T extends DomainObject, D extends EwokDAO<T>>
		implements IDataProvider<T> {
	private static final long serialVersionUID = 1L;

	private transient D dao;

	private Class<D> daoClass;

	private SearchFilter<T> filter;

	private FilterProvider(Class<D> daoClass, SearchFilter<T> filter) {
		this.daoClass = daoClass;
		this.filter = filter;

		verifyDAO();

	}

	private void verifyDAO() {
		if (dao == null) {
			dao = TysanApplication.getApplicationContext().getBean(daoClass);
		}
	}

	public static <TT extends DomainObject, DD extends EwokDAO<TT>> FilterProvider<TT, DD> of(
			Class<DD> daoClass, SearchFilter<TT> filter) {
		return new FilterProvider<TT, DD>(daoClass, filter);
	}

	@Override
	public void detach() {
		dao = null;
		filter.detach();
	}

	@Override
	public Iterator<? extends T> iterator(long first, long count) {
		verifyDAO();

		return dao.findByFilter(filter, first, count).iterator();
	}

	@Override
	public long size() {
		verifyDAO();

		return dao.countByFilter(filter);
	}

	@Override
	public IModel<T> model(T object) {
		return ModelMaker.wrap(object);
	}
}
