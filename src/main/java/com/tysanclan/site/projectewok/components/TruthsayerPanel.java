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

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.dao.*;
import com.tysanclan.site.projectewok.entities.filter.AchievementIconFilter;
import com.tysanclan.site.projectewok.entities.filter.AchievementProposalFilter;
import com.tysanclan.site.projectewok.entities.filter.TrialFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.pages.member.TruthsayerStepDownPage;
import com.tysanclan.site.projectewok.pages.member.justice.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class TruthsayerPanel extends TysanOverviewPanel<Void> {
	public class PendingAchievementProposalCondition
			implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			AchievementProposalFilter filter = new AchievementProposalFilter();

			filter.truthsayerReviewed(false);

			if (achievementProposalDAO.countByFilter(filter) > 0) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class PendingAchievementIconCondition
			implements IRequiresAttentionCondition {

		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			AchievementIconFilter filter = new AchievementIconFilter();

			filter.approved().isNull();

			if (achievementIconDAO.countByFilter(filter) > 0) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class PendingTrialCondition implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			TrialFilter filter = new TrialFilter();
			filter.trialThread().isNotNull();
			if (trialDAO.countByFilter(filter) > 0) {
				return AttentionType.ERROR;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class PendingAchievementRequestCondition
			implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			if (requestDAO.getNonGroupPendingAchievementRequests().size() > 0) {
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
	private UserDAO userDAO;

	@SpringBean
	private TrialDAO trialDAO;

	@SpringBean
	private AchievementIconDAO achievementIconDAO;

	@SpringBean
	private AchievementProposalDAO achievementProposalDAO;

	@SpringBean
	private AchievementRequestDAO requestDAO;

	public TruthsayerPanel(String id) {
		super(id, "Truthsayer");

		add(new Link<Void>("forumuserlink") {

			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new ForumUserManagementPage());

			}
		});

		UserFilter filter = new UserFilter();
		filter.rank(Rank.CHANCELLOR);

		add(new Link<Void>("impeachlink") {

			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new ImpeachmentInitiationPage());

			}
		}.setVisible(userDAO.countByFilter(filter) > 0));
		add(new Link<Void>("stepdown") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new TruthsayerStepDownPage());
			}
		});
		add(new Link<Void>("untenable") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new UntenabilityPage());

			}

		});
		add(new Link<Void>("truthsayerEditUserPage") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TruthSayerEditUserPage(null));
			}

		});

		add(createConditionalVisibilityLink("pendingtrial",
				TrialConfirmationPage.class, "Pending Trials",
				new PendingTrialCondition()));
		add(createConditionalVisibilityLink("pendingicon",
				AchievementIconApprovalPage.class, "Pending Achievement Icons",
				new PendingAchievementIconCondition()));
		add(createConditionalVisibilityLink("pendingproposal",
				TruthsayerAchievementProposalPage.class,
				"Pending Achievement Proposals",
				new PendingAchievementProposalCondition()));
		add(createConditionalVisibilityLink("pendingrequest",
				AchievementApprovalPage.class, "Pending Achievement Requests",
				new PendingAchievementRequestCondition()));
	}
}
