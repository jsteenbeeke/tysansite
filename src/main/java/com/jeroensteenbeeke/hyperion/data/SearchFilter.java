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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.model.IModel;

public abstract class SearchFilter<T extends DomainObject> implements
		IModel<SearchFilter<T>> {
	private static final long serialVersionUID = 1L;

	private List<Boolean> sortAscending;
	private List<String> sortProperties;

	/**
	* 
	*/
	public SearchFilter() {
		sortAscending = new LinkedList<Boolean>();
		sortProperties = new LinkedList<String>();
	}

	/**
	* Gets the order in which the results should be sorted
	* 
	* @return A list of booleans, indicating <code>true</code> if the property
	*         should be in ascending order, or <code>false</code> if it should
	*         be in descending order
	*/
	public List<Boolean> getOrderByDirections() {
		return sortAscending;
	}

	/**
	* Adds a property to order the results by
	* 
	* @param property
	*            A property to sort the results by
	* @param ascending
	*            Whether or not the sort should be ascending or descending
	*/
	public void addOrderBy(String property, boolean ascending) {
		sortAscending.add(ascending);
		sortProperties.add(property);
	}

	/**
	* @return A list of properties to sort results by
	*/
	public List<String> getOrderBy() {
		return sortProperties;
	}

	@Override
	public final SearchFilter<T> getObject() {
		return this;
	}

	@Override
	public final void setObject(SearchFilter<T> object) {
		// No operation
	}

	@Override
	public void detach() {
	}

}
