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

import java.io.Serializable;
import java.util.List;

/**
 * A data access object for accessing data
 * 
 * @param T
 *            The type of domain object
 * @author Jeroen Steenbeeke
 */
public interface DAO<T extends DomainObject> {
	/**
	 * @return The total number of items of this type
	 */
	public abstract long countAll();

	/**
	 * Delete the indicated object
	 * 
	 * @param object
	 *            The object to delete
	 */
	public abstract void delete(T object);

	/**
	 * Ensures that the given object no longer persists in the cache
	 * 
	 * @param object
	 *            The object to evict
	 */
	public abstract void evict(T object);

	/**
	 * Get a list of all objects
	 * 
	 * @return The list of objects, which may be empty
	 */
	public abstract List<T> findAll();

	/**
	 * Load the object with the indicated ID
	 * 
	 * @param id
	 *            The ID of the requested object
	 * @return The loaded object, or <code>null</code> if it does not exist
	 */
	public abstract T load(Serializable id);

	/**
	 * Save the indicated object
	 * 
	 * @param object
	 *            The object to save
	 */
	public abstract void save(T object);

	public abstract void update(T object);

	T getUniqueByFilter(SearchFilter<T> filter);

	long countByFilter(SearchFilter<T> filter);

	List<T> findByFilter(SearchFilter<T> filter);

	List<T> findByFilter(SearchFilter<T> filter, long offset, long count);
}
