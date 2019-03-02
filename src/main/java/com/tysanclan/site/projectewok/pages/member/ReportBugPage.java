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

import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.components.CreateBugPanel;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ReportBugPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private BugTrackerService bugTrackerService;

	public ReportBugPage() {
		super("Report Bug");

		add(new CreateBugPanel("addBug", ReportType.BUGREPORT) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onFormSubmit(String title, String description) {
				bugTrackerService.reportBug(getUser(), title, description);

				setResponsePage(new BugOverviewPage());
			}
		});
	}
}
