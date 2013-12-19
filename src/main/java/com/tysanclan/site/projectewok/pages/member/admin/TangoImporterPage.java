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

import java.io.IOException;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.imports.tango.TangoImportTask;
import com.tysanclan.site.projectewok.util.scheduler.TysanScheduler;

/**
 * @author Jeroen Steenbeeke
 */
public class TangoImporterPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	/**
	 * 
	 */
	public TangoImporterPage() {
		super("Tango Conversion Page");

		Form<?> uploadForm = new Form<Void>("uploadForm") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				PasswordTextField keyField = (PasswordTextField) get("masterKey");

				if (userDAO.findAll().isEmpty()) {

					if (keyField.getModelObject().equals(
							TysanApplication.MASTER_KEY)) {
						FileUploadField field = (FileUploadField) get("fileUploader");
						FileUpload upload = field.getFileUpload();

						if (upload == null) {
							error("You must upload a file");
							return;
						}

						try {
							TysanScheduler.getScheduler()
									.scheduleTask(
											new TangoImportTask(upload
													.getInputStream()));
							info("Tango import background job started");
						} catch (IOException e) {
							error("Internal error: Unable to read upload");
						}

					} else {
						error("Master key invalid");
					}
				} else {
					error("Import can only be run on an empty database!");
				}

			}

		};
		uploadForm
				.add(new PasswordTextField("masterKey", new Model<String>("")));
		uploadForm.add(new FileUploadField("fileUploader",
				new ListModel<FileUpload>()));
		add(uploadForm);

	}
}
