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

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class DateTimeLabel extends Panel {
	private static final long serialVersionUID = 1L;

	public DateTimeLabel(String id, Date date) {
		super(id);

		TysanSession session = (TysanSession) TysanSession
		        .get();
		User user = (session != null) ? session.getUser()
		        : null;

		String timezone = user != null ? user.getTimezone()
		        : null;

		if (timezone != null) {
			add(new Label("label", DateUtil
			        .getTimezoneFormattedString(date,
			                timezone)));
		} else {
			add(new Label("label", DateUtil
			        .getESTFormattedString(date)));
		}
	}
}
