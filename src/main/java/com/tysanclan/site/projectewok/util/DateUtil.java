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
package com.tysanclan.site.projectewok.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.NaiveRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class DateUtil {
	public static final TimeZone NEW_YORK = TimeZone
			.getTimeZone("America/New_York");

	/**
	 * Returns the date, formatted along the user's timezone preferences, using
	 * EST as a basis when in doubt
	 * 
	 * @param date
	 *            The date to format
	 * @param user
	 *            The user to base the preferences on
	 * @return A String formatted date according to the user's preferences
	 */
	public static String getUserTime(Date date, User user) {
		if (user == null || user.getTimezone() == null) {
			return getESTFormattedString(date);
		}

		return getTimezoneFormattedString(date, user.getTimezone());

	}

	public static String getESTFormattedString(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		StringBuilder builder = createDateFormatString(cal);

		return getESTFormattedString(date, builder.toString());
	}

	public static StringBuilder createDateFormatString(Calendar cal) {
		String postfix = getDayOfMonthPostfix(cal);

		StringBuilder builder = new StringBuilder();
		builder.append("d'").append(postfix)
				.append(" of' MMMM yyyy '@' HH:mm zzz");
		return builder;
	}

	public static String getTimezoneFormattedString(Date date, String timezone) {
		TimeZone tz = null;
		if (timezone == null) {
			tz = NEW_YORK;
		} else {
			tz = TimeZone.getTimeZone(timezone);
		}

		Calendar cal = Calendar.getInstance(tz, Locale.US);
		cal.setTime(date);
		StringBuilder builder = createDateFormatString(cal);

		SimpleDateFormat sdf = new SimpleDateFormat(builder.toString(),
				Locale.US);
		sdf.setTimeZone(tz);
		return sdf.format(date);
	}

	public static Calendar getCalendarInstance() {
		return Calendar.getInstance(NEW_YORK, Locale.US);
	}

	public static Calendar getMidnightCalendarInstance() {
		Calendar cal = Calendar.getInstance(NEW_YORK, Locale.US);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal;
	}

	public static Calendar getMidnightCalendarByUnadjustedDate(Date date,
			TimeZone timezone) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		Calendar mcal = getMidnightCalendarInstance(timezone);
		mcal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		mcal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		mcal.set(Calendar.YEAR, cal.get(Calendar.YEAR));

		return mcal;
	}

	public static Calendar getMidnightCalendarInstance(TimeZone timezone) {
		Calendar cal = Calendar.getInstance(timezone, Locale.US);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal;
	}

	public static String getESTFormattedString(Date date, String formatString) {

		SimpleDateFormat sdf = new SimpleDateFormat(formatString, Locale.US);
		TimeZone tz = NEW_YORK;
		sdf.setTimeZone(tz);
		return sdf.format(date);
	}

	private static String getDayOfMonthPostfix(Calendar cal) {
		String postfix = "th";

		switch (cal.get(Calendar.DAY_OF_MONTH)) {
			case 1:
			case 21:
			case 31:
				postfix = "st";
				break;
			case 2:
			case 22:
				postfix = "nd";
				break;
			case 3:
			case 23:
				postfix = "rd";
				break;
		}
		return postfix;
	}

	public static Date getMidnightDate(Date date) {
		Calendar cal = Calendar.getInstance(NEW_YORK, Locale.US);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static List<Integer> getMinutes() {
		List<Integer> result = new LinkedList<Integer>();

		for (int i = 0; i < 60; i++) {
			result.add(i);
		}

		return result;
	}

	public static List<Integer> getHours() {
		List<Integer> result = new LinkedList<Integer>();

		for (int i = 0; i < 24; i++) {
			result.add(i);
		}

		return result;
	}

	public static IChoiceRenderer<Integer> getTwoDigitRenderer() {
		return new NaiveRenderer<Integer>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Integer object) {
				String retval = object.toString();
				if (object < 10) {
					retval = "0" + retval;
				}

				return retval;
			}

			@Override
			public String getIdValue(Integer object, int index) {
				return object.toString();
			}
		};
	}

	public static int calculateAge(Date birthDate) {
		int age = -1;

		Calendar cal = DateUtil.getCalendarInstance();

		if (!cal.getTime().before(birthDate)) {
			while (cal.getTime().after(birthDate)) {
				age++;
				cal.add(Calendar.YEAR, -1);
			}
		}
		return age;
	}

	public static final Date tysanFoundation = new Date(883690914223L);

	public static final Date prosperoResigns = new Date(1309669200000L);

	public static Date daysAgo(int i) {
		Calendar cal = getCalendarInstance();

		cal.add(Calendar.DAY_OF_MONTH, -i);

		return cal.getTime();
	}

	public static Date monthsAgo(int i) {
		Calendar cal = getCalendarInstance();

		cal.add(Calendar.MONTH, -i);

		return cal.getTime();
	}

	public static Date yearsAgo(int i) {
		Calendar cal = getCalendarInstance();

		cal.add(Calendar.YEAR, -i);

		return cal.getTime();
	}
	
	  public static int daysBetween(Date d1, Date d2){
          return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
  }
}
