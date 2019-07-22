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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.LambdaRenderer;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.entities.GamePetition;
import com.tysanclan.site.projectewok.entities.Realm;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.CHANCELLOR, Rank.FULL_MEMBER, Rank.SENIOR_MEMBER,
		Rank.SENATOR, Rank.TRUTHSAYER, Rank.REVERED_MEMBER })
public class CreateGamePetitionPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	@SpringBean
	private RealmService realmService;

	public CreateGamePetitionPage() {
		super("Create Game Petition");

		add(new Label("count", new Model<Integer>(
				gameService.getRequiredPetitionSignatures())));

		Form<GamePetition> form = new Form<GamePetition>("petitionForm") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				FileUploadField iconField = (FileUploadField) get("icon");
				FileUpload upload = iconField.getFileUpload();

				TextField<String> nameField = (TextField<String>) get("name");
				DropDownChoice<Realm> realmChoice = (DropDownChoice<Realm>) get(
						"realm");
				TextField<String> realmField = (TextField<String>) get(
						"newRealm");

				String name = nameField.getModelObject();
				Realm realm = realmChoice.getModelObject();
				String realmName = realmField.getModelObject();

				GamePetition p = null;

				if (realm == null) {
					p = gameService.createPetition(getUser(), name, realmName,
							upload.getBytes());
				} else {
					p = gameService.createPetition(getUser(), name, realm,
							upload.getBytes());
				}

				if (p != null) {

					setResponsePage(new SignGamePetitionsPage());
				} else {
					error("Invalid image size, did you upload a valid GIF, JPEG or PNG with dimensions at least 48x48 and at most 64x64?");
				}
			}

		};

		form.setMultiPart(true);

		add(form);

		form.add(new TextField<String>("name", new Model<String>(""))
				.setRequired(true));
		form.add(new FileUploadField("icon").setRequired(true));

		List<Realm> realms = realmService.getRealms();

		DropDownChoice<Realm> realmChoice = new DropDownChoice<Realm>("realm",
				ModelMaker.wrap(Realm.class), ModelMaker.wrapList(realms));
		realmChoice.setNullValid(true);
		realmChoice.setVisible(realms.size() > 0);
		realmChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				DropDownChoice<Realm> rc = (DropDownChoice<Realm>) getComponent();
				Component newRealmField = rc.getParent().get("newRealm");
				newRealmField.setVisible(rc.getModelObject() == null);
				if (target != null) {
					target.add(newRealmField);
				}

			}
		});
		realmChoice.setChoiceRenderer(LambdaRenderer.of(Realm::getName));

		form.add(new TextField<>("newRealm", new Model<>(""))
				.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		form.add(realmChoice);

	}
}
