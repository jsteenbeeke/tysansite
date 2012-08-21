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

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.RegulationChange;
import com.tysanclan.site.projectewok.entities.RegulationChange.ChangeType;
import com.tysanclan.site.projectewok.entities.RegulationChangeVote;
import com.tysanclan.site.projectewok.entities.dao.RegulationChangeDAO;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.SENATOR)
public class RegulationModificationPage extends AbstractMemberPage {
	@SpringBean
	private RegulationChangeDAO regulationChangeDAO;

	/**
	 * 
	 */
	public RegulationModificationPage() {
		super("Regulations");

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		accordion.add(new ListView<RegulationChange>("votes", ModelMaker
				.wrap(regulationChangeDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RegulationChange> item) {
				RegulationChange change = item.getModelObject();

				String name = change.getChangeType() == ChangeType.ADD ? change
						.getTitle() : change.getRegulation().getName();

				item.add(new Label("title", ""
						+ change.getChangeType().toString() + " regulation "
						+ name));

				if (change.getChangeType() == ChangeType.MODIFY) {
					item.add(new Label("name", change.getTitle()));
				} else {
					item.add(new WebMarkupContainer("name").setVisible(false));
				}

				if (change.getChangeType() != ChangeType.ADD) {
					item.add(new Label("current", change.getRegulation()
							.getContents()).setEscapeModelStrings(false));
				} else {
					item.add(new WebMarkupContainer("current")
							.setVisible(false));
				}

				if (change.getChangeType() == ChangeType.REPEAL) {
					item.add(new WebMarkupContainer("body").setVisible(false));
				} else {
					item.add(new Label("body", change.getDescription())
							.setEscapeModelStrings(false));
				}

				RegulationChangeVote myVote = null;
				for (RegulationChangeVote vote : change.getVotes()) {
					if (vote.getSenator().equals(getUser())) {
						myVote = vote;
						break;
					}
				}

				if (myVote == null) {
					item.add(new Label("whatisay",
							"You have not yet voted on this change!"));
				} else {
					if (myVote.isInFavor()) {
						item.add(new Label("whatisay",
								"You have voted in favor of this change"));
					} else {
						item.add(new Label("whatisay",
								"You have voted against this change"));
					}
				}

				item.add(new WebMarkupContainer("veto").setVisible(change
						.isVeto()));

				item.add(new IconLink.Builder("images/icons/tick.png",
						new DefaultClickResponder<RegulationChange>(ModelMaker
								.wrap(change)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private DemocracyService democracyService;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								democracyService.voteForRegulation(getUser(),
										getModelObject(), true);
							}
						}).setText(
						"Yes, I want to "
								+ change.getChangeType().toString()
										.toLowerCase() + " this regulation")
						.newInstance("yes"));

				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<RegulationChange>(ModelMaker
								.wrap(change)) {
							private static final long serialVersionUID = 1L;

							@SpringBean
							private DemocracyService democracyService;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								democracyService.voteForRegulation(getUser(),
										getModelObject(), false);
							}

						}).setText(
						"No, I do not want to "
								+ change.getChangeType().toString()
										.toLowerCase() + " this regulation")
						.newInstance("no"));

			}

		});

		add(accordion);
	}

}
