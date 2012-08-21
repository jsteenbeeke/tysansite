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

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.AchievementService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.AchievementRequest;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.dao.AchievementRequestDAO;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
public class GroupAchievementApprovalPage extends AbstractMemberPage {
	@SpringBean
	private AchievementService achievementService;

	@SpringBean
	private AchievementRequestDAO requestDAO;

	private IModel<Group> groupModel;

	public GroupAchievementApprovalPage(Group group) {
		super("Achievement requests");

		if (!getUser().equals(group.getLeader())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		groupModel = ModelMaker.wrap(group);

		Accordion acc = new Accordion("accordion");
		acc.setHeader(new AccordionHeader(new LiteralOption("h2")));
		acc.setAutoHeight(false);

		acc.add(new ListView<AchievementRequest>("requests", ModelMaker
				.wrap(requestDAO.getPendingGroupRequests(group))) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<AchievementRequest> item) {
				AchievementRequest request = item.getModelObject();

				item.add(new Label("name", request.getAchievement().getName()));
				item.add(new Image("icon", new DynamicImageResource() {

					private static final long serialVersionUID = 1L;

					@Override
					protected byte[] getImageData() {
						return item.getModelObject().getAchievement().getIcon()
								.getImage();
					}

				}));

				item.add(new Label("description", request.getAchievement()
						.getDescription()).setEscapeModelStrings(false));
				item.add(new MemberListItem("requester", request
						.getRequestedBy()));

				if (request.getEvidencePicture() != null) {
					item.add(new Image("screenshot",
							new DynamicImageResource() {

								private static final long serialVersionUID = 1L;

								@Override
								protected byte[] getImageData() {
									return item.getModelObject()
											.getEvidencePicture();
								}

							}));
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

								setResponsePage(new GroupAchievementApprovalPage(
										groupModel.getObject()));

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

								setResponsePage(new GroupAchievementApprovalPage(
										groupModel.getObject()));

							}
						})
						.setText(
								"No, the requirements for this achievement have not been met")
						.newInstance("no"));

			}

		});

		add(acc);

	}

	@Override
	protected void onDetach() {
		super.onDetach();

		groupModel.detach();
	}
}
