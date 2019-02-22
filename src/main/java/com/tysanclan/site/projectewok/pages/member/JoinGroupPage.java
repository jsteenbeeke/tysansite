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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.components.GroupLink;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.dao.GroupDAO;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class JoinGroupPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GroupDAO groupDAO;

	public JoinGroupPage() {
		super("Join group");

		List<Group> groups = groupDAO.findAll();
		List<Group> currentGroups = getUser().getGroups();

		for (Group group : currentGroups) {
			groups.remove(group);
		}

		List<Group> applied = new LinkedList<Group>();
		List<Group> invited = new LinkedList<Group>();
		List<Group> available = new LinkedList<Group>();

		for (Group group : groups) {
			if (!group.getGroupMembers().contains(getUser())) {
				if (group.getAppliedMembers().contains(getUser())) {
					applied.add(group);
				} else if (group.getInvitedMembers().contains(getUser())) {
					invited.add(group);
				} else {
					switch (group.getJoinPolicy()) {
						case OPEN:
						case APPLICATION:
							available.add(group);
							break;
						default:
					}
				}
			}
		}

		String appliedDescription = applied.isEmpty() ? "You have not applied to any groups"
				: "You have applied to the following groups: ";
		String invitedDescription = invited.isEmpty() ? "You have not been invited to join any groups"
				: "You have been invited to join the following groups: ";
		String availableDescription = available.isEmpty() ? "There are no groups available for you to join"
				: "You can apply to join the following groups: ";

		add(new Label("applied-description", appliedDescription));
		add(new Label("invited-description", invitedDescription));
		add(new Label("can-apply-description", availableDescription));
		add(new ListView<Group>("applyToGroups", ModelMaker.wrap(available)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Group> item) {
				Group group = item.getModelObject();

				Link<Group> applyLink = new Link<Group>("apply",
						ModelMaker.wrap(group)) {

					private static final long serialVersionUID = 1L;

					@SpringBean
					private GroupService groupService;

					@Override
					public void onClick() {
						groupService.applyToGroup(getUser(), getModelObject());
						setResponsePage(new OverviewPage());
					}

				};

				applyLink.add(new ContextImage("icon",
						"images/icons/group_add.png"));

				item.add(applyLink);

				GroupLink link = new GroupLink("group", group);

				link.add(new Label("name", group.getName()));

				item.add(link);
				item.add(new Label("type", group.getTypeDescription()));

			}

		}.setVisible(!available.isEmpty()));

		add(new ListView<Group>("appliedGroups", ModelMaker.wrap(applied)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Group> item) {
				Group group = item.getModelObject();

				GroupLink link = new GroupLink("group", group);

				link.add(new Label("name", group.getName()));

				item.add(link);
				item.add(new Label("type", group.getTypeDescription()));

			}

		}.setVisible(!applied.isEmpty()));

		add(new ListView<Group>("invited", ModelMaker.wrap(invited)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Group> item) {
				Group group = item.getModelObject();

				Link<Group> yesLink = new Link<Group>("yes",
						ModelMaker.wrap(group)) {

					private static final long serialVersionUID = 1L;

					@SpringBean
					private GroupService groupService;

					@Override
					public void onClick() {
						groupService.acceptInvitation(getUser(),
								getModelObject());

						setResponsePage(new JoinGroupPage());
					}

				};

				yesLink.add(new ContextImage("icon", "images/icons/tick.png"));

				item.add(yesLink);

				Link<Group> noLink = new Link<Group>("no",
						ModelMaker.wrap(group)) {

					private static final long serialVersionUID = 1L;

					@SpringBean
					private GroupService groupService;

					@Override
					public void onClick() {
						groupService.declineInvitation(getUser(),
								getModelObject());

						setResponsePage(new JoinGroupPage());
					}

				};

				noLink.add(new ContextImage("icon", "images/icons/cross.png"));

				item.add(noLink);

				GroupLink link = new GroupLink("group", group);

				link.add(new Label("name", group.getName()));

				item.add(link);
				item.add(new Label("type", group.getTypeDescription()));

			}

		}.setVisible(!invited.isEmpty()));
	}
}
