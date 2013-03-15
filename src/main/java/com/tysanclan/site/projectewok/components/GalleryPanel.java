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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GalleryService;
import com.tysanclan.site.projectewok.entities.GalleryImage;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class GalleryPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GalleryService galleryService;

	public GalleryPanel(String id, User user) {
		super(id);

		initComponents(user.getGalleryImages());
	}

	public GalleryPanel(String id, Group group) {
		super(id);

		initComponents(group.getGalleryImages());
	}

	private void initComponents(List<GalleryImage> items) {
		WebMarkupContainer slider = new WebMarkupContainer("slider");

		PageableListView<GalleryImage> images = new PageableListView<GalleryImage>(
				"gallery", ModelMaker.wrap(items), 10) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<GalleryImage> item) {
				GalleryImage image = item.getModelObject();

				item.add(new Label("title", image.getDescription()));

				ExternalLink link = new ExternalLink("link",
						galleryService.getUrlBase() + image.getImageFilename());

				link.add(new WebMarkupContainer("thumbnail")
						.add(AttributeModifier.replace(
								"src",
								galleryService.getUrlBase()
										+ image.getThumbnailFilename())));

				item.add(link);
			}
		};

		slider.add(images);

		add(new AjaxPagingNavigator("nav", images)
				.setVisible(items.size() > 10));

		add(slider);
	}
}
