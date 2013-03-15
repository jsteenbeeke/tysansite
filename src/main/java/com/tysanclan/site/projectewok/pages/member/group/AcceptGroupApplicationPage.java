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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
public class AcceptGroupApplicationPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	private IModel<Group> groupModel;

	/**
	 * 
	 */
	public AcceptGroupApplicationPage(Group group) {
		super("Accept group applications");

		groupModel = ModelMaker.wrap(group);

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		List<User> users = new LinkedList<User>();
		users.addAll(group.getAppliedMembers());

		accordion.add(new ListView<User>("applications", users) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				User user = item.getModelObject();
				item.add(new Label("name", user.getUsername()));

				Link<User> yesLink = new Link<User>("yes", ModelMaker
						.wrap(user)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private GroupService groupService;

					@Override
					public void onClick() {
						groupService.acceptGroupJoinRequest(getUser(),
								getGroup(), getModelObject());
						setResponsePage(new AcceptGroupApplicationPage(
								getGroup()));
					}
				};

				Link<User> noLink = new Link<User>("no", ModelMaker.wrap(user)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private GroupService groupService;

					@Override
					public void onClick() {
						groupService.declineGroupJoinRequest(getUser(),
								getGroup(), getModelObject());
						setResponsePage(new AcceptGroupApplicationPage(
								getGroup()));
					}
				};

				yesLink.add(new ContextImage("icon", "images/icons/tick.png"));
				noLink.add(new ContextImage("icon", "images/icons/cross.png"));

				item.add(yesLink);
				item.add(noLink);

			}

		});

		add(accordion);

	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		groupModel.detach();
	}

	private Group getGroup() {
		return groupModel.getObject();
	}
}
