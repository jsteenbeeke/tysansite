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
package com.tysanclan.site.projectewok;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class TysanTopPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public TysanTopPanel(String id) {
		super(id);
	}

	public TysanTopPanel(String id, IModel<?> model) {
		super(id, model);
	}

	public void onAttachComponent() {
		InjectorHolder.getInjector().inject(this);
	}
}
