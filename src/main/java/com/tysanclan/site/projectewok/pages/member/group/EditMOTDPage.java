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

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.components.TysanTinyMCESettings;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class EditMOTDPage extends AbstractMemberPage {
	/**
     * 
     */
	public EditMOTDPage(Group group) {
		super("Edit message of the day - "
		        + group.getName());

		if (!group.getLeader().equals(getUser())) {
			throw new RestartResponseAtInterceptPageException(
			        new OverviewPage());
		}

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(
		        new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		Form<Group> motdForm = new Form<Group>("motdForm",
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
				TextArea<String> motdArea = (TextArea<String>) get("motd");
				String motd = motdArea.getModelObject();

				groupService.setGroupMOTD(getModelObject(),
				        motd);

				setResponsePage(new OverviewPage());
			}

		};

		motdForm.add(new TextArea<String>("motd",
		        new Model<String>(group
		                .getMessageOfTheDay()))
		        .add(new TinyMceBehavior(
		                new TysanTinyMCESettings())));

		accordion.add(motdForm);

		add(accordion);

	}
}
