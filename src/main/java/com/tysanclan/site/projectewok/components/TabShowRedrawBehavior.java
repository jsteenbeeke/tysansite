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

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.odlabs.wiquery.ui.tabs.Tabs;

/**
 * @author Jeroen Steenbeeke
 */
public class TabShowRedrawBehavior extends Behavior {
	private static final long serialVersionUID = 1L;

	private Tabs tabs;

	private int page;

	private Component component;

	public TabShowRedrawBehavior(Tabs tabs, int page, Component component) {
		super();
		this.tabs = tabs;
		this.page = page;
		this.component = component;
	}

	public Tabs getTabs() {
		return tabs;
	}

	public int getPage() {
		return page;
	}

	public Component getComponent() {
		return component;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);

		StringBuilder javascript = new StringBuilder();
		javascript.append("$('#" + getTabs().getMarkupId()
				+ "').bind(\"tabsshow\", function(event, ui) {\n");

		javascript.append("\tif (ui.index == " + getPage() + ") {\n");
		javascript.append("\t\tif ($('#" + getComponent().getMarkupId()
				+ "')._drawCount == 0) {\n");
		javascript.append("\t\t\t$('#" + getComponent().getMarkupId()
				+ "').redraw();\n");
		javascript.append("\t\t}\n");
		javascript.append("\t});\n");
		javascript.append("});\n");
		response.renderJavaScript(javascript.toString(), null);

	}

}
