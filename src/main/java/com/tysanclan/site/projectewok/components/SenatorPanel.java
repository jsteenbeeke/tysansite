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

import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.dao.*;
import com.tysanclan.site.projectewok.entities.filter.RoleTransferFilter;
import com.tysanclan.site.projectewok.entities.filter.TruthsayerNominationFilter;
import com.tysanclan.site.projectewok.pages.member.SenatorStepDownPage;
import com.tysanclan.site.projectewok.pages.member.justice.ImpeachmentPage;
import com.tysanclan.site.projectewok.pages.member.senate.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
public class SenatorPanel extends TysanOverviewPanel<Void> {
	public class TruthsayerComplaintCondition
			implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		private TruthsayerComplaint getComplaint() {
			for (TruthsayerComplaint c : truthsayerComplaintDAO.findAll()) {
				if (!c.hasVoted(getUser())) {
					return c;
				}
			}

			return null;
		}

		@Override
		public AttentionType requiresAttention() {
			TruthsayerComplaint c = getComplaint();

			if (c != null) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class KeyRoleNominationCondition
			implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		private RoleTransfer getActiveTransfer() {
			RoleTransferFilter filter = new RoleTransferFilter();
			filter.accepted(true);

			outer:
			for (RoleTransfer transfer : roleTransferDAO.findByFilter(filter)) {
				for (RoleTransferApproval app : transfer.getApprovedBy()) {
					if (app.getApprovedBy().equals(getUser()))
						continue outer;
				}

				return transfer;
			}

			return null;
		}

		@Override
		public AttentionType requiresAttention() {
			RoleTransfer transfer = getActiveTransfer();

			if (transfer != null)
				return AttentionType.ERROR;

			return null;
		}

		@Override
		public Long getDismissableId() {
			RoleTransfer transfer = getActiveTransfer();

			if (transfer != null)
				return transfer.getId();

			return null;
		}
	}

	public class TruthsayerNominatedCondition
			implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			TruthsayerNominationFilter filter = new TruthsayerNominationFilter();
			filter.voteStart().isNotNull();

			for (TruthsayerNomination nomination : truthsayerNominationDAO
					.findByFilter(filter)) {
				boolean found = false;
				for (TruthsayerNominationVote vote : nomination.getVotes()) {
					if (!found && vote.getSenator().equals(getUser())) {
						found = true;
					}
				}

				if (!found) {
					return AttentionType.WARNING;
				}
			}
			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class RegulationChangeCondition
			implements IRequiresAttentionCondition {

		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			for (RegulationChange regulationChange : regulationChangeDAO
					.findAll()) {
				boolean found = false;
				for (RegulationChangeVote vote : regulationChange.getVotes()) {
					if (!found && vote.getSenator().equals(getUser())) {
						found = true;
					}
				}

				if (!found) {
					return AttentionType.WARNING;
				}
			}
			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class AchievementProposalCondition
			implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			for (AchievementProposal proposal : achievementProposalDAO
					.findAll()) {
				boolean found = false;
				for (AchievementProposalVote vote : proposal.getApprovedBy()) {
					if (!found && vote.getSenator().equals(getUser())) {
						found = true;
					}
				}

				if (!found) {
					return AttentionType.WARNING;
				}
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class ImpeachmentCondition implements IRequiresAttentionCondition {

		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			for (Impeachment impeachment : impeachmentDAO.findAll()) {
				boolean found = false;
				for (ImpeachmentVote vote : impeachment.getVotes()) {
					if (!found && vote.getCaster().equals(getUser())) {
						found = true;
					}
				}

				if (!found) {
					return AttentionType.WARNING;
				}
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
	private TruthsayerNominationDAO truthsayerNominationDAO;

	@SpringBean
	private RegulationChangeDAO regulationChangeDAO;

	@SpringBean
	private ImpeachmentDAO impeachmentDAO;

	@SpringBean
	private AchievementProposalDAO achievementProposalDAO;

	@SpringBean
	private RoleTransferDAO roleTransferDAO;

	@SpringBean
	private TruthsayerComplaintDAO truthsayerComplaintDAO;

	public SenatorPanel(String id) {
		super(id, "Senator");

		add(createLink("truthsayers", TruthsayerVotePage.class,
				"Truthsayer Nomination", new TruthsayerNominatedCondition()));
		add(createLink("complaints", SenateTruthsayerComplaintPage.class,
				"Truthsayer Complaints", new TruthsayerComplaintCondition()));

		add(createLink("regulations", RegulationModificationPage.class,
				"Pending Regulation changes", new RegulationChangeCondition()));

		add(new Link<Void>("addregulation") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new AddRegulationPage());

			}

		});

		add(new Link<Void>("modifyregulation") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ModifyRegulationPage());

			}

		});

		add(new Link<Void>("repealregulation") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RepealRegulationPage());

			}

		});

		add(createConditionalVisibilityLink("impeachment",
				ImpeachmentPage.class, "Impeachment",
				new ImpeachmentCondition()));

		add(createConditionalVisibilityLink("achievement",
				AchievementProposalApprovalPage.class, "Achievement Proposals",
				new AchievementProposalCondition()));

		add(createConditionalVisibilityLink("keyrolevote",
				KeyRoleNominationApprovalPage.class,
				"Approve key role nomination",
				new KeyRoleNominationCondition()));

		add(new Link<Void>("stepdown") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SenatorStepDownPage());

			}

		});

	}
}
