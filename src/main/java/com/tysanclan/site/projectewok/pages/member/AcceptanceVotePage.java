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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.beans.ForumService;
import com.tysanclan.site.projectewok.components.AutoThreadLink;
import com.tysanclan.site.projectewok.components.DateTimeLabel;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.AcceptanceVote;
import com.tysanclan.site.projectewok.entities.AcceptanceVoteVerdict;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.AcceptanceVoteDAO;
import com.tysanclan.site.projectewok.entities.dao.ForumPostDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.ForumPostFilter;
import com.tysanclan.site.projectewok.pages.MemberPage;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.CHANCELLOR, Rank.SENATOR, Rank.TRUTHSAYER,
		Rank.REVERED_MEMBER, Rank.SENIOR_MEMBER, Rank.FULL_MEMBER,
		Rank.JUNIOR_MEMBER })
public class AcceptanceVotePage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private AcceptanceVoteDAO acceptanceVoteDAO;

	/**
	 * 
	 */
	public AcceptanceVotePage() {
		super("Acceptance votes");

		add(new ListView<AcceptanceVote>("members",
				ModelMaker.wrap(acceptanceVoteDAO.findAll())) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private ForumPostDAO forumPostDAO;

			@SpringBean
			private ForumService forumService;

			@Override
			protected void populateItem(ListItem<AcceptanceVote> item) {
				AcceptanceVote vote = item.getModelObject();
				User trialMember = vote.getTrialMember();

				AcceptanceVoteVerdict myVote = null;
				AcceptanceVoteVerdict mentorVote = null;

				for (AcceptanceVoteVerdict verdict : vote.getVerdicts()) {
					if (verdict.getCaster().equals(
							verdict.getVote().getTrialMember().getMentor())) {
						mentorVote = verdict;
					}

					if (verdict.getCaster().equals(getUser())) {
						myVote = verdict;
					}

				}

				item.add(new Label("username", trialMember.getUsername()));
				item.add(new ContextImage("ihaznotvoted",
						"images/icons/error.png").setVisible(myVote == null));

				PageParameters params = new PageParameters();
				params.add("userid", trialMember.getId().toString());
				BookmarkablePageLink<User> profileLink = new BookmarkablePageLink<User>(
						"profile", MemberPage.class, params);

				WebMarkupContainer mentorWarning = new WebMarkupContainer(
						"mentorWarning");
				User mentor = vote.getTrialMember().getMentor();
				if (mentor != null) {
					mentorWarning.add(new MemberListItem("mentor", mentor));
					mentorWarning.add(new ContextImage("warningIcon1",
							"images/icons/exclamation.png"));
					mentorWarning.add(new ContextImage("warningIcon2",
							"images/icons/exclamation.png"));
					mentorWarning.add(new ContextImage("warningIcon3",
							"images/icons/exclamation.png"));
					mentorWarning.add(new ContextImage("warningIcon4",
							"images/icons/exclamation.png"));
					mentorWarning.add(new ContextImage("warningIcon5",
							"images/icons/exclamation.png"));
					mentorWarning.add(new ContextImage("warningIcon6",
							"images/icons/exclamation.png"));

				} else {
					mentorWarning.add(new WebMarkupContainer("mentor")
							.setVisible(false));
					mentorWarning.add(new WebMarkupContainer("warningIcon1")
							.setVisible(false));
					mentorWarning.add(new WebMarkupContainer("warningIcon2")
							.setVisible(false));
					mentorWarning.add(new WebMarkupContainer("warningIcon3")
							.setVisible(false));
					mentorWarning.add(new WebMarkupContainer("warningIcon4")
							.setVisible(false));
					mentorWarning.add(new WebMarkupContainer("warningIcon5")
							.setVisible(false));
					mentorWarning.add(new WebMarkupContainer("warningIcon6")
							.setVisible(false));
				}

				mentorWarning.setVisible(mentorVote != null
						&& !mentorVote.isInFavor());

				item.add(mentorWarning);

				profileLink
						.add(new Label("username", trialMember.getUsername()));

				item.add(profileLink);

				item.add(new IconLink.Builder("images/icons/tick.png",
						new DefaultClickResponder<AcceptanceVote>(ModelMaker
								.wrap(vote)) {

							private static final long serialVersionUID = 1L;

							@SpringBean
							private DemocracyService democracyService;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								AcceptanceVote av = getModelObject();
								User caster = getUser();

								democracyService.castAcceptanceVote(av, caster,
										true);

								setResponsePage(new AcceptanceVotePage());
							}

						}).setText(
						"Yes, I want to accept " + trialMember.getUsername()
								+ " as a member").newInstance("yes"));

				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<AcceptanceVote>(ModelMaker
								.wrap(vote)) {

							private static final long serialVersionUID = 1L;

							@SpringBean
							private DemocracyService democracyService;

							/**
							 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
							 */
							@Override
							public void onClick() {
								AcceptanceVote av = getModelObject();
								User caster = getUser();

								democracyService.castAcceptanceVote(av, caster,
										false);

								setResponsePage(new AcceptanceVotePage());
							}

						})
						.setText(
								"No, I would prefer it if  "
										+ trialMember.getUsername()
										+ " would no longer remain a member"
										+ (getUser().equals(mentor) ? ". WARNING: AS MENTOR OF THIS APPLICANT YOUR VOTE WILL BE VISIBLE IF YOU VOTE AGAINST YOUR OWN PUPIL"
												: "")).newInstance("no"));

				item.add(new Label("count", new Model<Integer>(vote
						.getVerdicts().size())));

				ForumPostFilter filter = new ForumPostFilter();
				filter.setShadow(false);
				filter.setUser(trialMember);
				filter.addOrderBy("time", false);

				List<ForumPost> posts = forumPostDAO.findByFilter(filter);

				posts = forumService.filterPosts(getUser(), false, posts);

				List<ForumPost> topPosts = new LinkedList<ForumPost>();
				for (int i = 0; i < Math.min(posts.size(), 5); i++) {
					topPosts.add(posts.get(i));
				}

				ListView<ForumPost> lastPosts = new ListView<ForumPost>(
						"lastposts", ModelMaker.wrap(topPosts)) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(ListItem<ForumPost> item1) {
						ForumPost post = item1.getModelObject();

						item1.add(new AutoThreadLink("thread", post.getThread()));

						item1.add(new DateTimeLabel("time", post.getTime()));
					}

				};

				item.add(lastPosts.setVisible(!topPosts.isEmpty()));

				item.add(new WebMarkupContainer("noPosts")
						.setVisible(!lastPosts.isVisible()));

				WebMarkupContainer vacation = new WebMarkupContainer("vacation");
				vacation.add(new Label("username", trialMember.getUsername()));

				vacation.setVisible(trialMember.isVacation());

				item.add(vacation);

				String text = "You have not yet voted";

				if (myVote != null) {
					if (myVote.isInFavor()) {
						text = "You have voted in favor of "
								+ trialMember.getUsername();
					} else {
						text = "You have voted against "
								+ trialMember.getUsername();
					}
				}

				item.add(new Label("current", text));

			}

		});

	}
}
