package com.tysanclan.site.projectewok.pages.member;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.components.CreateBugPanel;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;

public class RequestFeaturePage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private BugTrackerService bugTrackerService;

	public RequestFeaturePage() {
		super("Request Feature");

		add(new CreateBugPanel("addFeature", ReportType.FEATUREREQUEST) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onFormSubmit(String title, String description) {
				bugTrackerService.requestFeature(getUser(), title, description);

				setResponsePage(new BugOverviewPage());
			}
		});
	}
}
