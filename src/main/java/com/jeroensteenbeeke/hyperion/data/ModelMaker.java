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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.model.IModel;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class ModelMaker {
	public static <T extends DomainObject> IModel<T> wrap(
	        T object) {
		return new PersistenceModel<T>(object);
	}

	public static <T extends DomainObject> IModel<List<T>> wrap(
	        List<T> list) {
		return new EntityListModel<T>(list);
	}

	public static <T extends DomainObject> IModel<? extends List<? extends T>> wrapChoices(
	        List<T> list) {
		return new EntityExtendListModelAdapter<T>(list);
	}

	public static <T extends DomainObject> IModel<Collection<T>> wrapAsCollection(
	        List<T> list) {
		return new CollectionListModelAdapter<T>(list);
	}

	public static <T extends DomainObject> IModel<T> wrap(
	        T object, boolean writeable) {
		return writeable ? new WriteablePersistenceModelAdapter<T>(
		        object)
		        : wrap(object);
	}

	private static class CollectionListModelAdapter<T extends DomainObject>
	        implements IModel<Collection<T>> {
		private static final long serialVersionUID = 1L;

		private IModel<List<T>> wrappedModel;

		/**
         * 
         */
		public CollectionListModelAdapter(List<T> list) {
			wrappedModel = wrap(list);
		}

		/**
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		@Override
		public void detach() {
			wrappedModel.detach();
		}

		/**
		 * @see org.apache.wicket.model.IModel#getObject()
		 */
		@Override
		public Collection<T> getObject() {
			return wrappedModel.getObject();
		}

		/**
		 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
		 */
		@Override
		public void setObject(Collection<T> object) {
			List<T> list = new LinkedList<T>();
			list.addAll(object);
			wrappedModel = wrap(list);
		}

	}
}
