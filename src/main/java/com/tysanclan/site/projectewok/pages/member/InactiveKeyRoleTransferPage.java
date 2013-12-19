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

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.InactiveKeyRoleTransferPanel;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.RoleTransfer;
import com.tysanclan.site.projectewok.entities.User;

@TysanRankSecured(Rank.CHANCELLOR)
public class InactiveKeyRoleTransferPage extends AbstractMemberPage {

	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	public InactiveKeyRoleTransferPage() {
		super("Assign Key Roles");
		User herald = roleService.getHerald();
		User steward = roleService.getSteward();
		User treasurer = roleService.getTreasurer();

		if (herald != null && steward != null && treasurer != null) {
			throw new RestartResponseAtInterceptPageException(
					OverviewPage.class);
		}

		RoleTransfer heraldTransfer = roleService
				.getCurrentTransfer(RoleType.HERALD);
		RoleTransfer treasurerTransfer = roleService
				.getCurrentTransfer(RoleType.TREASURER);
		RoleTransfer stewardTransfer = roleService
				.getCurrentTransfer(RoleType.STEWARD);

		if (heraldTransfer != null && stewardTransfer != null
				&& treasurerTransfer != null) {
			throw new RestartResponseAtInterceptPageException(
					OverviewPage.class);
		}

		add(new InactiveKeyRoleTransferPanel("herald", RoleType.HERALD)
				.setVisible(herald == null && heraldTransfer == null));
		add(new InactiveKeyRoleTransferPanel("steward", RoleType.STEWARD)
				.setVisible(steward == null && stewardTransfer == null));
		add(new InactiveKeyRoleTransferPanel("treasurer", RoleType.TREASURER)
				.setVisible(treasurer == null && treasurerTransfer == null));

	}

}
