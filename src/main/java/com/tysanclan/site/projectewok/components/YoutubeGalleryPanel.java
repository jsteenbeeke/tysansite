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

import java.util.List;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.YoutubeGalleryItem;

/**
 * @author Jeroen Steenbeeke
 */
public class YoutubeGalleryPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public YoutubeGalleryPanel(String id, User user) {
		super(id);

		initComponents(user.getYoutubeGalleryItems());
	}

	public YoutubeGalleryPanel(String id, Group group) {
		super(id);

		initComponents(group.getYoutubeGalleryItems());
	}

	private void initComponents(List<YoutubeGalleryItem> items) {
		// Slider slider = new Slider("slider", 0, items
		// .size() - 1);

		WebMarkupContainer slider = new WebMarkupContainer("slider");

		PageableListView<YoutubeGalleryItem> view = new PageableListView<YoutubeGalleryItem>(
				"gallery", ModelMaker.wrap(items), 10) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<YoutubeGalleryItem> item) {
				YoutubeGalleryItem galleryItem = item.getModelObject();
				item.add(new Label("title", galleryItem.getDescription()));

				item.add(new YoutubeVideo("vid", galleryItem.getUrl()));

			}
		};

		slider.add(view);

		add(new AjaxPagingNavigator("nav", view).setVisible(items.size() > 10));
		add(slider);
	}

}
