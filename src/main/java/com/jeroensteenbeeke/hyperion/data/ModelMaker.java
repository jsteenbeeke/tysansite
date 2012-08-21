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
