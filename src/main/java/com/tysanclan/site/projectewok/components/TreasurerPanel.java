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

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.PaymentRequestDAO;
import com.tysanclan.site.projectewok.pages.member.PaypalSettingsPage;
import com.tysanclan.site.projectewok.pages.member.admin.CaretakerFinancePage;
import com.tysanclan.site.projectewok.pages.member.admin.PaymentRequestApprovalPage;
import com.tysanclan.site.projectewok.pages.member.admin.TreasurerTransferPage;

public class TreasurerPanel extends TysanOverviewPanel<User> {
	public class PendingPaymentRequestCondition implements
			IRequiresAttentionCondition {
		private static final long serialVersionUID = 1L;

		@Override
		public AttentionType requiresAttention() {
			if (requestDAO.countAll() > 0) {
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
	private PaymentRequestDAO requestDAO;

	@SpringBean
	private RoleService roleService;

	public TreasurerPanel(String id, User user) {
		super(id, "Treasurer");

		setVisible(user.equals(roleService.getTreasurer()));

		add(new Link<User>("finances", ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			/**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
			@Override
			public void onClick() {
				setResponsePage(new CaretakerFinancePage(getModelObject()));
			}
		});

		add(createConditionalVisibilityLink("paymentRequests",
				PaymentRequestApprovalPage.class, "Payment requests",
				new PendingPaymentRequestCondition()));

		add(createLink("paypal", PaypalSettingsPage.class, "PayPal settings",
				new DoesNotHavePaypalCondition(user)));
		add(new Link<User>("transfer", ModelMaker.wrap(user)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new TreasurerTransferPage());
			}

		});
	}
}
