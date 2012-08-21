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
package com.tysanclan.site.projectewok.components;

import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;

/**
 * Dropdown choice that automatically wraps entities in proper models
 * 
 * @author Jeroen Steenbeeke
 */
public class TysanDropDownChoice<T extends DomainObject>
        extends DropDownChoice<T> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	
	public TysanDropDownChoice(String id, T entity,
	        List<T> choices,
	        IChoiceRenderer<? super T> renderer) {
		super(id, ModelMaker.wrap(entity), ModelMaker
		        .wrap(choices), renderer);
	}

	
	public TysanDropDownChoice(String id, T entity,
	        List<T> choices) {
		super(id, ModelMaker.wrap(entity), ModelMaker
		        .wrap(choices));

	}

}
