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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;

public abstract class CreateBugPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public CreateBugPanel(String id, ReportType type) {
		super(id);

		add(new Label("title", "Create " + type.getDescription()));

		final TextField<String> titleField = new TextField<String>(
				"titleField", new Model<String>(""));
		titleField.setRequired(true);

		final TextArea<String> descriptionArea = new TextArea<String>(
				"descriptionArea", new Model<String>(""));
		descriptionArea.setRequired(true);

		Form<Bug> form = new Form<Bug>("form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				CreateBugPanel.this.onFormSubmit(titleField.getModelObject(),
						descriptionArea.getModelObject());
			}
		};

		form.add(titleField);
		form.add(descriptionArea);

		add(form);
	}

	public abstract void onFormSubmit(String title, String description);
}
