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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.NewsForum;
import com.tysanclan.site.projectewok.entities.Rank;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class EditForumPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public EditForumPage(Forum forum) {
		super("Edit forum");

		add(new Label("title", "Edit forum " + forum.getName()));

		Form<Forum> editForm = new Form<Forum>("editform",
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

				CheckBox interactiveBox = (CheckBox) get("interactive");
				CheckBox publicAccessBox = (CheckBox) get("publicaccess");
				CheckBox membersOnlyBox = (CheckBox) get("membersonly");

				String name = nameField.getModelObject();
				String description = descriptionArea.getModelObject();

				Boolean interactive = interactiveBox.getModelObject();
				Boolean publicAccess = publicAccessBox.getModelObject();
				Boolean membersOnly = membersOnlyBox.getModelObject();

				Forum f = getModelObject();

				if (f.getName() == null || !f.getName().equals(name)) {
					forumService.setForumName(f, name, getUser());
				}
				if (f.getDescription() == null
						|| !f.getDescription().equals(description)) {
					forumService.setForumDescription(f, description, getUser());
				}
				if (!Boolean.valueOf(f.isInteractive()).equals(interactive)) {
					forumService.setInteractive(f, interactive, getUser());
				}
				if (!Boolean.valueOf(f.isPublicAccess()).equals(publicAccess)) {
					forumService.setModeratorOnlyRestriction(getUser(), f,
							publicAccess);
				}
				if (!Boolean.valueOf(f.isMembersOnly()).equals(membersOnly)) {
					forumService.setMembersOnly(getUser(), f, membersOnly);
				}

				setResponsePage(new ForumManagementPage());
			}

		};

		editForm.add(new TextField<String>("name", new Model<String>(forum
				.getName())));

		editForm.add(new TextArea<String>("description", new Model<String>(
				forum.getDescription())));

		editForm.add(new CheckBox("interactive", new Model<Boolean>(forum
				.isInteractive())).setEnabled(false));
		editForm.add(new CheckBox("publicaccess", new Model<Boolean>(!forum
				.isPublicAccess())).setEnabled(!(forum instanceof NewsForum)));
		editForm.add(new CheckBox("membersonly", new Model<Boolean>(forum
				.isMembersOnly())).setEnabled(!(forum instanceof NewsForum)));

		add(editForm);

		add(new Link<Void>("back") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ForumManagementPage());
			}

		});
	}
}
