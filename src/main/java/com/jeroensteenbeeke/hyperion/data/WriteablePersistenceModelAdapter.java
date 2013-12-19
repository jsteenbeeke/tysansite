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

import org.apache.wicket.model.IModel;

/**
 * @author Jeroen Steenbeeke
 */
class WriteablePersistenceModelAdapter<T extends DomainObject>
        implements IModel<T> {
	private static final long serialVersionUID = 1L;

	private PersistenceModel<T> model;

	/**
     * 
     */
	public WriteablePersistenceModelAdapter(T subject) {
		model = new PersistenceModel<T>(subject);
	}

	/**
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	public T getObject() {
		return model.getObject();
	}

	/**
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	public void setObject(T object) {
		model.detach();
		model = new PersistenceModel<T>(object);
	}

	/**
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	public void detach() {
		model.detach();

	}

}
