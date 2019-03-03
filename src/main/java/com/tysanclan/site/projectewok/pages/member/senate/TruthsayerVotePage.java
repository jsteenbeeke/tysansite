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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.TruthsayerNomination;
import com.tysanclan.site.projectewok.entities.TruthsayerNominationVote;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerNominationDAO;
import com.tysanclan.site.projectewok.entities.filter.TruthsayerNominationFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
public class TruthsayerVotePage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private TruthsayerNominationDAO truthsayerNominationDAO;

	public TruthsayerVotePage() {
		super("Truthsayer votes");

		TruthsayerNominationFilter filter = new TruthsayerNominationFilter();
		filter.voteStart().isNotNull();

		add(new ListView<TruthsayerNomination>("nominations", ModelMaker
				.wrap(truthsayerNominationDAO.findByFilter(filter)
						.toJavaList())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<TruthsayerNomination> item) {
				TruthsayerNomination nomination = item.getModelObject();

				item.add(new MemberListItem("member", nomination.getUser()));

				int yesCount = 0, noCount = 0;

				TruthsayerNominationVote myVote = null;

				for (TruthsayerNominationVote vote : nomination.getVotes()) {
					if (vote.getSenator().equals(getUser())) {
						myVote = vote;
					}
					if (vote.getVerdict()) {
						yesCount++;
					} else {
						noCount++;
					}
				}

				item.add(new Label("yesCount", new Model<Integer>(yesCount)));
				item.add(new Label("noCount", new Model<Integer>(noCount)));

				String verdict = "Accepted";

				if (yesCount > 0 || noCount > 0) {
					int total = yesCount + noCount;
					int fraction = (100 * yesCount) / total;
					if (fraction < 51) {
						verdict = "Rejected";
					}
				}

				item.add(new Label("verdict", verdict));

				Link<TruthsayerNomination> yesLink = new Link<TruthsayerNomination>(
						"yes", ModelMaker.wrap(nomination)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private LawEnforcementService lawEnforcementService;

					@Override
					public void onClick() {
						lawEnforcementService
								.voteInFavor(getUser(), getModelObject());
						setResponsePage(new TruthsayerVotePage());

					}

				};

				yesLink.add(new ContextImage("icon", "images/icons/tick.png"));

				yesLink.setVisible(myVote == null);

				item.add(yesLink);

				Link<TruthsayerNomination> noLink = new Link<TruthsayerNomination>(
						"no", ModelMaker.wrap(nomination)) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private LawEnforcementService lawEnforcementService;

					@Override
					public void onClick() {
						lawEnforcementService
								.voteAgainst(getUser(), getModelObject());
						setResponsePage(new TruthsayerVotePage());
					}

				};

				noLink.add(new ContextImage("icon", "images/icons/cross.png"));

				noLink.setVisible(myVote == null);

				item.add(noLink);

				item.add(new Label("vote", myVote == null ?
						null :
						myVote.getVerdict() ? "Yes" : "No")
						.setVisible(myVote != null));

			}

		});

	}
}
