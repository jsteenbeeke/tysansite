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

import com.googlecode.wicket.jquery.core.Options;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.DateUtil;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.wicketstuff.wiquery.core.javascript.JsQuery;
import org.wicketstuff.wiquery.core.javascript.JsScopeContext;
import org.wicketstuff.wiquery.core.javascript.JsStatement;
import org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference;
import org.wicketstuff.wiquery.ui.datepicker.scope.JsScopeUiDatePickerDateTextEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * WiQuery calendar on a div tag, displaying a calendar inline
 *
 * @author Jeroen Steenbeeke
 */
public abstract class InlineDatePicker extends WebMarkupContainer {
	private static final long serialVersionUID = 1L;

	private Options options;

	private final Date selectedDate;

	public InlineDatePicker(String id, Date date) {
		super(id);

		selectedDate = date == null ? generateDefaultDate() : date;

		DateSelectAjaxBehavior behavior = new DateSelectAjaxBehavior();
		add(behavior);

		options = new Options();
		options.set("onSelect", behavior.getInnerEvent());
		options.set("dateFormat", "'mm/dd/yy'");
		options.set("defaultDate",
					new SimpleDateFormat("MM/dd/yy").format(selectedDate));
	}

	/**
	 */
	private Date generateDefaultDate() {
		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.YEAR, -13);
		int year = cal.get(Calendar.YEAR);

		cal.clear();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal.getTime();
	}

	public InlineDatePicker setChangeMonth(boolean value) {
		options.set("changeMonth", value);
		return this;
	}

	public InlineDatePicker setChangeYear(boolean value) {
		options.set("changeYear", value);
		return this;
	}

	public InlineDatePicker setYearRange(String range) {
		options.set("yearRange", range);
		return this;
	}

	@Override
	public void renderHead(IHeaderResponse response) {

		super.renderHead(response);

		response.render(JavaScriptHeaderItem
								.forReference(JQueryUIJavaScriptResourceReference.get()));

		JsStatement statement = new JsStatement();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(selectedDate);
		statement
				.append("var bDate")
				.append(getMarkupId())
				.append(" = new Date(); bDate")
				.append(getMarkupId())
				.append(".setFullYear(" + calendar.get(Calendar.YEAR) + ", "
								+ calendar.get(Calendar.MONTH) + ", "
								+ calendar.get(Calendar.DAY_OF_MONTH) + ");");

		options.set("defaultDate", String.format("bDate%s", getMarkupId()));

		JsStatement statement2 = new JsQuery(this).$().chain("datepicker",
															 options.toString());

		statement.append(statement2.render());

		response.render(OnDomReadyHeaderItem.forScript(statement.render()));
	}

	protected abstract void onDateSelected(Date date, AjaxRequestTarget target);

	private class DateSelectAjaxBehavior extends AbstractDefaultAjaxBehavior {
		private static final long serialVersionUID = 1L;

		private OnSelectEvent innerEvent;

		public DateSelectAjaxBehavior() {
			innerEvent = new OnSelectEvent();
		}

		/**
		 * @return the innerEvent
		 */
		public OnSelectEvent getInnerEvent() {
			return innerEvent;
		}

		/**
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		@Override
		protected void respond(AjaxRequestTarget target) {
			String date = this.getComponent().getRequest().getQueryParameters()
							  .getParameterValue("date").toString();

			TimeZone tz = DateUtil.NEW_YORK;
			User user = TysanSession.session().flatMap(TysanSession::getUser).getOrNull();
			if (user != null && user.getTimezone() != null) {
				tz = TimeZone.getTimeZone(user.getTimezone());
			}

			Calendar cal = user != null ? DateUtil
					.getMidnightCalendarByUnadjustedDate(new Date(), tz)
					: DateUtil.getCalendarInstance();

			if (!date.matches("\\d{2}\\/\\d{2}\\/\\d{4}")
					&& !date.matches("\\d{2}-\\d{2}-\\d{4}")) {
				error("Invalid date selected");
				return;
			}

			String monthStr = date.substring(0, 2);
			String dayStr = date.substring(3, 5);
			String yearStr = date.substring(6, 10);

			int month = Integer.parseInt(monthStr);
			int day = Integer.parseInt(dayStr);
			int year = Integer.parseInt(yearStr);

			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.YEAR, year);

			Date ddate = cal.getTime();

			InlineDatePicker.this.onDateSelected(ddate, target);
		}

		private class OnSelectEvent extends JsScopeUiDatePickerDateTextEvent {
			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(JsScopeContext scopeContext) {
				JsStatement stat = new JsStatement().$(InlineDatePicker.this)
													.chain("val");

				String val = stat.render().toString();
				if (val.endsWith(";")) {
					val = val.substring(0, val.length() - 1);
				}

				scopeContext.append("wicketAjaxGet('" + getCallbackUrl()
											+ "&date='+" + val
											+ ", null,null, function() {return true;})");

			}

		}

	}

}
