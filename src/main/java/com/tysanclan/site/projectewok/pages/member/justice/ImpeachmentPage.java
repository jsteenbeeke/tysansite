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

import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Impeachment;
import com.tysanclan.site.projectewok.entities.ImpeachmentVote;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ImpeachmentDAO;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.pages.member.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.SENATOR)
public class ImpeachmentPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ImpeachmentDAO impeachmentDAO;

	public ImpeachmentPage() {
		super("Impeach Chancellor");

		List<Impeachment> impeachments = impeachmentDAO.findAll();

		if (impeachments.isEmpty()) {
			throw new RestartResponseAtInterceptPageException(
					new OverviewPage());
		}

		Impeachment impeachment = impeachments.get(0);

		String chancellor = impeachment.getChancellor().getUsername();

		add(new MemberListItem("chancellor", impeachment.getChancellor()));
		add(new MemberListItem("initiator", impeachment.getInitiator()));

		ImpeachmentVote myVote = null;

		for (ImpeachmentVote vote : impeachment.getVotes()) {
			if (vote.getCaster().equals(getUser())) {
				myVote = vote;
				break;
			}
		}

		add(new Label("vote", myVote == null ? "You have not yet voted"
				: myVote.isInFavor() ? "You have voted to impeach "
						+ chancellor : "You have voted not to impeach "
						+ chancellor));

		add(new IconLink.Builder("images/icons/tick.png",
				new DefaultClickResponder<User>(ModelMaker.wrap(getUser())) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private DemocracyService democracyService;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						democracyService.castImpeachmentVote(getModelObject(),
								true);

						setResponsePage(new ImpeachmentPage());
					}
				}).setText("Yes, I want to start the impeachment procedure")
				.newInstance("yes"));
		add(new IconLink.Builder("images/icons/cross.png",
				new DefaultClickResponder<User>(ModelMaker.wrap(getUser())) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private DemocracyService democracyService;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						democracyService.castImpeachmentVote(getModelObject(),
								false);

						setResponsePage(new ImpeachmentPage());
					}
				}).setText("No, I do not want to impeach " + chancellor)
				.newInstance("no"));

	}
}
