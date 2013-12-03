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
package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.events.IEventDispatcher;
import com.tysanclan.site.projectewok.beans.MembershipService;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.event.MemberStatusEvent;
import com.tysanclan.site.projectewok.pages.forum.OverviewPage;

/**
 * @author Jeroen Steenbeeke
 */
public class ConfirmMembershipTerminationPage extends
		AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	public ConfirmMembershipTerminationPage() {
		super("Terminate Membership");

		getAccordion().add(new Form<User>("terminate") {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private MembershipService membershipService;

			@SpringBean
			private IEventDispatcher dispatcher;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				membershipService.terminateMembership(getUser());

				dispatcher
						.dispatchEvent(new MemberStatusEvent(
								com.tysanclan.site.projectewok.entities.MembershipStatusChange.ChangeType.LEFT_VOLUNTARILY,
								getUser()));

				setResponsePage(new OverviewPage());
			}
		});
	}
}
