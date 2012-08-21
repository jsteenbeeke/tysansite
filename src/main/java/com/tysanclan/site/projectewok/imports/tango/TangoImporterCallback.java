/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.imports.tango;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeroen Steenbeeke
 */
public class TangoImporterCallback {
	private Map<Long, ImportedObject> createdObjects;

	/**
	 * 
	 */
	public TangoImporterCallback() {
		createdObjects = new HashMap<Long, ImportedObject>();
	}

	public void registerImportedObject(Long key, Object object) {
		createdObjects.put(key, new ImportedObject(object));
	}

	public boolean hasImportedObject(Long key, Class<?> objectType) {
		if (createdObjects.containsKey(key)) {
			ImportedObject obj = createdObjects.get(key);
			if (objectType.isAssignableFrom(obj.getType())) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> T getImportedObject(Long key, Class<T> objectType) {
		if (hasImportedObject(key, objectType)) {
			ImportedObject obj = createdObjects.get(key);
			return (T) obj.getObject();
		}

		return null;
	}

	private static class ImportedObject {
		private Object object;

		/**
		 * 
		 */
		public ImportedObject(Object object) {
			this.object = object;
		}

		public Class<?> getType() {
			return object.getClass();
		}

		public Object getObject() {
			return object;
		}

	}
}
