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
package com.tysanclan.site.projectewok.components;

import java.util.Calendar;
import java.util.List;

import io.vavr.collection.Seq;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.Committee;
import com.tysanclan.site.projectewok.entities.CompoundVote;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Group.JoinPolicy;
import com.tysanclan.site.projectewok.entities.GroupLeaderElection;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.AchievementRequestDAO;
import com.tysanclan.site.projectewok.entities.dao.GroupLeaderElectionDAO;
import com.tysanclan.site.projectewok.entities.filter.GroupLeaderElectionFilter;
import com.tysanclan.site.projectewok.pages.member.RunForGroupLeaderPage;
import com.tysanclan.site.projectewok.pages.member.group.AcceptGroupApplicationPage;
import com.tysanclan.site.projectewok.pages.member.group.DisbandGroupPage;
import com.tysanclan.site.projectewok.pages.member.group.EditGroupDescriptionPage;
import com.tysanclan.site.projectewok.pages.member.group.EditMOTDPage;
import com.tysanclan.site.projectewok.pages.member.group.GroupAchievementApprovalPage;
import com.tysanclan.site.projectewok.pages.member.group.GroupForumManagementPage;
import com.tysanclan.site.projectewok.pages.member.group.GroupJoinPolicyPage;
import com.tysanclan.site.projectewok.pages.member.group.GroupLeaderElectionPage;
import com.tysanclan.site.projectewok.pages.member.group.GroupMemberManagementPage;
import com.tysanclan.site.projectewok.pages.member.group.InviteGroupMemberPage;
import com.tysanclan.site.projectewok.pages.member.group.LeaveGroupPage;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class GroupOverviewPanel extends TysanOverviewPanel<Group> {
	public class GroupLeaderElectionCondition implements
			IRequiresAttentionCondition {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			boolean hasVoted = false;

			GroupLeaderElection election = getElection();

			if (election != null) {
				for (CompoundVote vote : election.getVotes()) {
					if (vote.getCaster().equals(getUser())) {
						hasVoted = true;
						break;
					}
				}
			}

			if (election != null && !election.isNominationOpen() && !hasVoted
					&& getUser().getRank() != Rank.TRIAL) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class RunForGroupLeaderCondition implements
			IRequiresAttentionCondition {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			GroupLeaderElection election = getElection();

			if (election != null && election.isNominationOpen()
					&& getUser().getRank() != Rank.TRIAL
					&& !election.getCandidates().contains(getUser())) {
				return AttentionType.INFO;

			}
			return null;
		}

		/**
		 * @return
		 */

		@Override
		public Long getDismissableId() {
			if (getElection() != null) {
				return getElection().getId();
			}
			return null;
		}
	}

	public class GroupAchievementRequestCondition implements
			IRequiresAttentionCondition {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			if (getUser().equals(getModelObject().getLeader())
					&& requestDAO.getPendingGroupRequests(getModelObject())
							.size() > 0) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class GroupApplicationCondition implements
			IRequiresAttentionCondition {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			Group g = getModelObject();

			if (!g.getAppliedMembers().isEmpty()
					&& getUser().equals(g.getLeader())
					&& g.getJoinPolicy() == JoinPolicy.APPLICATION) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	private static final long serialVersionUID = 1L;

	@SpringBean
	private GroupLeaderElectionDAO groupLeaderElectionDAO;

	@SpringBean
	private AchievementRequestDAO requestDAO;

	private IModel<GroupLeaderElection> electionModel = null;

	private GroupLeaderElection getElection() {
		if (electionModel == null) {
			GroupLeaderElection election = null;
			GroupLeaderElectionFilter filter = new GroupLeaderElectionFilter();
			filter.group(getModelObject());

			Calendar calendar = DateUtil.getCalendarInstance();
			calendar.add(Calendar.WEEK_OF_YEAR, -2);

			filter.start().greaterThan(calendar.getTime());

			Seq<GroupLeaderElection> elections = groupLeaderElectionDAO
					.findByFilter(filter);

			if (elections.size() > 0) {
				election = elections.get(0);
			}
			electionModel = ModelMaker.wrap(election);
		}

		return electionModel.getObject();
	}

	public GroupOverviewPanel(String id, User user, Group group) {
		super(id, ModelMaker.wrap(group), group.getName());

		add(new Link<Group>("editdescription", ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new EditGroupDescriptionPage(getModelObject()));

			}
		}.setVisible(user.equals(group.getLeader())));

		add(new IconLink.Builder("images/icons/page_white_edit.png",
				new DefaultClickResponder<Group>(ModelMaker.wrap(group)) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						setResponsePage(new EditMOTDPage(getModelObject()));
					}
				}).newInstance("editmotd").setVisible(
				user.equals(group.getLeader())));

		add(new BBCodePanel("motd", group.getMessageOfTheDay())
				.setVisible(group.getMessageOfTheDay() != null
						&& !group.getMessageOfTheDay().isEmpty()));

		add(new Link<Group>("invite", ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new InviteGroupMemberPage(getModelObject()));
			}
		}.setVisible(user.equals(group.getLeader())
				&& group.getJoinPolicy() == JoinPolicy.INVITATION));

		add(createConditionalVisibilityLink("applications",
				ModelMaker.wrap(group), AcceptGroupApplicationPage.class,
				"Join Applications", new GroupApplicationCondition()));

		Link<Group> forumLink = new Link<Group>("forums",
				ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new GroupForumManagementPage(getModelObject()));

			}

		};

		forumLink.setVisible(user.equals(group.getLeader()));

		add(forumLink);

		Link<Group> managementLink = new Link<Group>("management",
				ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new GroupMemberManagementPage(getModelObject()));
			}
		};

		managementLink.setVisible(getUser().equals(group.getLeader()));

		add(managementLink);

		Link<Group> setPolicyLink = new Link<Group>("joinpolicy",
				ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new GroupJoinPolicyPage(getModelObject()));

			}

		};

		setPolicyLink.setVisible(user.equals(group.getLeader())
				&& Hibernate.getClass(group) != Committee.class);

		add(setPolicyLink);

		Link<Group> disbandLink = new Link<Group>("disband",
				ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new DisbandGroupPage(getModelObject()));

			}

		};

		disbandLink.setVisible(user.equals(group.getLeader())
				&& Hibernate.getClass(group) != Committee.class);

		add(disbandLink);

		add(createConditionalVisibilityLink("runforgroupleader",
				ModelMaker.wrap(getElection()), RunForGroupLeaderPage.class,
				"Run for group leader", new RunForGroupLeaderCondition()));

		add(createConditionalVisibilityLink("pendingrequest",
				ModelMaker.wrap(group), GroupAchievementApprovalPage.class,
				"Pending Achievement Requests",
				new GroupAchievementRequestCondition()));

		add(createConditionalVisibilityLink("electgroupleader",
				ModelMaker.wrap(getElection()), GroupLeaderElectionPage.class,
				"Elect group leader", new GroupLeaderElectionCondition()));

		add(new Link<Group>("leave", ModelMaker.wrap(group)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new LeaveGroupPage(getModelObject()));
			}
		});

	}

	/**
	 * @see com.tysanclan.site.projectewok.components.TysanOverviewPanel#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		if (electionModel != null) {
			electionModel.detach();
		}
	}
}
