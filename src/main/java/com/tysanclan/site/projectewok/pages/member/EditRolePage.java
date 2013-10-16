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
package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.TysanTinyMCESettings;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Role;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class EditRolePage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public EditRolePage(Role role) {
		super("Edit role - " + role.getName());

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "content");

		accordion.add(new Label("title", role.getName()));

		Form<Role> editForm = new Form<Role>("editForm", ModelMaker.wrap(role)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private RoleService roleService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextField<String> nameField = (TextField<String>) get("name");
				TextArea<String> descriptionArea = (TextArea<String>) get("description");

				String name = nameField.getModelObject();
				String description = descriptionArea.getModelObject();

				Role r = getModelObject();
				if (!r.getName().equals(name)) {
					roleService.setRoleName(getUser(), r, name);
				}
				if (!r.getDescription().equals(description)) {
					roleService.setRoleDescription(getUser(), r, description);
				}

				setResponsePage(new RolesManagementPage());
			}

		};

		editForm.add(new TextField<String>("name", new Model<String>(role
				.getName())));
		editForm.add(new TextArea<String>("description", new Model<String>(role
				.getDescription())).add(new TinyMceBehavior(
				new TysanTinyMCESettings())));

		accordion.add(editForm);

		add(accordion);

		add(new Link<Void>("back") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new RolesManagementPage());
			}
		});
	}
}
