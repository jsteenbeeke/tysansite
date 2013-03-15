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

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.tabs.Tabs;

import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.components.BugListPanel;
import com.tysanclan.site.projectewok.components.CreateBugPanel;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.BugStatus;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;
import com.tysanclan.site.projectewok.entities.dao.filters.BugFilter;

@TysanMemberSecured
public class BugOverviewPage extends AbstractMemberPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String FEATURE_MODE = "requestfeature";

	private static final String BUG_MODE = "reportbug";

	@SpringBean
	private BugTrackerService bugTrackerService;

	public BugOverviewPage(PageParameters params) {
		this(params.get("mode").toOptionalString());
	}

	public BugOverviewPage() {
		this((String) null);
	}

	public BugOverviewPage(String mode) {
		super("Bugs and Feature Requests");

		Tabs tabs = new Tabs("tabs");

		if (mode != null) {
			if (FEATURE_MODE.equals(mode)) {
				tabs.setDefaultSelectedTabIndex(3);
			} else if (BUG_MODE.equals(mode)) {
				tabs.setDefaultSelectedTabIndex(2);
			}
		}

		BugFilter filter = new BugFilter();
		filter.addReportType(ReportType.BUGREPORT);
		filter.addReportType(ReportType.CRASHREPORT);
		filter.addAllowedStatus(BugStatus.NEW);
		filter.addAllowedStatus(BugStatus.ACKNOWLEDGED);

		filter.addOrderBy("status", true);
		filter.addOrderBy("updated", false);
		filter.addOrderBy("reported", false);

		tabs.add(new BugListPanel("bugs", "Bug", filter) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Bug bug) {
				setResponsePage(new ViewBugPage(bug));
			}
		});

		filter = new BugFilter();
		filter.addReportType(ReportType.FEATUREREQUEST);
		filter.addAllowedStatus(BugStatus.NEW);
		filter.addAllowedStatus(BugStatus.ACKNOWLEDGED);
		filter.addOrderBy("status", true);
		filter.addOrderBy("updated", false);
		filter.addOrderBy("reported", false);

		tabs.add(new BugListPanel("features", "Feature", filter) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Bug bug) {
				setResponsePage(new ViewBugPage(bug));
			}
		});

		tabs.add(new CreateBugPanel("addBug", ReportType.BUGREPORT) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onFormSubmit(String title, String description) {
				bugTrackerService.reportBug(getUser(), title, description);

				setResponsePage(new BugOverviewPage());
			}
		});

		tabs.add(new CreateBugPanel("addFeature", ReportType.FEATUREREQUEST) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onFormSubmit(String title, String description) {
				bugTrackerService.requestFeature(getUser(), title, description);

				setResponsePage(new BugOverviewPage());
			}
		});

		filter = new BugFilter();
		filter.addReportType(ReportType.BUGREPORT);
		filter.addReportType(ReportType.CRASHREPORT);
		filter.addAllowedStatus(BugStatus.RESOLVED);
		filter.addAllowedStatus(BugStatus.CLOSED);

		filter.addOrderBy("status", false);
		filter.addOrderBy("updated", false);
		filter.addOrderBy("reported", false);

		tabs.add(new BugListPanel("rbugs", "Bug", filter) {
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

		tabs.add(new BugListPanel("rfeatures", "Feature", filter) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Bug bug) {
				setResponsePage(new ViewBugPage(bug));
			}
		});

		add(tabs);

	}
}
