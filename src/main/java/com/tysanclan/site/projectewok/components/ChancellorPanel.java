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

import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.dao.*;
import com.tysanclan.site.projectewok.entities.filter.AchievementProposalFilter;
import com.tysanclan.site.projectewok.entities.filter.RoleTransferFilter;
import com.tysanclan.site.projectewok.pages.member.*;
import com.tysanclan.site.projectewok.pages.member.senate.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;

/**
 * @author Jeroen Steenbeeke
 */
public class ChancellorPanel extends TysanOverviewPanel<Void> {
	public class TruthsayerComplaintCondition
			implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			for (TruthsayerComplaint c : truthsayerComplaintDAO.findAll()) {
				if (!c.isMediated()) {
					if (c.isObserved()) {
						DateTime start = new DateTime(c.getStart());
						DateTime threeDaysAgo = new DateTime().minusDays(4);
						if (threeDaysAgo.isAfter(start)) {
							return AttentionType.WARNING;
						}
					} else {
						return AttentionType.WARNING;
					}
				}
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

	public class InactiveKeyRoleCondition
			implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			User herald = roleService.getHerald();
			User steward = roleService.getSteward();
			User treasurer = roleService.getTreasurer();

			if (herald != null && steward != null && treasurer != null) {
				return null;
			}

			RoleTransfer heraldTransfer = roleService
					.getCurrentTransfer(RoleType.HERALD);
			RoleTransfer treasurerTransfer = roleService
					.getCurrentTransfer(RoleType.TREASURER);
			RoleTransfer stewardTransfer = roleService
					.getCurrentTransfer(RoleType.STEWARD);

			if (heraldTransfer != null && stewardTransfer != null
					&& treasurerTransfer != null) {
				return null;
			}

			return AttentionType.ERROR;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class GroupRequestCondition implements IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			long requests = groupCreationRequestDAO.countAll();

			if (requests > 0) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			return null;
		}
	}

	public class PendingAchievementCondition
			implements IRequiresAttentionCondition {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			AchievementProposalFilter filter = new AchievementProposalFilter();
			filter.chancellorVeto().isNull();

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

	public class RegulationChangeCondition
			implements IRequiresAttentionCondition {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public RegulationChange getPendingChange() {
			for (RegulationChange change : regulationChangeDAO.findAll()) {
				if (!change.isVeto()) {
					return change;
				}
			}

			return null;
		}

		@Override
		public AttentionType requiresAttention() {
			RegulationChange change = getPendingChange();

			if (change != null) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			RegulationChange change = getPendingChange();

			if (change != null) {
				return change.getId();
			}

			return null;
		}
	}

	private static final long serialVersionUID = 1L;

	@SpringBean
	private GroupCreationRequestDAO groupCreationRequestDAO;

	@SpringBean
	private RegulationChangeDAO regulationChangeDAO;

	@SpringBean
	private AchievementProposalDAO achievementProposalDAO;

	@SpringBean
	private RoleTransferDAO roleTransferDAO;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private TruthsayerComplaintDAO truthsayerComplaintDAO;

	/**
	 *
	 */
	public ChancellorPanel(String id) {
		super(id, "Chancellor");

		addGroupRequestLink();

		addRolesLink();

		addMakeCommitteeLink();

		addNominateTruthsayerLink();

		addForumManagementLink();

		addStepdownLink();

		addRegulationLinks();

		addRealmsLink();

		addGamesLink();

		add(createConditionalVisibilityLink("regulations", VetoPage.class,
				"Pending Regulation changes", new RegulationChangeCondition()));

		add(createConditionalVisibilityLink("inactivekeyrole",
				InactiveKeyRoleTransferPage.class, "Assign key roles",
				new InactiveKeyRoleCondition()));

		add(createLink("complaints", ChancellorTruthsayerComplaintPage.class,
				"Truthsayer Complaints", new TruthsayerComplaintCondition()));

		add(createConditionalVisibilityLink("keyrolevote",
				KeyRoleNominationApprovalPage.class,
				"Approve key role nomination",
				new KeyRoleNominationCondition()));

		add(createConditionalVisibilityLink("pendingproposal",
				ChancellorAchievementPage.class,
				"Pending Achievement Proposals",
				new PendingAchievementCondition()));
	}

	private void addRealmsLink() {
		add(new Link<Void>("realms") {

			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new RealmManagementPage());

			}
		});
	}

	private void addGamesLink() {
		add(new Link<Void>("games") {

			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new GameManagementPage());

			}
		});
	}

	/**
	 *
	 */
	private void addRegulationLinks() {
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
	}

	/**
	 *
	 */
	private void addStepdownLink() {
		add(new Link<Void>("stepdown") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new ChancellorStepDownPage());
			}
		});

	}

	private void addRolesLink() {
		add(new Link<Void>("roles") {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new RolesManagementPage());
			}
		});
	}

	/**
	 *
	 */
	private void addNominateTruthsayerLink() {
		add(new Link<Void>("truthsayernomination") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TruthsayerNominationPage());
			}
		});

	}

	/**
	 *
	 */
	private void addForumManagementLink() {
		add(new Link<Void>("forummanagement") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new ForumManagementPage());
			}
		});

	}

	/**
	 *
	 */
	private void addMakeCommitteeLink() {
		add(new Link<Void>("committee") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new CommitteePage());
			}

		});
	}

	/**
	 *
	 */
	private void addGroupRequestLink() {
		add(createLink("grouprequest", GroupRequestApprovalPage.class,
				"Group Creation Request", new GroupRequestCondition()));
	}
}
