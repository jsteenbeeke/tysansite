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
package com.tysanclan.site.projectewok.components;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;

import com.tysanclan.site.projectewok.entities.twitter.ITweet;
import com.tysanclan.site.projectewok.util.DateUtil;
import com.tysanclan.site.projectewok.util.StringUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class TwitterStatusPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public TwitterStatusPanel(String id, List<? extends ITweet> list) {
		this(id, list, false);
	}

	public TwitterStatusPanel(String id, List<? extends ITweet> list,
			final boolean convertLinks) {
		super(id);

		PageableListView<ITweet> tweets = new PageableListView<ITweet>(
				"tweets", list, 20) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ITweet> item) {
				ITweet status = item.getModelObject();

				String source = status.getSource();

				StringBuilder statString = new StringBuilder();

				statString.append(StringUtil.twitterify(status.getContents(),
						convertLinks));
				statString
						.append("<br /> <span style=\"font-size: 8px;\">Posted by ");
				statString
						.append("<a class=\"Yellow\" href=\"http://twitter.com/");
				statString.append(status.getScreenName());
				statString.append("\">");
				statString.append(status.getName());
				statString.append("</a>");
				statString.append(" on ");
				statString.append(DateUtil.getESTFormattedString(
						status.getPosted(), "dd MMM yyyy 'at' hh:mm"));
				statString.append(" using ");
				if (!"web".equals(source)) {
					statString.append(source.substring(0, 2));

					statString.append(" class=\"Yellow\"");
					statString.append(source.substring(2));
				} else {
					statString
							.append("<a class=\"Yellow\" href=\"http://twitter.com/\">web</a>");
				}
				statString.append("</span>");

				item.add(new Label("tweet", statString.toString())
						.setEscapeModelStrings(false));

			}
		};

		add(new PagingNavigator("nav", tweets));

		add(tweets);
	}
}
