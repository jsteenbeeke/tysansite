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
package com.tysanclan.site.projectewok.pages;

import com.jeroensteenbeeke.hyperion.solstice.data.FilterDataProvider;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.AutoForumLink;
import com.tysanclan.site.projectewok.components.NewsPanel;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO;
import com.tysanclan.site.projectewok.entities.filter.ForumThreadFilter;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
public class NewsPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ForumService forumService;

	@SpringBean
	private ForumThreadDAO forumThreadDAO;

	public NewsPage() {
		super("News");

		Forum forum = forumService.getNewsForum();

		ForumThreadFilter filter = new ForumThreadFilter();
		filter.forum(forum);
		filter.postTime().orderBy(false);

		DataView<ForumThread> newsItems = new DataView<ForumThread>("newsitems",
				FilterDataProvider.of(filter, forumThreadDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<ForumThread> item) {
				User u = getTysanSession().flatMap(TysanSession::getUser)
						.getOrNull();

				item.add(new NewsPanel("newspanel", item.getModelObject(),
						u == null));
			}
		};
		newsItems.setItemsPerPage(5);

		add(newsItems);

		add(new AutoForumLink("navigation", forum, "View news archives"));
	}
}
