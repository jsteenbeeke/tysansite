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
