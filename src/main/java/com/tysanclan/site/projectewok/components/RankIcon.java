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

		StringBuilder rankStr = new StringBuilder();
		String lowerCaseRank = rank.toString().toLowerCase();

		for (int i = 0; i < lowerCaseRank.length(); i++) {
			if (!Character.isWhitespace(lowerCaseRank.charAt(i))) {
				rankStr.append(lowerCaseRank.charAt(i));
			} else {
				rankStr.append("_");
			}
		}

		String imageLocation = "images/ranks/" + rankStr.toString() + ".gif";

		if (rank == Rank.FORUM || rank == Rank.BANNED) {
			imageLocation = "images/icons/script.png";
		}
		if (rank == Rank.HERO) {
			imageLocation = "images/icons/rosette.png";
		}

		ContextImage img = new ContextImage("icon", new Model<String>(
				imageLocation));
		img.add(new AttributeModifier("title", new Model<String>(rank
				.toString())));
		img.add(new AttributeModifier("alt", new Model<String>(rank.toString())));
		add(img);
	}
}
