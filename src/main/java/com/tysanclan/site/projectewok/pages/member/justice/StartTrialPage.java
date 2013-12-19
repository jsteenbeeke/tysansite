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
package com.tysanclan.site.projectewok.pages.member.justice;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.components.TysanTinyMCESettings;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RegulationDAO;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class StartTrialPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RegulationDAO regulationDAO;

	@SpringBean
	private UserService userService;

	/**
	 * 
	 */
	public StartTrialPage() {
		super("Report rules violation");

		Form<Trial> accuseForm = new Form<Trial>("accuse") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private LawEnforcementService lawEnforcementService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				DropDownChoice<User> accusedChoice = (DropDownChoice<User>) get("member");
				ListMultipleChoice<Regulation> regulationsChoice = (ListMultipleChoice<Regulation>) get("regulations");
				TextArea<String> motivationArea = (TextArea<String>) get("motivation");

				User accused = accusedChoice.getModelObject();
				Collection<Regulation> regulations = regulationsChoice
						.getModelObject();
				String motivation = motivationArea.getModelObject();

				Trial trial = lawEnforcementService.startTrial(getUser(),
						accused, motivation, regulations);

				if (trial == null) {
					error("Could not assign judge! That means there are not enough Truthsayers, Senators and no Chancellor, or all of them are on trial in this case");
				} else {
					setResponsePage(new OverviewPage());
				}
			}

		};

		List<User> users = userService.getMembers();

		users.remove(getUser());

		Collections.sort(users, new Comparator<User>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			public int compare(User o1, User o2) {
				return o1.getUsername().compareToIgnoreCase(o2.getUsername());
			}
		});

		accuseForm.add(new DropDownChoice<User>("member", ModelMaker.wrap(
				(User) null, true), ModelMaker.wrapChoices(users))
				.setRequired(true));

		accuseForm
				.add(new TextArea<String>("motivation", new Model<String>(""))
						.setRequired(true)
						.add(new TinyMceBehavior(new TysanTinyMCESettings())));

		accuseForm.add(new ListMultipleChoice<Regulation>("regulations",
				ModelMaker.wrapAsCollection(new LinkedList<Regulation>()),
				ModelMaker.wrapChoices(regulationDAO.findAll()),
				new IChoiceRenderer<Regulation>() {
					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
					 */
					@Override
					public Object getDisplayValue(Regulation object) {
						return object.getName();
					}

					@Override
					public String getIdValue(Regulation object, int index) {
						return object.getId().toString();
					}
				}).setRequired(true));

		add(accuseForm);

	}
}
