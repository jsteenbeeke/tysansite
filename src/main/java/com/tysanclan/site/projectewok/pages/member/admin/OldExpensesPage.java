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
package com.tysanclan.site.projectewok.pages.member.admin;

import com.jeroensteenbeeke.hyperion.tardis.scheduler.wicket.HyperionScheduler;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.tasks.CreatePaidExpensesTask;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.Model;
import org.joda.time.DateTime;

public class OldExpensesPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public OldExpensesPage() {

		super("Expense Transformer Page");

		Form<?> uploadForm = new Form<Void>("uploadForm") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				PasswordTextField keyField = (PasswordTextField) get(
						"masterKey");

				if (keyField.getModelObject()
						.equals(TysanApplication.MASTER_KEY)) {

					HyperionScheduler.getScheduler()
							.scheduleTask(DateTime.now(),
									new CreatePaidExpensesTask());
					info("Expense transformation background job started");

				} else {
					error("Master key invalid");
				}

			}

		};
		uploadForm
				.add(new PasswordTextField("masterKey", new Model<String>("")));
		add(uploadForm);
	}
}
