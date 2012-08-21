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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class CalendarPanelNavigator extends Panel {
	private static final long serialVersionUID = 1L;

	private CalendarPanel panel;

	private Date selectedDate;

	public CalendarPanelNavigator(String id,
	        CalendarPanel panel) {
		super(id);
		this.panel = panel;

		selectedDate = DateUtil.getMidnightDate(panel
		        .getCalendar().getTime());

		add(createMonthLabel(selectedDate));

		add(new AjaxLink<Date>("previous") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar cal = DateUtil
				        .getCalendarInstance();
				cal.setTime(getSelectedDate());
				cal.add(Calendar.MONTH, -1);
				replacePanel(target, cal.getTime());
			}

		});

		add(new AjaxLink<Date>("next") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Calendar cal = DateUtil
				        .getCalendarInstance();
				cal.setTime(getSelectedDate());
				cal.add(Calendar.MONTH, 1);
				replacePanel(target, cal.getTime());
			}

		});

	}

	/**
	 * @return the selectedDate
	 */
	public Date getSelectedDate() {
		return selectedDate;
	}

	public Label createMonthLabel(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(
		        "MMMM yyyy", Locale.US);
		format.setTimeZone(DateUtil.NEW_YORK);

		Label label = new Label("month", format
		        .format(date));
		label.setOutputMarkupId(true);
		label.setOutputMarkupPlaceholderTag(true);

		return label;
	}

	private void replacePanel(AjaxRequestTarget target,
	        Date newDate) {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.setTime(newDate);
		CalendarPanel newPanel = createNewPanel(panel
		        .getId(), cal);
		panel.replaceWith(newPanel);
		panel = newPanel;

		Component oldLabel = get("month");
		Label label = createMonthLabel(newDate);
		oldLabel.replaceWith(label);

		if (target != null) {
			target.addComponent(panel);
			target.addComponent(label);
			selectedDate = newDate;
		}
	}

	public abstract CalendarPanel createNewPanel(String id,
	        Calendar calendar);

}
