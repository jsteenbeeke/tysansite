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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.components.GalleryPanel;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.components.YoutubeGalleryPanel;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.GroupDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class GroupPage extends TysanPage {
	private static final long serialVersionUID = 1L;
	@SpringBean
	private GroupDAO groupDAO;

	public GroupPage(PageParameters params) {
		super("Group");

		Group g = groupDAO.get(params.get("groupid").toLong());

		if (g == null) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		initComponents(g);
	}

	private void initComponents(Group group) {
		setPageTitle(group.getName());

		add(new Label("description", group.getDescription())
				.setEscapeModelStrings(false));
		if (group.getLeader() != null) {
			add(new MemberListItem("leader", group.getLeader()));
		} else {
			add(new WebMarkupContainer("leader").setVisible(false));
		}

		List<User> users = new LinkedList<User>();

		users.addAll(group.getGroupMembers());
		if (group.getLeader() != null) {
			users.remove(group.getLeader());
		}

		add(new ListView<User>("members", ModelMaker.wrap(users)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<User> item) {
				item.add(new MemberListItem("member", item.getModelObject()));

			}
		});

		add(new GalleryPanel("gallery", group));
		add(new YoutubeGalleryPanel("ygallery", group));

	}

	/**
	 * Creates a new Group Page for the indicated group
	 */
	public GroupPage(Group group) {
		super("Group");

		initComponents(group);
	}
}
