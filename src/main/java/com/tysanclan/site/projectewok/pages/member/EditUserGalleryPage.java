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

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.progressbar.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GalleryService;
import com.tysanclan.site.projectewok.beans.GalleryService.QuotaExceedException;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.YoutubeVideo;
import com.tysanclan.site.projectewok.entities.GalleryImage;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.YoutubeGalleryItem;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class EditUserGalleryPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(EditUserGalleryPage.class);

	@SpringBean
	private GalleryService galleryService;

	private int tabIndex;

	public EditUserGalleryPage(User user) {
		this(user, 0);
	}

	public EditUserGalleryPage(User user, int selectedTab) {
		super("User Gallery");

		this.tabIndex = selectedTab;

		addImageManager(user);
		addYoutubeManager(user);

	}

	@Override
	protected Integer getAutoTabIndex() {
		return tabIndex;
	}

	private void addYoutubeManager(User user) {
		add(new ListView<YoutubeGalleryItem>("gallery", ModelMaker.wrap(user
				.getYoutubeGalleryItems())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<YoutubeGalleryItem> item) {
				YoutubeGalleryItem galleryItem = item.getModelObject();

				item.add(new YoutubeVideo("vid", galleryItem.getUrl()));

				item.add(new Label("description", galleryItem.getDescription()));

				item.add(new IconLink.Builder("images/icons/image_delete.png",
						new DefaultClickResponder<YoutubeGalleryItem>(
								ModelMaker.wrap(galleryItem)) {
							private static final long serialVersionUID = 1L;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								galleryService.deleteVideo(getModelObject());
								setResponsePage(new EditUserGalleryPage(
										getUser(), 1));
							}

						}).newInstance("delete"));
			}

		});
		final TextField<String> urlField = new TextField<String>("url",
				new Model<String>(""));
		urlField.setRequired(true);
		urlField.add(new YouTubeUrlValidator());

		final TextField<String> descriptionField = new TextField<String>(
				"description", new Model<String>(""));
		descriptionField.setRequired(true);

		Form<YoutubeGalleryItem> addForm = new Form<YoutubeGalleryItem>(
				"youtubeAddForm") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				galleryService.addVideo(getUser(), null,
						descriptionField.getModelObject(),
						urlField.getModelObject());

				setResponsePage(new EditUserGalleryPage(getUser(), 1));
			}
		};

		addForm.add(urlField);

		addForm.add(descriptionField);

		add(addForm);
	}

	private void addImageManager(User user) {
		WebMarkupContainer slider = new WebMarkupContainer("slider");

		slider.add(new ListView<GalleryImage>("gallery", ModelMaker.wrap(user
				.getGalleryImages())) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<GalleryImage> item) {
				GalleryImage image = item.getModelObject();

				item.add(new WebMarkupContainer("thumbnail")
						.add(AttributeModifier.replace(
								"src",
								galleryService.getUrlBase()
										+ image.getThumbnailFilename())));

				item.add(new IconLink.Builder("images/icons/image_delete.png",
						new DefaultClickResponder<GalleryImage>(ModelMaker
								.wrap(image)) {
							private static final long serialVersionUID = 1L;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								GalleryImage _image = getModelObject();

								File f = new File(galleryService
										.getGalleryPath()
										+ _image.getImageFilename());
								if (f.exists()) {
									log.trace("Deleting file: " + f.delete());
								}
								f = new File(galleryService.getGalleryPath()
										+ _image.getThumbnailFilename());
								if (f.exists()) {
									log.trace("Deleting file: " + f.delete());
								}

								galleryService.deleteImage(getModelObject());
								setResponsePage(new EditUserGalleryPage(
										getUser()));
							}

						}).newInstance("delete"));
			}
		});

		add(slider);

		ProgressBar bar = new ProgressBar("space");

		BigDecimal current = galleryService.getCurrentGallerySizeMB(user);
		BigDecimal max = galleryService.getMaxGallerySizeMB();

		int value = current.multiply(new BigDecimal(100))
				.divide(max, 0, RoundingMode.HALF_UP).intValue();

		bar.setValue(value);

		add(bar);

		add(new Label("curr", new Model<BigDecimal>(current)));
		add(new Label("max", new Model<BigDecimal>(max)));

		final FileUploadField imageUploadField = new FileUploadField("file");
		final TextField<String> imageDescriptionField = new TextField<String>(
				"description", new Model<String>(""));

		imageDescriptionField.setRequired(true);

		Form<GalleryImage> uploadForm = new Form<GalleryImage>("uploadForm") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserDAO userDAO;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				String description = imageDescriptionField.getModelObject();
				FileUpload upload = imageUploadField.getFileUpload();

				try {
					GalleryImage image = galleryService.uploadImage(
							description, getUser(), null, upload.getBytes());
					if (image == null) {
						error("Not a valid image file");
					} else {
						User _user = getUser();
						Long id = _user.getId();
						userDAO.evict(_user);

						setResponsePage(new EditUserGalleryPage(
								userDAO.load(id)));
					}
				} catch (QuotaExceedException e) {
					error(e.getMessage());
				}
			}
		};

		uploadForm.add(imageUploadField);
		uploadForm.add(imageDescriptionField);

		uploadForm.setMultiPart(true);

		add(uploadForm);
	}
}
