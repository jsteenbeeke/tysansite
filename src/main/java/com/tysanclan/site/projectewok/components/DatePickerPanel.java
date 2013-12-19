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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.ui.datepicker.DatePicker;

import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class DatePickerPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private DatePicker<String> datePicker;

	
	public DatePickerPanel(String id, Date date) {
		super(id);
		initComponents(date);
	}

	/**
     * 
     */
	public DatePickerPanel(String id) {
		super(id);
		initComponents(new Date());
	}

	
	private void initComponents(Date date) {
		String modelData;

		if (date == null) {
			modelData = "";
		} else {
			Calendar cal = Calendar.getInstance(
			        DateUtil.NEW_YORK, Locale.US);
			cal.setTime(date);

			SimpleDateFormat format = new SimpleDateFormat(
			        "MM/dd/yy");
			modelData = format.format(cal.getTime());
		}

		datePicker = new DatePicker<String>("date",
		        new Model<String>(modelData));
		datePicker.setDateFormat("mm/dd/yy");

		add(datePicker);
	}

	/**
	 * @see org.apache.wicket.Component#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();
		datePicker.detach();
	}

	public Date getSelectedDate() {
		String input = datePicker.getInput();

		if (input == null || input.isEmpty()) {
			return null;
		}

		try {
			return new SimpleDateFormat("MM/dd/yy")
			        .parse(input);
		} catch (ParseException e) {
			return new Date();
		}
	}

}
