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
package com.tysanclan.site.projectewok.components;

import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ForumCategoryDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumOverviewPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumService forumService;

	@SpringBean
	private ForumCategoryDAO forumCategoryDAO;

	/**
	 * @param id Component ID
	 */
	public ForumOverviewPanel(String id) {
		super(id);

		DataView<ForumCategory> cats = new DataView<ForumCategory>("categories",
				ForumDataProvider.of(forumCategoryDAO)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumDAO forumDAO;

			@Override
			protected void populateItem(Item<ForumCategory> item) {
				ForumCategory cat = item.getModelObject();
				String catName = cat.getName();

				User user = TysanSession.session()
						.flatMap(TysanSession::getUser).getOrNull();

				boolean active = user == null || !user.isCollapseForums();

				if (!active) {
					for (Forum f : cat.getForums()) {
						if (f.isInteractive()) {
							active = true;
							break;
						}
					}
				}

				item.add(new WebMarkupContainer("unread")
						.setVisible(user != null));

				Label label = new Label("cattitle", catName);

				item.add(label);

				DataView<Forum> forums = new DataView<Forum>("forums",
						ForumDataProvider.of(cat, forumDAO)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private ForumThreadDAO forumThreadDAO;

					@Override
					protected void populateItem(Item<Forum> innerItem) {
						final Forum forum = innerItem.getModelObject();

						innerItem.add(new AutoForumLink("forumlink", forum));
						innerItem.add(new Label("forumdescription",
								new Model<String>(forum.getDescription())));
						innerItem.add(new Label("total", new Model<Long>(
								ForumDataProvider.of(forum, forumThreadDAO)
										.size())));

						User u = TysanSession.session()
								.flatMap(TysanSession::getUser).getOrNull();

						int unreadCount = u != null ?
								forumService.getForumUnreadCount(u, forum) :
								0;

						innerItem.add(new Label("unread",
								new Model<Integer>(unreadCount))
								.setVisible(u != null));

						StringBuilder modList = new StringBuilder();
						for (User mod : forum.getModerators()) {
							if (modList.length() > 0) {
								modList.append(", ");
							}

							modList.append(mod.getUsername());
						}

						if (forum.getModerators().isEmpty()) {
							modList.append("-");
						}

						innerItem.add(new Label("moderators",
								modList.toString()));
					}

				};

				item.add(forums);

			}
		};

		add(cats);

		Link<Void> markAsReadLink = new Link<Void>("markasread") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				User user = TysanSession.session()
						.flatMap(TysanSession::getUser).getOrNull();

				if (user != null) {
					forumService.clearUnreadPosts(user);
				}

			}

		};

		markAsReadLink.add(new ContextImage("icon", "images/icons/eye.png"));

		User user = TysanSession.session().flatMap(TysanSession::getUser)
				.getOrNull();

		markAsReadLink.setVisible(user != null);

		add(markAsReadLink);
	}

}
