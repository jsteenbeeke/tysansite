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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Jeroen Steenbeeke
 */
public class YoutubeVideo extends Panel {
	private static final long serialVersionUID = 1L;

	public YoutubeVideo(String id, String videoURL) {
		super(id);

		add(new WebMarkupContainer("ref").add(AttributeModifier.replace(
				"value", videoURL + "&amp;hl=en_US&amp;fs=1?rel=0")));

		add(new WebMarkupContainer("ref2").add(AttributeModifier.replace("src",
				videoURL + "&amp;hl=en_US&amp;fs=1?rel=0")));
	}
}
