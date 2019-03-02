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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.components.BBCodeTextArea;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class EditGroupDescriptionPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public EditGroupDescriptionPage(Group group) {
		super("Edit group description - " + group.getName());

		if (!group.getLeader().equals(getUser())) {
			throw new RestartResponseAtInterceptPageException(
					new OverviewPage());
		}

		Form<Group> descriptionForm = new Form<Group>("descriptionForm",
				ModelMaker.wrap(group)) {

			private static final long serialVersionUID = 1L;

			@SpringBean
			private GroupService groupService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextArea<String> descriptionArea = (TextArea<String>) get(
						"description");
				String description = descriptionArea.getModelObject();

				groupService.setGroupDescription(getModelObject(), description);

				setResponsePage(new OverviewPage());
			}

		};

		descriptionForm
				.add(new BBCodeTextArea("description", group.getDescription()));

		add(descriptionForm);

	}
}
