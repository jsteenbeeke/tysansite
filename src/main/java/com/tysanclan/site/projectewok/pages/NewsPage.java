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
package com.tysanclan.site.projectewok.pages;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionAnimated;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.FilterDataProvider;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.AutoForumLink;
import com.tysanclan.site.projectewok.components.NewsPanel;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ForumThreadFilter;

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
		filter.setForum(forum);
		filter.addOrderBy("postTime", false);

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAnimated(new AccordionAnimated("slide"));
		accordion.setAutoHeight(false);

		DataView<ForumThread> newsItems = new DataView<ForumThread>(
				"newsitems", FilterDataProvider.of(filter, forumThreadDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<ForumThread> item) {
				User u = NewsPage.this.getTysanSession() != null ? NewsPage.this
						.getTysanSession().getUser() : null;

				item.add(new NewsPanel("newspanel", item.getModelObject(),
						u == null));
			}
		};
		newsItems.setItemsPerPage(5);

		accordion.add(newsItems);

		add(accordion);

		add(new AutoForumLink("navigation", forum, "View news archives"));
	}
}
