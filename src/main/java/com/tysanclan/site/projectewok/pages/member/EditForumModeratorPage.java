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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.User.CaseInsensitiveUserComparator;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;

/**
 * @author Jeroen Steenbeeke
 */
public class EditForumModeratorPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	private IModel<Forum> forumModel;

	@SpringBean
	private UserDAO userDAO;

	/**
	 * 
	 */
	public EditForumModeratorPage(Forum forum) {
		super("Moderator Management");

		forumModel = ModelMaker.wrap(forum);

		add(new Label("stitle", "Moderators for forum " + forum.getName()));

		List<User> moderators = new LinkedList<User>();
		moderators.addAll(forum.getModerators());

		Collections.sort(moderators, new CaseInsensitiveUserComparator());

		add(new ListView<User>("moderators", ModelMaker.wrap(moderators)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<User> item) {
				User user = item.getModelObject();
				item.add(new Label("user", user.getUsername()));

				Link<User> deleteLink = new Link<User>("remove",
						ModelMaker.wrap(user)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private ForumService forumService;

					/**
					 * @see org.apache.wicket.markup.html.link.Link#onClick()
					 */
					@Override
					public void onClick() {
						User moderator = getModelObject();
						forumService.removeModerator(getUser(), getForum(),
								moderator);

						setResponsePage(new EditForumModeratorPage(getForum()));
					}
				};

				deleteLink.add(new ContextImage("icon",
						"images/icons/group_delete.png"));

				item.add(deleteLink);

			}

		});

		Form<Forum> addModeratorForm = new Form<Forum>("addForm",
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
				DropDownChoice<User> userChoice = (DropDownChoice<User>) get("userSelect");

				User moderator = userChoice.getModelObject();

				forumService.addModerator(getUser(), getModelObject(),
						moderator);

				setResponsePage(new EditForumModeratorPage(getModelObject()));
			}

		};

		UserFilter filter = new UserFilter();
		filter.addRank(Rank.CHANCELLOR);
		filter.addRank(Rank.SENATOR);
		filter.addRank(Rank.TRUTHSAYER);
		filter.addRank(Rank.REVERED_MEMBER);
		filter.addRank(Rank.SENIOR_MEMBER);
		filter.addRank(Rank.FULL_MEMBER);
		filter.addRank(Rank.JUNIOR_MEMBER);
		filter.addOrderBy("username", true);

		List<User> users = userDAO.findByFilter(filter);

		addModeratorForm.add(new DropDownChoice<User>("userSelect", ModelMaker
				.wrap(users.get(0), true), ModelMaker.wrap(users))
				.setNullValid(false));

		add(addModeratorForm);

		add(new Link<Void>("back") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ForumManagementPage());
			}

		});
	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		forumModel.detach();
	}

	private Forum getForum() {
		return forumModel.getObject();
	}
}
