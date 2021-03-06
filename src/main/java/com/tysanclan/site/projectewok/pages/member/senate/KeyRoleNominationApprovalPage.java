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
package com.tysanclan.site.projectewok.pages.member.senate;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.KeyRoleNominationApprovalPanel;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import org.apache.wicket.spring.injection.annot.SpringBean;

@TysanRankSecured({ Rank.CHANCELLOR, Rank.SENATOR })
public class KeyRoleNominationApprovalPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	public KeyRoleNominationApprovalPage() {
		super("Key role nominations");

		add(new KeyRoleNominationApprovalPanel("herald",
				roleService.getCurrentTransfer(RoleType.HERALD)));
		add(new KeyRoleNominationApprovalPanel("steward",
				roleService.getCurrentTransfer(RoleType.STEWARD)));
		add(new KeyRoleNominationApprovalPanel("treasurer",
				roleService.getCurrentTransfer(RoleType.TREASURER)));

	}
}
