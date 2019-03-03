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
package com.tysanclan.site.projectewok.components.renderer;

import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.NaiveRenderer;
import com.tysanclan.site.projectewok.model.GameRealmCartesian;

/**
 * @author Jeroen Steenbeeke
 */
public final class GameRealmCartesianRenderer
		implements NaiveRenderer<GameRealmCartesian> {
	private static final long serialVersionUID = 1L;

	/**
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	@Override
	public Object getDisplayValue(GameRealmCartesian object) {
		return object.getGame().getName() + " on " + object.getRealm()
				.getName();
	}

	/**
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object,
	 *      int)
	 */
	@Override
	public String getIdValue(GameRealmCartesian object, int index) {
		return Integer.toString(index);
	}

}
