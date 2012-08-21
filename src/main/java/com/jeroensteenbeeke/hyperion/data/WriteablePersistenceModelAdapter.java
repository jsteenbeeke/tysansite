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
