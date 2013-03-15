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
package com.tysanclan.site.projectewok.pages.member.group;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.progressbar.ProgressBar;
import org.odlabs.wiquery.ui.tabs.Tabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GalleryService;
import com.tysanclan.site.projectewok.beans.GalleryService.QuotaExceedException;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.YoutubeVideo;
import com.tysanclan.site.projectewok.entities.GalleryImage;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.YoutubeGalleryItem;
import com.tysanclan.site.projectewok.entities.dao.GroupDAO;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.EditUserGalleryPage;
import com.tysanclan.site.projectewok.pages.member.YouTubeUrlValidator;
import com.tysanclan.site.projectewok.pages.member.admin.AutoSelectTabs;

/**
 * @author Jeroen Steenbeeke
 */
public class EditGroupGalleryPage extends AbstractMemberPage {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(EditGroupGalleryPage.class);

	@SpringBean
	private GalleryService galleryService;

	private Tabs tabs;

	public EditGroupGalleryPage(Group group) {
		this(group, 0);
	}

	public EditGroupGalleryPage(Group group, int selectedTab) {
		super("Group Gallery");

		if (!group.isAllowMemberGalleryAccess()
				&& !getUser().equals(group.getLeader())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		tabs = new AutoSelectTabs("tabs", selectedTab);

		addYoutubeManager(group);
		addImageManager(group);

		add(tabs);

	}

	private void addYoutubeManager(Group group) {
		tabs.add(new ListView<YoutubeGalleryItem>("gallery", ModelMaker
				.wrap(group.getYoutubeGalleryItems())) {
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

		Form<Group> addForm = new Form<Group>("youtubeAddForm",
				ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				galleryService.addVideo(null, getModelObject(),
						descriptionField.getModelObject(),
						urlField.getModelObject());

				setResponsePage(new EditGroupGalleryPage(getModelObject(), 1));
			}
		};

		addForm.add(urlField);

		addForm.add(descriptionField);

		tabs.add(addForm);

	}

	private void addImageManager(Group group) {
		WebMarkupContainer slider = new WebMarkupContainer("slider");

		slider.add(new ListView<GalleryImage>("gallery", ModelMaker.wrap(group
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
								GalleryImage img = getModelObject();
								Group gr = img.getGroup();

								File f = new File(galleryService
										.getGalleryPath()
										+ img.getImageFilename());
								if (f.exists()) {
									log.trace("Delete file: " + f.delete());
								}
								f = new File(galleryService.getGalleryPath()
										+ img.getThumbnailFilename());
								if (f.exists()) {
									log.trace("Delete file: " + f.delete());
								}

								galleryService.deleteImage(img);
								setResponsePage(new EditGroupGalleryPage(gr));
							}

						}).newInstance("delete"));
			}
		});

		tabs.add(slider);

		ProgressBar bar = new ProgressBar("space");

		BigDecimal current = galleryService.getCurrentGallerySizeMB(group);
		BigDecimal max = galleryService.getMaxGallerySizeMB();

		int value = current.multiply(new BigDecimal(100))
				.divide(max, 0, RoundingMode.HALF_UP).intValue();

		bar.setValue(value);

		tabs.add(bar);

		tabs.add(new Label("curr", new Model<BigDecimal>(current)));
		tabs.add(new Label("max", new Model<BigDecimal>(max)));

		final FileUploadField imageUploadField = new FileUploadField("file");
		final TextField<String> imageDescriptionField = new TextField<String>(
				"description", new Model<String>(""));

		imageDescriptionField.setRequired(true);

		Form<Group> uploadForm = new Form<Group>("uploadForm",
				ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GroupDAO groupDAO;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				Group gr = getModelObject();

				String description = imageDescriptionField.getModelObject();
				FileUpload upload = imageUploadField.getFileUpload();

				try {
					GalleryImage image = galleryService.uploadImage(
							description, null, getModelObject(),
							upload.getBytes());
					if (image == null) {
						error("Not a valid image file");
					} else {
						Long id = gr.getId();
						groupDAO.evict(gr);

						setResponsePage(new EditGroupGalleryPage(
								groupDAO.load(id)));
					}
				} catch (QuotaExceedException e) {
					error(e.getMessage());
				}
			}
		};

		uploadForm.add(imageUploadField);
		uploadForm.add(imageDescriptionField);

		uploadForm.setMultiPart(true);

		tabs.add(uploadForm);

		final CheckBox allowBox = new CheckBox("allow", new Model<Boolean>(
				group.isAllowMemberGalleryAccess()));

		Form<Group> setUserAccessForm = new Form<Group>("userAccessForm",
				ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GroupService groupService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				Group gr = getModelObject();

				groupService.setGalleryAccess(gr, allowBox.getModelObject());

				Page p = new EditGroupGalleryPage(gr, 2);

				p.info("Group members may "
						+ (allowBox.getModelObject() ? "now" : "no longer")
						+ " edit the galleries");

				setResponsePage(p);
			}

		};

		setUserAccessForm.add(allowBox);

		setUserAccessForm.setVisible(getUser().equals(group.getLeader()));

		tabs.add(setUserAccessForm);
		tabs.add(new WebMarkupContainer("permissions")
				.setVisible(setUserAccessForm.isVisible()));
	}
}
