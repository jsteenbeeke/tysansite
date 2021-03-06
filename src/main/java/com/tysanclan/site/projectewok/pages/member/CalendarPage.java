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
package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.EventService;
import com.tysanclan.site.projectewok.components.*;
import com.tysanclan.site.projectewok.entities.Event;
import com.tysanclan.site.projectewok.entities.dao.EventDAO;
import com.tysanclan.site.projectewok.entities.filter.EventFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class CalendarPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	private static final String EVENT_VIEW_ID = "today";
	@SpringBean
	private EventDAO eventDAO;

	/**
	 *
	 */
	public CalendarPage() {
		super("Calendar");

		add(new OtterSniperPanel("otterSniperPanel", 2));

		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);

		Date currTime = DateUtil.getMidnightCalendarInstance().getTime();

		InlineDatePicker calendar = new InlineDatePicker("calendar", currTime) {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onDateSelected(Date date, AjaxRequestTarget target) {
				TimeZone tz = DateUtil.NEW_YORK;

				if (getUser().getTimezone() != null) {
					tz = TimeZone.getTimeZone(getUser().getTimezone());
				}

				Calendar cal = Calendar.getInstance(tz);
				cal.setTime(date);

				Component oldEventView = getEventViewComponent();
				Component oldTitle = getTitleComponent();

				ListView<Event> eventView = createListView(cal.getTime());

				getContainer()
						.setVisible(!eventView.getModelObject().isEmpty());

				Label newTitle = new Label("title",
						"Events for " + new SimpleDateFormat("EEEE d MMMM yyyy",
								Locale.US).format(date));

				oldEventView.replaceWith(eventView);
				oldTitle.replaceWith(newTitle);

				Component component = get("schedule:description")
						.setVisible(true);

				if (target != null) {
					target.appendJavaScript(
							"tinyMCE.execCommand('mceRemoveControl', false, '"
									+ component.getMarkupId() + "')");
					target.add(getContainer());

					target.appendJavaScript(
							"tinyMCE.execCommand('mceAddControl', false, '"
									+ component.getMarkupId() + "')");
				}
			}

		};

		container.add(calendar);

		add(container);

		ListView<Event> eventView = createListView(currTime);
		container.add(eventView);
		container.setVisible(!eventView.getModelObject().isEmpty());
		container.add(new Label("title",
				"Events for " + new SimpleDateFormat("EEEE d MMMM yyyy",
						Locale.US).format(currTime)));

		Form<Event> scheduleEventForm = new Form<Event>("schedule") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private EventService eventService;

			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> titleComponent = (TextField<String>) get(
						"title");
				TextArea<String> descriptionComponent = (TextArea<String>) get(
						"description");
				DatePickerPanel dateComponent = (DatePickerPanel) get(
						"dateselect");
				DropDownChoice<Integer> hourComponent = (DropDownChoice<Integer>) get(
						"hourselect");
				DropDownChoice<Integer> minuteComponent = (DropDownChoice<Integer>) get(
						"minuteselect");

				String title = titleComponent.getModelObject();
				String description = descriptionComponent.getModelObject();
				Date selectedDate = dateComponent.getSelectedDate();

				if (selectedDate == null) {
					error("No date selected");
					return;
				}

				TimeZone tz = DateUtil.NEW_YORK;

				if (getUser().getTimezone() != null) {
					tz = TimeZone.getTimeZone(getUser().getTimezone());
				}

				Calendar cal = Calendar.getInstance(tz);
				cal.setTime(DateUtil.getMidnightDate(selectedDate));
				cal.add(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.HOUR_OF_DAY, hourComponent.getModelObject());
				cal.add(Calendar.MINUTE, minuteComponent.getModelObject());

				Event event = eventService
						.scheduleEvent(getUser(), cal.getTime(), title,
								description);
				if (event != null) {
					setResponsePage(new CalendarPage());
				}
			}

		};

		scheduleEventForm
				.add(new TextField<String>("title", new Model<String>(""))
						.setRequired(true));
		TextArea<String> descriptionEditor = new BBCodeTextArea("description",
				"");
		descriptionEditor.setRequired(true);

		scheduleEventForm.add(descriptionEditor);

		DatePickerPanel datePanel = new DatePickerPanel("dateselect");

		scheduleEventForm.add(datePanel);

		scheduleEventForm.add(new DropDownChoice<Integer>("hourselect",
				new Model<Integer>(0), DateUtil.getHours(),
				DateUtil.getTwoDigitRenderer()).setNullValid(false)
				.setRequired(true));

		scheduleEventForm.add(new DropDownChoice<Integer>("minuteselect",
				new Model<Integer>(0), DateUtil.getMinutes(),
				DateUtil.getTwoDigitRenderer()).setNullValid(false)
				.setRequired(true));

		TimeZone tz = DateUtil.NEW_YORK;

		if (getUser().getTimezone() != null) {
			tz = TimeZone.getTimeZone(getUser().getTimezone());
		}

		scheduleEventForm.add(new Label("timezone",
				tz.getDisplayName(false, TimeZone.LONG, Locale.US)));

		add(scheduleEventForm);
	}

	public ListView<Event> createListView(Date date) {

		EventFilter filter = new EventFilter();
		Calendar midnightCalendarInstance = DateUtil
				.getMidnightCalendarInstance();
		Date startOfDay = midnightCalendarInstance.getTime();
		midnightCalendarInstance.add(Calendar.DAY_OF_YEAR, 1);
		midnightCalendarInstance.add(Calendar.SECOND, 1);
		filter.date().between(startOfDay, midnightCalendarInstance.getTime());

		IModel<List<Event>> events = ModelMaker
				.wrap(eventDAO.findByFilter(filter).toJavaList());

		return new ListView<Event>(EVENT_VIEW_ID, events) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Event> item) {
				Event event = item.getModelObject();

				String name = event.getEventThread().getTitle();

				if (name.startsWith("Event: ")) {
					name = name.substring(7);
				}

				item.add(new AutoThreadLink("event",
						item.getModelObject().getEventThread(), name));

			}

		};
	}

	Component getContainer() {
		return get("container");
	}

	Component getTitleComponent() {
		return get("container:title");
	}

	Component getEventViewComponent() {
		return get("container:" + EVENT_VIEW_ID);
	}
}
