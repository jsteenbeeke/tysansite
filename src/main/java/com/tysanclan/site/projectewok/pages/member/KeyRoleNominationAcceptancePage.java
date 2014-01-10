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
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.RoleTransfer;

@TysanRankSecured({ Rank.FULL_MEMBER, Rank.SENIOR_MEMBER, Rank.REVERED_MEMBER,
		Rank.CHANCELLOR, Rank.TRUTHSAYER, Rank.SENATOR })
public class KeyRoleNominationAcceptancePage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	private IModel<RoleTransfer> transferModel;

	@SpringBean
	private RoleService roleService;

	public KeyRoleNominationAcceptancePage(RoleTransfer transfer) {
		super("Accept " + transfer.getRoleType().toString() + " nomination");

		this.transferModel = ModelMaker.wrap(transfer);

		add(new Label("name", transfer.getRoleType().toString()));

		add(new IconLink.Builder("images/icons/tick.png",
				new DefaultClickResponder<Void>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						roleService.acceptNomination(transferModel.getObject());

						setResponsePage(new OverviewPage());
					}
				}).setText("Yes, I wish to accept this nomination")
				.newInstance("yes"));
		add(new IconLink.Builder("images/icons/cross.png",
				new DefaultClickResponder<Void>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						roleService.rejectNomination(transferModel.getObject());

						setResponsePage(new OverviewPage());
					}
				})
				.setText(
						"No, I would rather have someone else take this position in my stead")
				.newInstance("no"));

	}

	@Override
	protected void onDetach() {
		super.onDetach();
		transferModel.detach();
	}
}
