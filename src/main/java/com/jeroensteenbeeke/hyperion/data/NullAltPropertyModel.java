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

import com.jeroensteenbeeke.hyperion.reflection.Reflector;

public class NullAltPropertyModel<T extends DomainObject> implements IModel<String> {
	private static final long serialVersionUID = 1L;

	private final String property;

	private final String alt;

	private IModel<T> object;

	private final Class<T> objectClass;

	@SuppressWarnings("unchecked")
	public NullAltPropertyModel(T object, String property, String alt) {
		this.property = property;
		this.alt = alt;
		this.object = ModelMaker.wrap(object);
		if (object == null) {
			this.objectClass = null;
		} else {
			this.objectClass = (Class<T>) object.getClass();
		}
	}

	@Override
	public String getObject() {
		T obj = object.getObject();

		if (obj != null) {
			return (String) Reflector.getProperty(objectClass, property).get(
					obj);
		}

		return alt;
	}

	@Override
	public void setObject(String object) {
		// Does nothing

	}

	@Override
	public void detach() {
		object.detach();
	}

}
