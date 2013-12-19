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
package com.tysanclan.site.projectewok.pages.member.admin;

import java.util.Arrays;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.RangeValidator;

import com.tysanclan.site.projectewok.SiteWideNotification;
import com.tysanclan.site.projectewok.SiteWideNotification.Category;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
public class SiteWideNotificationPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	private static enum DurationTypes {
		MINUTES("minutes") {
			@Override
			public Duration getDuration(int amount) {
				return Duration.minutes(amount);
			}
		},
		HOURS("hours") {
			@Override
			public Duration getDuration(int amount) {
				return Duration.hours(amount);
			}
		},
		DAYS("days") {
			@Override
			public Duration getDuration(int amount) {
				return Duration.days(amount);
			}
		};

		private final String descriptor;

		DurationTypes(String descriptor) {
			this.descriptor = descriptor;
		}

		/**
		 * @return the descriptor
		 */
		public String getDescriptor() {
			return descriptor;
		}

		public abstract Duration getDuration(int amount);
	}

	public SiteWideNotificationPage(User user) {
		super("Site Wide Notification");

		if (!getUser().equals(roleService.getSteward())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		final TextField<String> messageField = new TextField<String>("message",
				new Model<String>(""));
		messageField.setRequired(true);

		final DropDownChoice<Category> categorySelect = new DropDownChoice<Category>(
				"category", new Model<Category>(Category.INFO),
				Arrays.asList(Category.values()));
		categorySelect.setNullValid(false);
		categorySelect.setRequired(true);

		String[] durations = new String[DurationTypes.values().length];
		int i = 0;

		for (DurationTypes t : DurationTypes.values()) {
			durations[i++] = t.getDescriptor();
		}

		final DropDownChoice<String> durationTypeSelect = new DropDownChoice<String>(
				"durationType", new Model<String>("minutes"),
				Arrays.asList(durations));
		durationTypeSelect.setNullValid(false);
		durationTypeSelect.setRequired(true);

		final TextField<Integer> durationField = new TextField<Integer>(
				"duration", new Model<Integer>(1), Integer.class);
		durationField.add(RangeValidator.minimum(1));
		durationField.setRequired(true);

		Form<SiteWideNotification> notificationForm = new Form<SiteWideNotification>(
				"notificationForm") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				String message = messageField.getModelObject();
				Category category = categorySelect.getModelObject();
				String durationType = durationTypeSelect.getModelObject();
				Integer durationAmount = durationField.getModelObject();

				Duration d = Duration.minutes(1);
				for (DurationTypes t : DurationTypes.values()) {
					if (t.getDescriptor().equals(durationType)) {
						d = t.getDuration(durationAmount);
					}
				}

				SiteWideNotification not = new SiteWideNotification(category,
						message, d);

				TysanApplication.get().notify(not);

				setResponsePage(new OverviewPage());

			}

		};

		notificationForm.add(messageField);
		notificationForm.add(categorySelect);
		notificationForm.add(durationTypeSelect);
		notificationForm.add(durationField);

		add(notificationForm);
	}
}
