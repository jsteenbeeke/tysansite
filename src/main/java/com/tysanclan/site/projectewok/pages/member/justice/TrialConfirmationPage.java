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
package com.tysanclan.site.projectewok.pages.member.justice;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.dao.TrialDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.TrialFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.TRUTHSAYER)
public class TrialConfirmationPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private TrialDAO trialDAO;

	@SpringBean
	private LawEnforcementService lawEnforcementService;

	public TrialConfirmationPage() {
		super("Trial Confirmation");

		TrialFilter filter = new TrialFilter();
		filter.setWithTrialThread(false);

		add(
				new ListView<Trial>("trials", ModelMaker.wrap(trialDAO
						.findByFilter(filter))) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
					 */
					@Override
					protected void populateItem(ListItem<Trial> item) {
						Trial trial = item.getModelObject();

						item.add(new MemberListItem("accuser", trial
								.getAccuser()));
						item.add(new MemberListItem("accused", trial
								.getAccused()));
						StringBuilder regBuilder = new StringBuilder();

						for (Regulation reg : trial.getRegulations()) {
							if (regBuilder.length() > 0) {
								regBuilder.append(", ");
							}
							regBuilder.append(reg.getName());
						}

						item.add(new Label("regulations", regBuilder.toString()));
						item.add(new Label("motivation", trial.getMotivation())
								.setEscapeModelStrings(false));

						item.add(new IconLink.Builder("images/icons/tick.png",
								new DefaultClickResponder<Trial>(ModelMaker
										.wrap(trial)) {

									private static final long serialVersionUID = 1L;

									/**
									 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
									 */
									@Override
									public void onClick() {
										lawEnforcementService.confirmTrial(
												getUser(), getModelObject());

										setResponsePage(new TrialConfirmationPage());
									}

								}).newInstance("accept"));

						item.add(new IconLink.Builder("images/icons/cross.png",
								new DefaultClickResponder<Trial>(ModelMaker
										.wrap(trial)) {

									private static final long serialVersionUID = 1L;

									/**
									 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
									 */
									@Override
									public void onClick() {
										lawEnforcementService.dismissTrial(
												getUser(), getModelObject());

										setResponsePage(new TrialConfirmationPage());
									}

								}).newInstance("dismiss"));
					}
				});
	}
}
