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
package com.tysanclan.site.projectewok.pages.member.senate;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.BBCodeTextArea;
import com.tysanclan.site.projectewok.entities.RegulationChange;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.SENATOR, Rank.CHANCELLOR })
public class AddRegulationPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public AddRegulationPage() {
		super("Create Regulation");

		add(createAddForm());

	}

	/**
	 */
	private Form<RegulationChange> createAddForm() {
		Form<RegulationChange> form = new Form<RegulationChange>("addForm") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private DemocracyService democracyService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@SuppressWarnings("unchecked")
			@Override
			protected void onSubmit() {
				TextArea<String> descriptionArea = (TextArea<String>) get(
						"description");
				TextField<String> newTitleField = (TextField<String>) get(
						"newTitle");

				String newDescription = descriptionArea.getModelObject();
				String newTitle = newTitleField.getModelObject();

				RegulationChange vote = democracyService
						.createAddRegulationVote(getUser(), newTitle,
								newDescription);
				if (vote != null) {
					if (getUser().getRank() == Rank.SENATOR) {
						setResponsePage(new RegulationModificationPage());
					} else {
						setResponsePage(new VetoPage());
					}
				}

			}

		};

		form.add(new TextField<String>("newTitle", new Model<String>("")));

		form.add(new BBCodeTextArea("description", "").setRequired(true));

		return form;
	}
}
