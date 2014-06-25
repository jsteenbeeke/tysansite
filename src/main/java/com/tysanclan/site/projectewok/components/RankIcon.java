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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.pages.PageStyle;

/**
 * @author Jeroen Steenbeeke
 */
public class RankIcon extends Panel {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public RankIcon(String id, Rank rank) {
		super(id);

		PageStyle style = PageStyle.getCurrentStyle(getStyle());

		String imageLocation = style.getRankImage(rank);

		ContextImage img = new ContextImage("icon", new Model<String>(
				imageLocation));
		img.add(new AttributeModifier("title", new Model<String>(rank
				.toString())));
		img.add(new AttributeModifier("alt", new Model<String>(rank.toString())));
		add(img);
	}
}
