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
