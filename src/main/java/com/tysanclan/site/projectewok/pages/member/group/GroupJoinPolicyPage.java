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

import java.util.Arrays;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.entities.Committee;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Group.JoinPolicy;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class GroupJoinPolicyPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public GroupJoinPolicyPage(Group group) {
		super("Join policy for " + group.getName());

		if (group instanceof Committee || !group.getLeader().equals(getUser())) {
			throw new RestartResponseAtInterceptPageException(
					new AccessDeniedPage());
		}

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "'content'");

		Form<Group> setPolicyForm = new Form<Group>("setpolicy",
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
				Group gr = getModelObject();
				DropDownChoice<JoinPolicy> policyChoice = (DropDownChoice<JoinPolicy>) get("policy");
				JoinPolicy joinPolicy = policyChoice.getModelObject();

				groupService.setJoinPolicy(gr, joinPolicy);

				setResponsePage(new OverviewPage());
			}
		};

		setPolicyForm.add(new DropDownChoice<JoinPolicy>("policy",
				new Model<JoinPolicy>(group.getJoinPolicy()), Arrays
						.asList(JoinPolicy.values()),
				new IChoiceRenderer<JoinPolicy>() {
					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
					 */
					@Override
					public Object getDisplayValue(JoinPolicy object) {
						switch (object) {
							case APPLICATION:
								return "Members must apply, and I must approve their application";
							case INVITATION:
								return "Only members I invite can join my group";
							case OPEN:
								return "Anyone can join my group at any time";

						}

						return null;
					}

					/**
					 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object,
					 *      int)
					 */
					@Override
					public String getIdValue(JoinPolicy object, int index) {
						return object.name();
					}
				}));

		accordion.add(setPolicyForm);

		add(accordion);

	}
}
