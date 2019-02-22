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
package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.UntenabilityVote;
import com.tysanclan.site.projectewok.entities.UntenabilityVoteChoice;
import com.tysanclan.site.projectewok.entities.dao.UntenabilityVoteDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class UntenabilityVotePage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UntenabilityVoteDAO untenabilityVoteDAO;

	public UntenabilityVotePage() {
		super("Untenability Votes");

		add(new ListView<UntenabilityVote>("votes",
				ModelMaker.wrap(untenabilityVoteDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<UntenabilityVote> item) {
				UntenabilityVote vote = item.getModelObject();

				item.add(new Label("name", vote.getRegulation().getName()));

				UntenabilityVoteChoice myChoice = null;

				for (UntenabilityVoteChoice choice : vote.getChoices()) {
					if (choice.getCaster().equals(getUser())) {
						myChoice = choice;
						break;
					}
				}

				String currentChoice = "You have not yet voted on this issue";

				if (myChoice != null) {
					currentChoice = myChoice.isInFavor() ? "You have voted to repeal the regulation"
							: "You have voted to maintain the regulation";
				}

				item.add(new Label("currentvote", currentChoice));

				item.add(new IconLink.Builder("images/icons/tick.png",
						new DefaultClickResponder<UntenabilityVote>(ModelMaker
								.wrap(vote)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private DemocracyService democracyService;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								democracyService.castUntenabilityVote(
										getUser(), getModelObject(), true);

								setResponsePage(new UntenabilityVotePage());
							}
						}).setText("Yes, I want this regulation repealled!")
						.newInstance("yes"));
				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<UntenabilityVote>(ModelMaker
								.wrap(vote)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private DemocracyService democracyService;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								democracyService.castUntenabilityVote(
										getUser(), getModelObject(), false);

								setResponsePage(new UntenabilityVotePage());
							}
						})
						.setText("No, this regulation must remain in effect!")
						.newInstance("no"));

			}

		});

	}
}
