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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.webcomponents.core.form.choice.NaiveRenderer;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.BugTrackerService;
import com.tysanclan.site.projectewok.components.DateLabel;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.Bug;
import com.tysanclan.site.projectewok.entities.Bug.BugStatus;
import com.tysanclan.site.projectewok.entities.Bug.ReportType;
import com.tysanclan.site.projectewok.entities.BugComment;
import com.tysanclan.site.projectewok.entities.dao.BugDAO;
import com.tysanclan.site.projectewok.entities.filter.BugFilter;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@TysanMemberSecured
public class ViewBugPage extends AbstractSingleAccordionMemberPage {
	private static final long serialVersionUID = 1L;

	public static class ViewBugPageParams {
		private final long id;

		public ViewBugPageParams(Long id) {
			super();
			this.id = id;
		}

		public long getId() {
			return id;
		}
	}

	public static class BugRenderer
			implements IChoiceRenderer<Bug>, NaiveRenderer<Bug> {
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(Bug object) {
			return object.getId() + ": " + object.getTitle();
		}

		@Override
		public String getIdValue(Bug object, int index) {

			return Long.toString(object.getId());
		}
	}

	public class AcceptResponder extends DefaultClickResponder<Bug> {
		private static final long serialVersionUID = 1L;

		public AcceptResponder(Bug bug) {
			super(ModelMaker.wrap(bug));
		}

		@Override
		public void onClick() {
			tracker.assignBug(getModelObject(), getUser());

			setResponsePage(new ViewBugPage(getModelObject()));
		}

	}

	public class CloseResponder extends DefaultClickResponder<Bug> {
		private static final long serialVersionUID = 1L;

		public CloseResponder(Bug bug) {
			super(ModelMaker.wrap(bug));
		}

		@Override
		public void onClick() {
			tracker.closeBug(getModelObject());

			setResponsePage(new ViewBugPage(getModelObject()));
		}
	}

	public class ReopenResponder extends DefaultClickResponder<Bug> {
		private static final long serialVersionUID = 1L;

		public ReopenResponder(Bug bug) {
			super(ModelMaker.wrap(bug));
		}

		@Override
		public void onClick() {
			tracker.reopenBug(getModelObject(), getUser());

			setResponsePage(new ViewBugPage(getModelObject()));
		}
	}

	@SpringBean
	private BugTrackerService tracker;

	@SpringBean
	private BugDAO bugDAO;

	private IModel<Bug> bugModel;

	public ViewBugPage(PageParameters params) {
		super("Bug");

		ViewBugPageParams parameters;
		try {
			parameters = requiredLong("bug").forParameters(params)
					.toClass(ViewBugPageParams.class);
		} catch (PageParameterExtractorException e) {
			throw new AbortWithHttpErrorCodeException(
					HttpServletResponse.SC_NOT_FOUND);
		}

		Bug bug = bugDAO.load(parameters.getId()).getOrElseThrow(
				() -> new RestartResponseAtInterceptPageException(
						BugOverviewPage.class));

		initPage(bug);
	}

	public ViewBugPage(Bug bug) {
		super("Bug");

		initPage(bug);

	}

	@Override
	protected void onDetach() {
		super.onDetach();

		if (bugModel != null) {
			bugModel.detach();
		}
	}

	/**
	 * @param bug
	 */
	private void initPage(Bug bug) {
		bugModel = ModelMaker.wrap(bug);

		setPageTitle(
				bug.getTitle() + " - " + bug.getReportType().getDescription());

		add(new Label("description", bug.getDescription())
				.setEscapeModelStrings(true));

		add(new Label("permalink",
				"Link to this " + bug.getReportType().getDescription())
				.add(AttributeModifier.replace("href",
						bug.getReportType().getUrl(bug.getId()))));

		add(new Label("status", bug.getStatus().toString()));

		add(new DateLabel("created", bug.getReported()));

		add(new DateLabel("updated", bug.getUpdated()));

		if (bug.getReporter() != null) {
			add(new MemberListItem("reportedBy", bug.getReporter()));
		} else {
			add(new Label("reportedBy", "<i>Someone not logged in</i>")
					.setEscapeModelStrings(false));
		}

		add(new Label("fixedIn", bug.getResolutionVersion())
				.setVisible(bug.getResolutionVersion() != null));

		add(new ListView<BugComment>("comments",
				ModelMaker.wrap(bug.getComments())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<BugComment> item) {
				BugComment comment = item.getModelObject();

				if (comment.getCommenter() != null) {
					item.add(
							new MemberListItem("user", comment.getCommenter()));
				} else {
					item.add(new Label("user", "<i>Non-logged in user</i>")
							.setEscapeModelStrings(false));
				}

				item.add(new Label("comment", comment.getDescription())
						.setEscapeModelStrings(false));
			}
		});

		final TextArea<String> descriptionArea = new TextArea<String>(
				"descriptionArea", new Model<String>(""));
		descriptionArea.setRequired(true);

		Form<Bug> createCommentForm = new Form<Bug>("addForm",
				ModelMaker.wrap(bug)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				tracker.addCommentToBug(getModelObject(), getUser(),
						descriptionArea.getModelObject());

				setResponsePage(new ViewBugPage(getModelObject()));
			}
		};

		createCommentForm.add(descriptionArea);

		add(createCommentForm);

		add(new IconLink.Builder("images/icons/bug_go.png",
				new AcceptResponder(bug)).setText("I will fix this bug")
				.newInstance("accept").setVisible(
						getUser().isBugReportMaster() && !getUser()
								.equals(bug.getAssignedTo())));

		add(new IconLink.Builder("images/icons/bug_delete.png",
				new CloseResponder(bug)).setText(
				"This bug/feature is fixed/realized in the current version, and can be closed")
				.newInstance("close").setVisible(getUser().isBugReportMaster()
						&& bug.getStatus() == BugStatus.RESOLVED));
		add(new IconLink.Builder("images/icons/bug_add.png",
				new ReopenResponder(bug)).setText("This bug still occurs")
				.newInstance("reopen").setVisible(
						bug.getStatus() == BugStatus.CLOSED
								&& bug.getReportType()
								!= ReportType.FEATUREREQUEST));

		WebMarkupContainer masterPanel = new WebMarkupContainer("masterPanel");

		BugFilter filter = new BugFilter();
		filter.id().notEqualTo(bug.getId());

		List<Bug> otherBugs = bugDAO.findByFilter(filter).asJava();

		final DropDownChoice<Bug> bugSelect = new DropDownChoice<Bug>(
				"duplicate", ModelMaker.wrap((Bug) null),
				ModelMaker.wrapChoices(otherBugs), new BugRenderer());
		bugSelect.setRequired(true);
		bugSelect.setNullValid(false);

		Form<Bug> resolveAsDuplicateForm = new Form<Bug>("markAsDuplicateForm",
				ModelMaker.wrap(bug)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				tracker.markAsDuplicate(getModelObject(),
						bugSelect.getModelObject());

				setResponsePage(new BugOverviewPage());
			}
		};

		resolveAsDuplicateForm.add(bugSelect);

		masterPanel.add(resolveAsDuplicateForm);

		masterPanel.setVisible(getUser().isBugReportMaster() && getUser()
				.equals(bug.getAssignedTo()) && (
				bug.getStatus() == BugStatus.NEW
						|| bug.getStatus() == BugStatus.ACKNOWLEDGED));

		final TextField<String> fixedInVersionField = new TextField<String>(
				"fixedIn",
				new Model<String>(TysanApplication.getApplicationVersion()));
		fixedInVersionField.setRequired(true);

		final TextArea<String> solutionArea = new TextArea<String>(
				"solutionArea", new Model<String>(""));
		solutionArea.setRequired(true);

		Form<Bug> resolveBugForm = new Form<Bug>("resolveForm",
				ModelMaker.wrap(bug)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				tracker.bugResolved(getModelObject(),
						solutionArea.getModelObject(),
						fixedInVersionField.getModelObject());

				setResponsePage(new BugOverviewPage());
			}
		};

		resolveBugForm.add(solutionArea);
		resolveBugForm.add(fixedInVersionField);

		masterPanel.add(resolveBugForm);

		add(masterPanel);
	}

	@Override
	protected void onClickBackToOverview() {
		Bug bug = bugModel.getObject();

		if (bug != null) {
			setResponsePage(bug.getReportType().getOverviewPage());
		} else {
			setResponsePage(new BugOverviewPage());
		}
	}
}
