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

import java.util.*;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class PrimaryPreferencesPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer image;

	/**
	 * 
	 */
	public PrimaryPreferencesPanel(String id, User user) {
		super(id);

		Form<User> settingsForm = new Form<User>("settingsForm",
				ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private UserService userService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				User _user = getModelObject();
				Long user_id = _user.getId();

				DropDownChoice<String> tzChoice = (DropDownChoice<String>) get("timezone");
				TextField<String> customTitleField = (TextField<String>) get("customTitle");
				TextField<String> imageURLField = (TextField<String>) get("imageURL");
				TextArea<String> signatureArea = (TextArea<String>) get("signature");
				CheckBox collapseBox = (CheckBox) get("collapseForums");

				String timezone = tzChoice.getModelObject();
				String customTitle = customTitleField.getModelObject();
				String imageURL = imageURLField.getModelObject();
				String signature = signatureArea.getModelObject();
				boolean collapse = collapseBox.getModelObject();

				userService.setUserAvatar(user_id, imageURL);
				userService.setUserCustomTitle(user_id, customTitle);
				userService.setUserSignature(user_id, signature);
				userService.setUserTimezone(user_id, timezone);
				userService.setUserCollapseForums(user_id, collapse);
			}

		};

		List<String> timezones = new LinkedList<String>();
		timezones.addAll(Arrays.asList(TimeZone.getAvailableIDs()));
		Collections.sort(timezones);

		settingsForm.add(new CheckBox("collapseForums", new Model<Boolean>(user
				.isCollapseForums())));

		settingsForm.add(new DropDownChoice<String>("timezone",
				new Model<String>(user.getTimezone()), timezones));
		settingsForm.add(new TextField<String>("customTitle",
				new Model<String>(user.getCustomTitle())).add(
				StringValidator.maximumLength(255))
				.setEscapeModelStrings(false));

		TextField<String> urlField = new TextField<String>("imageURL",
				new Model<String>(user.getImageURL()));
		urlField.add(StringValidator.maximumLength(255));
		urlField.setOutputMarkupId(true);
		urlField.setOutputMarkupPlaceholderTag(true);

		urlField.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior#onUpdate(org.apache.wicket.ajax.AjaxRequestTarget)
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				TextField<String> urlComponent = (TextField<String>) getComponent();

				String newURL = urlComponent.getModelObject();

				WebMarkupContainer container = getImage();

				if (newURL == null || newURL.isEmpty()) {
					container.setVisible(false);

				} else {
					container.add(new SimpleAttributeModifier("src", newURL));
					container.setVisible(true);
				}
				if (target != null) {
					target.addComponent(getImage());
				}

			}
		});

		settingsForm.add(urlField);

		settingsForm.add(new TextArea<String>("signature", new Model<String>(
				user.getSignature())).add(new TinyMceBehavior(
				new TysanTinyMCESettings())));

		image = new WebMarkupContainer("preview");
		if (user.getImageURL() == null || user.getImageURL().isEmpty()) {
			image.setVisible(false);
		} else {
			image.add(new SimpleAttributeModifier("src", user.getImageURL()));
		}

		image.setOutputMarkupId(true);
		image.setOutputMarkupPlaceholderTag(true);

		settingsForm.add(image);

		add(settingsForm);

	}

	/**
	 * @return the image
	 */
	public WebMarkupContainer getImage() {
		return image;
	}

	public abstract void onSubmit();
}
