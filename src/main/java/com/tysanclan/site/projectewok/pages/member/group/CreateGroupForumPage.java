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
package com.tysanclan.site.projectewok.pages.member.group;

import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.dao.ForumCategoryDAO;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;

/**
 * @author Jeroen Steenbeeke
 */
public class CreateGroupForumPage extends TysanPage {

	@SpringBean
	private ForumCategoryDAO forumCategoryDAO;

	private IModel<ForumCategory> categoryModel;

	/**
     * 
     */
	public CreateGroupForumPage(Group group) {
		super("Add forum for group " + group.getName());

		if (!group.getLeader().equals(getUser())) {
			throw new RestartResponseAtInterceptPageException(
			        AccessDeniedPage.class);
		}

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(
		        new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		initCategoryModel(group);

		Form<Group> createForm = new Form<Group>(
		        "createform", ModelMaker.wrap(group)) {
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
				String description = descriptionArea
				        .getModelObject();

				boolean error = false;

				if (name == null || name.isEmpty()) {
					error("Name must not be empty!");
					error = true;
				}
				if (description == null
				        || description.isEmpty()) {
					error("Description must not be empty!");
					error = true;
				}

				if (error)
					return;

				ForumCategory category = getForumCategory();
				Group gr = getModelObject();

				forumService.createGroupForum(name,
				        description, category, gr);

				setResponsePage(new GroupForumManagementPage(
				        gr));
			}

		};

		createForm.add(new TextField<String>("name",
		        new Model<String>("")));
		createForm.add(new TextArea<String>("description",
		        new Model<String>("")));

		accordion.add(createForm);

		add(accordion);

		addBackLink(group);
	}

	
	private void addBackLink(Group group) {
		add(new Link<Group>("back", ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new GroupForumManagementPage(
				        getModelObject()));

			}
		});
	}

	
	private void initCategoryModel(Group group) {
		List<ForumCategory> categories = forumCategoryDAO
		        .findAll();
		ForumCategory category = null;

		for (ForumCategory next : categories) {
			if (next.isAllowPublicGroupForums()) {
				category = next;
			}
		}

		if (category == null) {
			throw new RestartResponseAtInterceptPageException(
			        new GroupForumManagementPage(group));
		}

		categoryModel = ModelMaker.wrap(category);
	}

	public ForumCategory getForumCategory() {
		return categoryModel.getObject();
	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		categoryModel.detach();
	}
}
