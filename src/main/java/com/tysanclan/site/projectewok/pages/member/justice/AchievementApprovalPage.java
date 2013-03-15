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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Achievement;
import com.tysanclan.site.projectewok.entities.AchievementRequest;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.dao.AchievementRequestDAO;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.util.ImageUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.TRUTHSAYER)
public class AchievementApprovalPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private AchievementService achievementService;

	@SpringBean
	private AchievementRequestDAO requestDAO;

	public AchievementApprovalPage() {
		super("Achievement requests");

		Accordion acc = new Accordion("accordion");
		acc.setHeader(new AccordionHeader(new LiteralOption("h2")));
		acc.setAutoHeight(false);

		acc.add(new ListView<AchievementRequest>("requests", ModelMaker
				.wrap(requestDAO.getNonGroupPendingAchievementRequests())) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<AchievementRequest> item) {
				AchievementRequest request = item.getModelObject();
				Achievement achievement = request.getAchievement();
				Game game = achievement.getGame();

				item.add(new Label("name", request.getAchievement().getName()));

				byte[] iconImage = item.getModelObject().getAchievement()
						.getIcon().getImage();

				byte[] gameImage = game != null ? game.getImage() : new byte[0];

				item.add(new Image("icon", new ByteArrayResource(ImageUtil
						.getMimeType(iconImage), iconImage)));
				item.add(new Image("game", new ByteArrayResource(ImageUtil
						.getMimeType(gameImage), gameImage))
						.setVisible(game != null));

				item.add(new Label("description", request.getAchievement()
						.getDescription()).setEscapeModelStrings(false));
				item.add(new MemberListItem("requester", request
						.getRequestedBy()));

				if (request.getEvidencePicture() != null) {
					byte[] evidence = request.getEvidencePicture();

					item.add(new Image("screenshot", new ByteArrayResource(
							ImageUtil.getMimeType(evidence), evidence)));
				} else {
					item.add(new WebMarkupContainer("screenshot")
							.setVisible(false));
				}

				item.add(new Label("evidence", request.getEvidenceDescription())
						.setEscapeModelStrings(false));

				item.add(new IconLink.Builder("images/icons/tick.png",
						new DefaultClickResponder<AchievementRequest>(
								ModelMaker.wrap(request)) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								achievementService.approveRequest(getUser(),
										getModelObject());

								setResponsePage(new AchievementApprovalPage());

							}
						})
						.setText(
								"Yes, the requirements for this achievement have been met")
						.newInstance("yes"));
				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<AchievementRequest>(
								ModelMaker.wrap(request)) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								achievementService.denyRequest(getUser(),
										getModelObject());

								setResponsePage(new AchievementApprovalPage());

							}
						})
						.setText(
								"No, the requirements for this achievement have not been met")
						.newInstance("no"));

			}

		});

		add(acc);
	}
}
