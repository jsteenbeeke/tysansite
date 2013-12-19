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

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author Jeroen Steenbeeke
 */
class EntityExtendListModelAdapter<T extends DomainObject>
        extends LoadableDetachableModel<List<? extends T>>
        implements IModel<List<? extends T>> {
	private static final long serialVersionUID = 1L;

	private EntityListModel<T> listModel;

	public EntityExtendListModelAdapter(List<T> entityList) {
		listModel = new EntityListModel<T>(entityList);
	}

	/**
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected List<? extends T> load() {
		return listModel.load();
	}

	/**
	 * @see org.apache.wicket.model.LoadableDetachableModel#detach()
	 */
	@Override
	public void detach() {
		listModel.detach();
		super.detach();
	}

}
