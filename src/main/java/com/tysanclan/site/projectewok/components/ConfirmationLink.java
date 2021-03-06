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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 * @author Ties
 */
public class ConfirmationLink<T> extends Link<T> {

	private static final long serialVersionUID = 1L;

	private String confirmMessage;

	public ConfirmationLink(String id, String confirmMessage) {
		super(id);
		this.confirmMessage = confirmMessage;
	}

	public ConfirmationLink(String id, IModel<T> model, String confirmMessage) {
		super(id, model);
		this.confirmMessage = confirmMessage;
	}

	@Override
	protected CharSequence getOnClickScript(CharSequence url) {
		AppendingStringBuffer scriptBuffer = new AppendingStringBuffer();
		scriptBuffer.append("return confirm('");
		scriptBuffer.append(confirmMessage);
		scriptBuffer.append("');");
		return scriptBuffer;
	}

	@Override
	public void onClick() {
	}
}
