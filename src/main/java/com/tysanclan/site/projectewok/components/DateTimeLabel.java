/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.components;

import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
public class DateTimeLabel extends Panel {
	private static final long serialVersionUID = 1L;

	public DateTimeLabel(String id, Date date) {
		super(id);

		String timezone = TysanSession.session().flatMap(TysanSession::getUser)
				.map(User::getTimezone).getOrNull();

		if (timezone != null) {
			add(new Label("label",
					DateUtil.getTimezoneFormattedString(date, timezone)));
		} else {
			add(new Label("label", DateUtil.getESTFormattedString(date)));
		}
	}
}
