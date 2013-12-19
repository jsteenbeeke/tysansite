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
