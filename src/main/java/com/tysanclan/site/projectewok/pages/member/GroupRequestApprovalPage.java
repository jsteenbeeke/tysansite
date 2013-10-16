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
package com.tysanclan.site.projectewok.pages.member;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.GroupService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.GroupCreationRequest;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.dao.GroupCreationRequestDAO;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class GroupRequestApprovalPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private GroupCreationRequestDAO groupCreationRequestDAO;

	/**
	 * 
	 */
	public GroupRequestApprovalPage() {
		super("Group Request Approval");

		Accordion accordion = new Accordion("accordion");
		accordion.setAutoHeight(false);
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.getOptions().put("heightStyle", "content");

		List<GroupCreationRequest> requests = groupCreationRequestDAO.findAll();

		String intro = (requests.size() == 0) ? "There are no pending group creation requests"
				: "There are " + requests.size()
						+ " pending group creation requests:";

		add(new Label("pendingtext", intro));

		accordion.add(new ListView<GroupCreationRequest>("pending", ModelMaker
				.wrap(requests)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<GroupCreationRequest> item) {
				GroupCreationRequest request = item.getModelObject();

				boolean social = request.getGame() == null;

				item.add(new Label("name", request.getName()));
				item.add(new Label("type", social ? "Social Group"
						: "Gaming Group"));
				item.add(new MemberListItem("leader", request.getRequester()));
				if (social) {
					item.add(new ContextImage("icon", "images/icons/group.png"));
				} else {
					item.add(new Image("icon", new ByteArrayResource(
							"image/gif", request.getGame().getImage())));
				}

				item.add(new Label("description", request.getDescription())
						.setEscapeModelStrings(false));
				item.add(new Label("motivation", request.getMotivation())
						.setEscapeModelStrings(false));
				Link<GroupCreationRequest> yesLink = new Link<GroupCreationRequest>(
						"yes", ModelMaker.wrap(request)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private GroupService groupService;

					/**
					 * @see org.apache.wicket.markup.html.link.Link#onClick()
					 */
					@Override
					public void onClick() {
						groupService.acceptRequest(getUser(), getModelObject());
						setResponsePage(new GroupRequestApprovalPage());
					}
				};
				Link<GroupCreationRequest> noLink = new Link<GroupCreationRequest>(
						"no", ModelMaker.wrap(request)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private GroupService groupService;

					/**
					 * @see org.apache.wicket.markup.html.link.Link#onClick()
					 */
					@Override
					public void onClick() {
						groupService
								.declineRequest(getUser(), getModelObject());
						setResponsePage(new GroupRequestApprovalPage());
					}
				};

				yesLink.add(new ContextImage("icon", "images/icons/tick.png"));
				noLink.add(new ContextImage("icon", "images/icons/cross.png"));

				item.add(yesLink);
				item.add(noLink);
			}

		});

		add(accordion);
	}
}
