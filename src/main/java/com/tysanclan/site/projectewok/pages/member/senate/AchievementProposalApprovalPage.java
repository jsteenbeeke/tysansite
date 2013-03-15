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
package com.tysanclan.site.projectewok.pages.member.senate;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.FilterDataProvider;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.AchievementIcon;
import com.tysanclan.site.projectewok.entities.AchievementProposal;
import com.tysanclan.site.projectewok.entities.AchievementProposalVote;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.dao.AchievementProposalDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.AchievementProposalFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractSingleAccordionMemberPage;
import com.tysanclan.site.projectewok.util.ImageUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.SENATOR)
public class AchievementProposalApprovalPage extends
		AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private AchievementProposalDAO achievementProposalDAO;

	public AchievementProposalApprovalPage() {
		super("Approve achievements");

		AchievementProposalFilter filter = new AchievementProposalFilter();
		filter.addOrderBy("name", true);

		getAccordion().add(
				new DataView<AchievementProposal>("proposals",
						FilterDataProvider.of(filter, achievementProposalDAO)) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(
							final Item<AchievementProposal> item) {
						AchievementProposal proposal = item.getModelObject();

						AchievementIcon icon = proposal.getIcon();
						item.add(new Image("icon", new ByteArrayResource(
								ImageUtil.getMimeType(icon.getImage()), icon
										.getImage())));
						item.add(new Label("name", proposal.getName()));
						item.add(new Label("description", proposal
								.getDescription()).setEscapeModelStrings(false));
						item.add(new Label("game",
								proposal.getGame() != null ? proposal.getGame()
										.getName() : "-"));
						item.add(new Label("group",
								proposal.getGroup() != null ? proposal
										.getGroup().getName() : "-"));

						String currentVote = "Not voted yet";

						for (AchievementProposalVote vote : proposal
								.getApprovedBy()) {
							if (vote.getSenator().equals(getUser())) {
								if (vote.isInFavor()) {
									currentVote = "You have voted in favor";
								} else {
									currentVote = "You have voted against";
								}
								break;
							}
						}

						item.add(new Label("current", currentVote));

						item.add(new IconLink.Builder("images/icons/tick.png",
								new DefaultClickResponder<AchievementProposal>(
										ModelMaker.wrap(proposal)) {

									private static final long serialVersionUID = 1L;

									@SpringBean
									private AchievementService achievementService;

									/**
									 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
									 */
									@Override
									public void onClick() {
										achievementService.approveProposal(
												getModelObject(), getUser());

										setResponsePage(new AchievementProposalApprovalPage());
									}

								}).setText("Approve").newInstance("yes"));

						item.add(new IconLink.Builder("images/icons/cross.png",
								new DefaultClickResponder<AchievementProposal>(
										ModelMaker.wrap(proposal)) {

									private static final long serialVersionUID = 1L;

									@SpringBean
									private AchievementService achievementService;

									/**
									 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
									 */
									@Override
									public void onClick() {
										achievementService.rejectProposal(
												getModelObject(), getUser());

										setResponsePage(new AchievementProposalApprovalPage());
									}

								}).setText("Reject")

						.newInstance("no"));

					}
				});
	}
}
