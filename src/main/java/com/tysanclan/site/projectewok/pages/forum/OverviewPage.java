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
package com.tysanclan.site.projectewok.pages.forum;

import org.apache.wicket.markup.html.WebMarkupContainer;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanNonMemberSecured;
import com.tysanclan.site.projectewok.components.EmailChangeConfirmationPanel;

@TysanNonMemberSecured
public class OverviewPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public OverviewPage() {
		super("User overview");

		add(new EmailChangeConfirmationPanel("emailChangeConfirmation",
				getUser()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onCancel() {
				setResponsePage(new OverviewPage());
			}

			@Override
			public void onConfirmed() {
				setResponsePage(new OverviewPage());
			}

		});

		add(new WebMarkupContainer("apply")
				.setVisible(getUser().getRank() == Rank.FORUM));

	}
}
