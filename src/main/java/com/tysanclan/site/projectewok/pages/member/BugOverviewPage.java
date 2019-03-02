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

import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.components.BugListPanel;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.BugStatus;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;
import com.tysanclan.site.projectewok.entities.filter.BugFilter;
import org.apache.wicket.spring.injection.annot.SpringBean;

@TysanMemberSecured
public class BugOverviewPage extends AbstractMemberPage {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
	private BugTrackerService bugTrackerService;

	public BugOverviewPage() {
		super("Bugs Overview");

		BugFilter filter = new BugFilter();
		filter.reportType(ReportType.BUGREPORT);
		filter.orReportType(ReportType.CRASHREPORT);
		filter.status(BugStatus.NEW);
		filter.orStatus(BugStatus.ACKNOWLEDGED);

		filter.status().orderBy(true);
		filter.updated().orderBy(false);
		filter.reported().orderBy(false);

		add(new BugListPanel("bugs", "Bug", filter) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Bug bug) {
				setResponsePage(new ViewBugPage(bug));
			}
		});

		filter = new BugFilter();
		filter.reportType(ReportType.BUGREPORT);
		filter.orReportType(ReportType.CRASHREPORT);
		filter.status(BugStatus.RESOLVED);
		filter.orStatus(BugStatus.CLOSED);

		filter.status().orderBy(false);
		filter.updated().orderBy(false);
		filter.reported().orderBy(false);

		add(new BugListPanel("rbugs", "Bug", filter) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Bug bug) {
				setResponsePage(new ViewBugPage(bug));
			}
		});
	}
}
