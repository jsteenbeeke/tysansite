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
package com.tysanclan.site.projectewok.pages.member.group;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.GroupForum;

/**
 * @author Jeroen Steenbeeke
 */
public class EditGroupForumPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public EditGroupForumPage(GroupForum forum) {
		super("Edit group forum - " + forum.getName());

		add(new Label("title", "Edit forum " + forum.getName()));

		Form<GroupForum> editForm = new Form<GroupForum>("editform",
				ModelMaker.wrap(forum)) {

			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumService forumService;

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

				GroupForum gf = getModelObject();

				if (!gf.getName().equals(name)) {
					forumService.setForumName(gf, name, getUser());
				}
				if (!gf.getDescription().equals(description)) {
					forumService
							.setForumDescription(gf, description, getUser());
				}

				setResponsePage(new GroupForumManagementPage(gf.getGroup()));
			}

		};

		editForm.add(new TextField<String>("name", new Model<String>(forum
				.getName())));

		editForm.add(new TextArea<String>("description", new Model<String>(
				forum.getDescription())));

		add(editForm);

		add(new Link<Group>("back", ModelMaker.wrap(forum.getGroup())) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new GroupForumManagementPage(getModelObject()));
			}

		});
	}
}
