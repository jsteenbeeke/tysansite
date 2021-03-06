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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.NaiveRenderer;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.entities.AchievementProposal;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.dao.RegulationDAO;
import com.tysanclan.site.projectewok.entities.filter.RegulationFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.TRUTHSAYER)
public class AchievementRejectionPage
		extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RegulationDAO regulationDAO;

	public AchievementRejectionPage(AchievementProposal proposal) {
		super("Reject achievement");

		RegulationFilter filter = new RegulationFilter();
		filter.name().orderBy(true);

		final CheckBox charterCheckBox = new CheckBox("charter",
				new Model<Boolean>(false));
		final DropDownChoice<Regulation> regulationChoice = new DropDownChoice<Regulation>(
				"regulation", ModelMaker.wrap(Regulation.class), ModelMaker
				.wrapList(regulationDAO.findByFilter(filter).toJavaList()),
				new NaiveRenderer<Regulation>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getDisplayValue(Regulation object) {
						return object.getName();
					}

					@Override
					public String getIdValue(Regulation object, int index) {
						return object.getId().toString();
					}
				});

		Form<AchievementProposal> rejectForm = new Form<AchievementProposal>(
				"rejectForm", new CompoundPropertyModel<AchievementProposal>(
			ModelMaker.wrap(proposal))) {

			private static final long serialVersionUID = 1L;

			@SpringBean
			private AchievementService achievementService;

			@Override
			public void onSubmit() {
				if (!charterCheckBox.getModelObject()
						&& regulationChoice.getModelObject() == null) {
					error("You must select a regulation or indicate that there is a Charter violation");
					return;
				}

				achievementService.rejectAchievement(getModelObject(),
						regulationChoice.getModelObject(),
						charterCheckBox.getModelObject(), getUser());

				setResponsePage(new TruthsayerAchievementProposalPage());
			}

		};

		rejectForm.add(new Label("name"));
		rejectForm.add(regulationChoice);
		rejectForm.add(charterCheckBox);

		add(rejectForm);

	}
}
