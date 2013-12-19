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

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.Rank;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class CreateForumPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public CreateForumPage(ForumCategory category) {
		super("Create Forum");

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "'content'");

		Form<ForumCategory> createForm = new Form<ForumCategory>("createform",
				ModelMaker.wrap(category)) {
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

				CheckBox publicAccessBox = (CheckBox) get("publicaccess");
				CheckBox membersOnlyBox = (CheckBox) get("membersonly");

				String name = nameField.getModelObject();
				String description = descriptionArea.getModelObject();

				boolean error = false;

				if (name == null || name.isEmpty()) {
					error("Name must not be empty!");
					error = true;
				}
				if (description == null || description.isEmpty()) {
					error("Description must not be empty!");
					error = true;
				}

				if (error)
					return;

				Boolean publicAccess = publicAccessBox.getModelObject();
				Boolean membersOnly = membersOnlyBox.getModelObject();

				ForumCategory cat = getModelObject();

				Forum forum = forumService.createForum(name, description,
						publicAccess, cat, getUser());

				if (membersOnly) {
					forumService.setMembersOnly(getUser(), forum, membersOnly);
				}

				setResponsePage(new ForumManagementPage());
			}

		};

		createForm.add(new TextField<String>("name", new Model<String>("")));

		createForm.add(new TextArea<String>("description",
				new Model<String>("")));

		createForm.add(new CheckBox("publicaccess", new Model<Boolean>(false)));
		createForm.add(new CheckBox("membersonly", new Model<Boolean>(false)));

		accordion.add(createForm);

		add(accordion);

		add(new Link<Void>("back") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ForumManagementPage());
			}

		});
	}
}
