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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionActive;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ForumCategoryDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO;

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

		DataView<ForumCategory> cats = new DataView<ForumCategory>(
				"categories", ForumDataProvider.of(forumCategoryDAO)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumDAO forumDAO;

			@Override
			protected void populateItem(Item<ForumCategory> item) {
				ForumCategory cat = item.getModelObject();
				String catName = cat.getName();

				TysanSession session = TysanSession.get();
				User user = session != null ? session.getUser() : null;

				boolean active = user == null || !user.isCollapseForums();

				if (!active) {
					for (Forum f : cat.getForums()) {
						if (f.isInteractive()) {
							active = true;
							break;
						}
					}
				}

				// item.add(item);

				Accordion accordion = new Accordion("accordion");
				accordion
						.setHeader(new AccordionHeader(new LiteralOption("h2")));
				accordion.setAutoHeight(false);
				accordion.getOptions().put("heightStyle", "'content'");

				boolean collapse = user != null && user.isCollapseForums();

				if (collapse && !active) {
					accordion.setCollapsible(collapse);
					accordion.setActive(new AccordionActive(active));
				}

				item.add(accordion);

				accordion.add(new WebMarkupContainer("unread")
						.setVisible(session != null
								&& session.getUser() != null));

				Label label = new Label("cattitle", catName);

				accordion.add(label);

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

						TysanSession sess = TysanSession.get();
						User u = sess != null ? sess.getUser() : null;

						int unreadCount = u != null ? forumService
								.getForumUnreadCount(u, forum) : 0;

						innerItem.add(new Label("unread", new Model<Integer>(
								unreadCount)).setVisible(sess != null
								&& sess.getUser() != null));

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

						innerItem.add(new Label("moderators", modList
								.toString()));
					}

				};

				accordion.add(forums);

			}
		};

		add(cats);

		Link<Void> markAsReadLink = new Link<Void>("markasread") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				TysanSession session = (TysanSession) TysanSession.get();

				if (session != null && session.getUser() != null) {
					forumService.clearUnreadPosts(session.getUser());
				}

			}

		};

		markAsReadLink.add(new ContextImage("icon", "images/icons/eye.png"));

		TysanSession session = (TysanSession) TysanSession.get();
		User user = null;

		if (session != null) {
			user = session.getUser();
		}

		markAsReadLink.setVisible(user != null);

		add(markAsReadLink);
	}

}
