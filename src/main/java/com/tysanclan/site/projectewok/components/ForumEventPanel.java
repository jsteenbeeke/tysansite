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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.tysanclan.site.projectewok.entities.Event;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumEventPanel extends Panel {
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
	public ForumEventPanel(String id, Event event, User user) {
		super(id);

		add(new ContextImage("icon", new Model<String>(
		        "images/icons/calendar.png")));
		add(new Label("time", DateUtil.getUserTime(event
		        .getDate(), user)));
	}
}
