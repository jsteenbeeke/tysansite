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
package com.tysanclan.site.projectewok.model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.wicket.model.IModel;

/**
 * @author Jeroen Steenbeeke
 */
public class DollarSignModel implements IModel<String> {
	private static final long serialVersionUID = 1L;

	private IModel<BigDecimal> wrapped;

	/**
     * 
     */
	public DollarSignModel(IModel<BigDecimal> wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@Override
	public String getObject() {
		NumberFormat format = NumberFormat
		        .getCurrencyInstance(Locale.US);
		return format.format(wrapped.getObject()
		        .doubleValue());

	}

	/**
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(String object) {
		wrapped.setObject(new BigDecimal(object));
	}

	/**
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
		wrapped.detach();

	}
}
