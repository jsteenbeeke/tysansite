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

import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.ProfileService;
import com.tysanclan.site.projectewok.entities.Profile;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.MemberPage;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class ProfilePanel extends Panel {
	private static final long serialVersionUID = 1L;

	private WebMarkupContainer image;

	private Label age;

	private Date selectedDate = null;

	/**
	 * 
	 */
	public ProfilePanel(String id, User user) {
		super(id);

		PageParameters params = new PageParameters();
		params.add("userid", user.getId().toString());

		add(new BookmarkablePageLink<User>("profilelink", MemberPage.class,
				params));

		Form<User> profileForm = new Form<User>("profile",
				ModelMaker.wrap(user)) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ProfileService profileService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				User u = getModelObject();
				Profile profile = u.getProfile();

				TextField<String> realnameField = (TextField<String>) get("realname");
				TextField<String> photoURLField = (TextField<String>) get("photoURL");
				TextField<String> skypeField = (TextField<String>) get("skypename");
				TextField<String> twitterField = (TextField<String>) get("twitter");

				CheckBox photoPublicCheckbox = (CheckBox) get("public");
				CheckBox skypePublicBox = (CheckBox) get("skypepublic");
				TextArea<String> publicdescField = (TextArea<String>) get("publicdesc");
				TextArea<String> privatedescField = (TextArea<String>) get("privatedesc");

				String realname = realnameField.getModelObject();
				Date birthDate = getSelectedDate();
				String photoURL = photoURLField.getModelObject();
				Boolean photoPublic = photoPublicCheckbox.getModelObject();
				Boolean skypePublic = skypePublicBox.getModelObject();
				String publicdesc = publicdescField.getModelObject();
				String privatedesc = privatedescField.getModelObject();
				String aimName = skypeField.getModelObject();
				String twitter = twitterField.getModelObject();

				if (profile == null) {
					profile = profileService.createProfile(u);
				}

				if (!isBothNullOrEquals(realname, profile.getRealName())) {
					profileService.setRealname(profile, realname);
				}
				if (!isBothNullOrEquals(birthDate, profile.getBirthDate())) {
					profileService.setBirthDate(profile, birthDate);
				}
				if (!isBothNullOrEquals(twitter, profile.getTwitterUID())) {
					profileService.setTwitterUID(profile, twitter);
				}

				if (!isBothNullOrEquals(aimName,
						profile.getInstantMessengerAddress())
						|| !isBothNullOrEquals(skypePublic,
								profile.isInstantMessengerPublic())) {
					profileService.setAIMAddress(profile, aimName, skypePublic);
				}

				if (!isBothNullOrEquals(photoURL, profile.getPhotoURL())
						|| !isBothNullOrEquals(photoPublic,
								profile.isPhotoPublic())) {
					profileService.setPhotoURL(profile, photoURL, photoPublic);
				}
				if (!isBothNullOrEquals(publicdesc,
						profile.getPublicDescription())) {
					profileService.setPublicDescription(profile, publicdesc);
				}
				if (!isBothNullOrEquals(privatedesc,
						profile.getPrivateDescription())) {
					profileService.setPrivateDescription(profile, privatedesc);
				}

				ProfilePanel.this.onUpdated();
			}

			public <T> boolean isBothNullOrEquals(T value1, T value2) {
				if (value1 == null && value2 == null) {
					return true;
				}

				if (value1 == null)
					return false;
				if (value2 == null)
					return false;

				return value1.equals(value2);
			}
		};

		Profile profile = user.getProfile();

		profileForm.add(new TextField<String>("realname", new Model<String>(
				profile != null ? profile.getRealName() : "")));

		Calendar cal = DateUtil.getCalendarInstance();
		cal.add(Calendar.YEAR, -13);
		int year = cal.get(Calendar.YEAR);

		if (profile != null) {
			setSelectedDate(profile.getBirthDate());
		}

		profileForm.add(new InlineDatePicker("birthdate",
				profile != null ? profile.getBirthDate() : null) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onDateSelected(Date date, AjaxRequestTarget target) {
				Label oldAge = getAge();
				Label newAge = new Label("age", getAgeModel(date));
				newAge.setOutputMarkupId(true);
				newAge.setOutputMarkupPlaceholderTag(true);
				oldAge.replaceWith(newAge);
				setAge(newAge);

				setSelectedDate(date);

				if (target != null) {
					target.add(newAge);
				}

			}
		}.setChangeMonth(true).setChangeYear(true)
				.setYearRange("'1900:" + year + "'"));

		profileForm.add(new TextField<String>("twitter", new Model<String>(
				profile != null ? profile.getTwitterUID() : "")));

		TextField<String> photoURLField = new TextField<String>("photoURL",
				new Model<String>(profile != null ? profile.getPhotoURL() : ""));
		photoURLField.setOutputMarkupId(true);
		photoURLField.setOutputMarkupPlaceholderTag(true);

		photoURLField.add(new OnChangeAjaxBehavior() {
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
					container.add(AttributeModifier.replace("src", newURL));
					container.setVisible(true);
				}
				if (target != null) {
					target.add(getImage());
				}

			}
		});

		String currentPhotoURL = profile != null ? profile.getPhotoURL() : null;

		image = new WebMarkupContainer("preview");
		if (currentPhotoURL == null || currentPhotoURL.isEmpty()) {
			image.setVisible(false);
		} else {
			image.add(AttributeModifier.replace("src", currentPhotoURL));
		}

		image.setOutputMarkupId(true);
		image.setOutputMarkupPlaceholderTag(true);

		age = new Label("age", getAgeModel(profile));
		age.setOutputMarkupId(true);
		age.setOutputMarkupPlaceholderTag(true);

		profileForm.add(age);

		profileForm.add(image);
		profileForm.add(new CheckBox("public", new Model<Boolean>(
				profile != null ? profile.isPhotoPublic() : false)));
		profileForm.add(new ContextImage("skypeicon", "images/skype-icon.gif"));
		profileForm.add(new CheckBox("skypepublic", new Model<Boolean>(
				profile != null ? profile.isInstantMessengerPublic() : false)));
		profileForm.add(new TextField<String>("skypename", new Model<String>(
				profile != null ? profile.getInstantMessengerAddress() : "")));

		profileForm.add(new BBCodeTextArea("publicdesc",
				profile != null ? profile.getPublicDescription() : ""));
		profileForm.add(new BBCodeTextArea("privatedesc",
				profile != null ? profile.getPrivateDescription() : ""));

		profileForm.add(photoURLField);

		add(profileForm);

	}

	private IModel<?> getAgeModel(Profile profile) {
		if (profile == null || profile.getBirthDate() == null) {
			return new Model<String>("Unknown");
		}

		return getAgeModel(profile.getBirthDate());
	}

	private IModel<?> getAgeModel(Date date) {
		int _age = DateUtil.calculateAge(date);

		return new Model<Integer>(_age);
	}

	/**
	 * @return the age
	 */
	public Label getAge() {
		return age;
	}

	/**
	 * @param age
	 *            the age to set
	 */
	public void setAge(Label age) {
		this.age = age;
	}

	/**
	 * @return the image
	 */
	public WebMarkupContainer getImage() {
		return image;
	}

	/**
	 * @return the selectedDate
	 */
	public Date getSelectedDate() {
		return selectedDate;
	}

	/**
	 * @param selectedDate
	 *            the selectedDate to set
	 */
	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	public abstract void onUpdated();
}
