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

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.components.BugListPanel;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.BugStatus;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;
import com.tysanclan.site.projectewok.entities.dao.filters.BugFilter;

@TysanMemberSecured
public class FeatureOverviewPage extends AbstractMemberPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
	private BugTrackerService bugTrackerService;

	public FeatureOverviewPage() {
		super("Feature Requests");

		BugFilter filter = new BugFilter();

		filter.addReportType(ReportType.FEATUREREQUEST);
		filter.addAllowedStatus(BugStatus.NEW);
		filter.addAllowedStatus(BugStatus.ACKNOWLEDGED);
		filter.addOrderBy("status", true);
		filter.addOrderBy("updated", false);
		filter.addOrderBy("reported", false);

		add(new BugListPanel("features", "Feature", filter) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Bug bug) {
				setResponsePage(new ViewBugPage(bug));
			}
		});

		filter = new BugFilter();
		filter.addReportType(ReportType.FEATUREREQUEST);
		filter.addAllowedStatus(BugStatus.RESOLVED);
		filter.addAllowedStatus(BugStatus.CLOSED);
		filter.addOrderBy("status", false);
		filter.addOrderBy("updated", false);
		filter.addOrderBy("reported", false);

		add(new BugListPanel("rfeatures", "Feature", filter) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Bug bug) {
				setResponsePage(new ViewBugPage(bug));
			}
		});
	}
}