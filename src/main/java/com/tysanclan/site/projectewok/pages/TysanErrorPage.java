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
package com.tysanclan.site.projectewok.pages;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.tardis.scheduler.wicket.HyperionScheduler;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.tasks.ErrorReportTask;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Jeroen Steenbeeke
 */
public class TysanErrorPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(TysanErrorPage.class);


	public TysanErrorPage() {
		super("DAFUQ!");
		throw new IllegalStateException(
				"WHY THE FUCK ARE YOU TRYING TO CALL THIS, WICKET?!?!");
	}

	public TysanErrorPage(@Nullable String target, @Nullable String referrer,
						  @Nonnull final Exception exception) {
		super("An error has occurred");

		final ErrorReportTask.ErrorData errorData = new ErrorReportTask.ErrorData(exception, target, referrer);

		HyperionScheduler.getScheduler().scheduleTask(DateTime.now().plusMinutes(5), new ErrorReportTask(errorData));


		final TextArea<String> reportArea = new TextArea<String>("report",
																 new Model<>(""));
		reportArea.setRequired(true);

		Form<Bug> reportForm = new Form<Bug>("bugreport") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				errorData.getCustomData().put("userReport", reportArea.getModelObject());

				setResponsePage(NewsPage.class);
			}

		};

		reportForm.add(reportArea);

		add(reportForm);

	}


}
