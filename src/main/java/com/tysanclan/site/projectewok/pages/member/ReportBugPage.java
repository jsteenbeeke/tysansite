package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.components.CreateBugPanel;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;

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
