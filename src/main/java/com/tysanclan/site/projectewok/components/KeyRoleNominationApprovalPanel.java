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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.RoleTransfer;
import com.tysanclan.site.projectewok.entities.RoleTransferApproval;
import com.tysanclan.site.projectewok.pages.member.senate.KeyRoleNominationApprovalPage;

public class KeyRoleNominationApprovalPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private IModel<RoleTransfer> transferModel;

	@SpringBean
	private RoleService roleService;

	public KeyRoleNominationApprovalPanel(String id, RoleTransfer transfer) {
		super(id);

		this.transferModel = ModelMaker.wrap(transfer);

		RoleTransferApproval app = null;
		if (transfer != null) {
			for (RoleTransferApproval a : transfer.getApprovedBy()) {
				if (a.getApprovedBy().equals(TysanSession.get().getUser())) {
					app = a;
					break;
				}
			}
		}

		if (transfer == null || !transfer.isAccepted() || app != null)
			setVisible(false);

		if (transfer != null && app == null) {
			add(new MemberListItem("user", transfer.getCandidate()));
			add(new Label("role", transfer.getRoleType().toString()));
			add(new Label("role2", transfer.getRoleType().toString()));

			add(new IconLink.Builder("images/icons/tick.png",
					new DefaultClickResponder<Void>() {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							roleService.approveCandidate(transferModel
									.getObject(), TysanSession.get().getUser());

							setResponsePage(new KeyRoleNominationApprovalPage());
						}
					}).setText(
					"Yes, I think " + transfer.getCandidate().getUsername()
							+ " would be a suitable "
							+ transfer.getRoleType().toString()).newInstance(
					"yes"));
			add(new IconLink.Builder("images/icons/cross.png",
					new DefaultClickResponder<Void>() {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							roleService.objectToTransfer(TysanSession.get()
									.getUser(), transferModel.getObject());

							setResponsePage(new KeyRoleNominationApprovalPage());
						}
					}).setText(
					"No, I disagree with this nomination, denying this position to "
							+ transfer.getCandidate().getUsername())
					.newInstance("no"));
		} else {
			add(new WebMarkupContainer("no"));
			add(new WebMarkupContainer("role"));
			add(new WebMarkupContainer("role2"));
			add(new WebMarkupContainer("user"));
			add(new WebMarkupContainer("yes"));
		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();

		transferModel.detach();
	}
}
