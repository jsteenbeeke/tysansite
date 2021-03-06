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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.util.HashException;
import com.tysanclan.rest.api.util.HashUtil;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.components.OtterSniperPanel;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.dao.ChancellorElectionDAO;
import com.tysanclan.site.projectewok.entities.dao.ElectionDAO;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;
import com.tysanclan.site.projectewok.entities.filter.ElectionFilter;
import com.tysanclan.site.projectewok.util.DateUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jeroen Steenbeeke
 */
public class PastElectionsPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private ElectionDAO electionDAO;

	@SpringBean
	private ChancellorElectionDAO chancellorElectionDAO;

	@SpringBean
	private SenateElectionDAO senateElectionDAO;

	/**
	 *
	 */
	public PastElectionsPage() {
		super("Past Elections");

		add(new OtterSniperPanel("otterSniperPanel", 3));

		Calendar calendar = DateUtil.getCalendarInstance();
		calendar.add(Calendar.WEEK_OF_YEAR, -2);
		calendar.add(Calendar.DAY_OF_YEAR, -1);

		ElectionFilter filter = new ElectionFilter();
		filter.start().lessThan(calendar.getTime());
		filter.start().orderBy(false);

		List<Election> elections = new LinkedList<Election>();

		for (Election e : electionDAO.findByFilter(filter)) {
			if (Hibernate.getClass(e) != GroupLeaderElection.class) {
				elections.add(e);
			}
		}

		add(new ListView<Election>("elections", ModelMaker.wrap(elections)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Election> item) {
				Election election = item.getModelObject();

				final Map<Long, Integer> totals = new HashMap<Long, Integer>();

				DateFormat format = new SimpleDateFormat("EEEE d MMMM yyyy",
						Locale.US);

				List<User> candidates = new LinkedList<>(
						election.getCandidates());

				candidates.sort(new Comparator<User>() {
					/**
					 * @see Comparator#compare(Object,
					 *      Object)
					 */
					@Override
					public int compare(User o1, User o2) {
						return o1.getUsername()
								.compareToIgnoreCase(o2.getUsername());
					}
				});

				Calendar cal = DateUtil.getCalendarInstance();
				cal.setTime(election.getStart());
				cal.add(Calendar.WEEK_OF_YEAR, 2);
				cal.add(Calendar.DAY_OF_MONTH, 1);

				String type = "Unknown";
				String winnersLabel = "Winner:";

				Class<?> electionClass = Hibernate.getClass(election);
				List<User> winners = new LinkedList<User>();

				if (electionClass == ChancellorElection.class) {
					ChancellorElection ce = chancellorElectionDAO
							.load(election.getId())
							.getOrElseThrow(IllegalStateException::new);
					type = "Chancellor";
					if (ce.getWinner() != null) {
						winners.add(ce.getWinner());
					}
				} else if (electionClass == SenateElection.class) {
					type = "Senate";
					winnersLabel = "Winners:";
					SenateElection se = senateElectionDAO.load(election.getId())
							.getOrElseThrow(IllegalStateException::new);
					winners.addAll(se.getWinners());
				}

				List<CompoundVote> votes = new LinkedList<CompoundVote>();

				for (CompoundVote v : election.getVotes()) {
					for (CompoundVoteChoice cvc : v.getChoices()) {
						Long userid = cvc.getVotesFor().getId();
						int score = cvc.getScore();

						if (totals.containsKey(userid)) {
							totals.put(userid, totals.get(userid) + score);
						} else {
							totals.put(userid, score);
						}

					}
					votes.add(v);
				}

				votes.sort((o1, o2) -> o1.getCaster().getUsername()
						.compareToIgnoreCase(o2.getCaster().getUsername()));

				item.add(new Label("title",
						type + " election of " + format.format(cal.getTime())));
				item.add(new Label("winnerslabel", winnersLabel));
				item.add(new ListView<User>("winners",
						ModelMaker.wrap(winners)) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
					 */
					@Override
					protected void populateItem(ListItem<User> innerItem) {
						innerItem.add(new MemberListItem("winner",
								innerItem.getModelObject()));
					}
				});

				item.add(new WebMarkupContainer("candidatesheader")
						.add(AttributeModifier.replace("colspan",
								Integer.toString(
										election.getCandidates().size()))));

				item.add(new ListView<CompoundVote>("votes",
						ModelMaker.wrap(votes)) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(
							ListItem<CompoundVote> innerItem) {
						CompoundVote vote = innerItem.getModelObject();

						if (vote.getCaster().equals(getUser())) {
							innerItem.add(new Label("userdescriptor",
									getUser().getUsername()));
						} else {
							innerItem.add(new Label("userdescriptor",
									getScrambledUsername(vote))
									.setEscapeModelStrings(false));
						}

						List<CompoundVoteChoice> scores = new LinkedList<CompoundVoteChoice>();
						scores.addAll(vote.getChoices());
						Collections.sort(scores,
								new Comparator<CompoundVoteChoice>() {
									/**
									 * @see java.util.Comparator#compare(java.lang.Object,
									 *      java.lang.Object)
									 */
									@Override
									public int compare(CompoundVoteChoice o1,
											CompoundVoteChoice o2) {
										return o1.getVotesFor().getUsername()
												.compareToIgnoreCase(
														o2.getVotesFor()
																.getUsername());
									}
								});

						innerItem.add(new ListView<CompoundVoteChoice>("scores",
								ModelMaker.wrap(scores)) {

							private static final long serialVersionUID = 1L;

							@Override
							protected void populateItem(
									ListItem<CompoundVoteChoice> innerInnerItem) {
								CompoundVoteChoice choice = innerInnerItem
										.getModelObject();

								innerInnerItem.add(new Label("score",
										new Model<Integer>(choice.getScore())));

							}

						});
					}

				});

				item.add(new ListView<User>("candidates",
						ModelMaker.wrap(candidates)) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
					 */
					@Override
					protected void populateItem(ListItem<User> innerItem) {
						innerItem.add(new MemberListItem("candidate",
								innerItem.getModelObject()));

					}
				});

				item.add(new ListView<User>("totals",
						ModelMaker.wrap(candidates)) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
					 */
					@Override
					protected void populateItem(ListItem<User> innerItem) {
						User user = innerItem.getModelObject();
						innerItem.add(new Label("total",
								new Model<Integer>(totals.get(user.getId()))));
					}
				});

			}

		});

	}

	private String getScrambledUsername(CompoundVote vote) {
		String prehash =
				vote.getCaster().getUsername() + vote.getElection().getStart()
						.getTime() + vote.getId();

		int hashHash = getHashHash(prehash);

		return "<i>" + Integer.toString(Math.abs(hashHash)) + "</i>";
	}

	/**
	 * @param prehash
	 * @return
	 */
	private int getHashHash(String prehash) {
		try {
			String hash = HashUtil.sha1Hash(prehash);
			int hashHash = hash.hashCode();
			return hashHash;
		} catch (HashException e) {
			return -1; // Still not identifiable
		}

	}
}
