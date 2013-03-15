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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
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
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.GroupForum;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class EditGroupForumModeratorPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	private IModel<GroupForum> forumModel;

	public EditGroupForumModeratorPage(GroupForum groupForum) {
		super("Moderators for forum " + groupForum.getName());

		forumModel = ModelMaker.wrap(groupForum);

		Group group = groupForum.getGroup();

		if (!group.getLeader().equals(getUser())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		accordion.add(new Label("title", "Moderators for forum "
				+ groupForum.getName()));

		List<User> moderators = new LinkedList<User>();
		moderators.addAll(groupForum.getModerators());

		Collections.sort(moderators, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getUsername().compareTo(o2.getUsername());
			}

		});

		accordion.add(new ListView<User>("moderators", ModelMaker
				.wrap(moderators)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				User user = item.getModelObject();
				item.add(new Label("user", user.getUsername()));

				Link<User> deleteLink = new Link<User>("remove", ModelMaker
						.wrap(user)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private ForumService forumService;

					/**
					 * @see org.apache.wicket.markup.html.link.Link#onClick()
					 */
					@Override
					public void onClick() {
						User moderator = getModelObject();

						setResponsePage(new EditGroupForumModeratorPage(
								(GroupForum) forumService.removeModerator(
										getUser(), getForum(), moderator)));
					}
				};

				deleteLink.add(new ContextImage("icon",
						"images/icons/group_delete.png"));

				item.add(deleteLink);

			}

		});

		Form<GroupForum> addModeratorForm = new Form<GroupForum>("addForm",
				ModelMaker.wrap(groupForum)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumService forumService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<User> userChoice = (DropDownChoice<User>) get("userSelect");

				User moderator = userChoice.getModelObject();

				setResponsePage(new EditGroupForumModeratorPage(
						(GroupForum) forumService.addModerator(getUser(),
								getModelObject(), moderator)));
			}

		};

		List<User> users = new LinkedList<User>();

		users.addAll(group.getGroupMembers());
		users.removeAll(moderators);

		Collections.sort(users, new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return o1.getUsername().compareToIgnoreCase(o2.getUsername());
			}

		});

		addModeratorForm.add(new DropDownChoice<User>("userSelect", ModelMaker
				.wrap(users.isEmpty() ? null : users.get(0), true), ModelMaker
				.wrap(users)).setNullValid(false));

		addModeratorForm.setVisible(!users.isEmpty());

		accordion.add(addModeratorForm);

		add(accordion);

		addBackLink(group);
	}

	private GroupForum getForum() {
		return forumModel.getObject();
	}

	private void addBackLink(Group group) {
		add(new Link<Group>("back", ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new GroupForumManagementPage(getModelObject()));

			}
		});
	}
}
