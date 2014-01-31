package com.tysanclan.site.projectewok.pages.member.admin;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.RestService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.components.RevealablePasswordField;
import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;
import com.tysanclan.site.projectewok.entities.dao.AuthorizedRestApplicationDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.AuthorizedRestApplicationFilter;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;

public class StewardRestAgentPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private RestService restService;

	@SpringBean
	private AuthorizedRestApplicationDAO appDAO;

	public StewardRestAgentPage() {
		super("REST API Clients");

		if (!getUser().equals(roleService.getSteward()))
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);

		AuthorizedRestApplicationFilter activeFilter = new AuthorizedRestApplicationFilter();
		activeFilter.setActive(true);
		activeFilter.addOrderBy("name", true);

		add(new ListView<AuthorizedRestApplication>("active",
				ModelMaker.wrap(appDAO.findByFilter(activeFilter))) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(
					final ListItem<AuthorizedRestApplication> item) {
				AuthorizedRestApplication application = item.getModelObject();

				item.add(new Label("name", application.getName()));
				item.add(new Label("clientid", application.getClientId()));
				item.add(new RevealablePasswordField("clientsecret", Model
						.of(application.getClientSecret()))
						.setResetPassword(false));
				item.add(new IconLink.Builder(
						"images/icons/application_delete.png",
						new DefaultClickResponder<AuthorizedRestApplication>() {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								restService.deactivateApplication(item
										.getModelObject());

								setResponsePage(new StewardRestAgentPage());
							}

						}).newInstance("deactivate"));
			}
		});

		AuthorizedRestApplicationFilter inactiveFilter = new AuthorizedRestApplicationFilter();
		inactiveFilter.setActive(false);
		inactiveFilter.addOrderBy("name", true);

		add(new ListView<AuthorizedRestApplication>("inactive",
				ModelMaker.wrap(appDAO.findByFilter(inactiveFilter))) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(
					final ListItem<AuthorizedRestApplication> item) {
				AuthorizedRestApplication application = item.getModelObject();

				item.add(new Label("name", application.getName()));
				item.add(new IconLink.Builder(
						"images/icons/application_go.png",
						new DefaultClickResponder<AuthorizedRestApplication>() {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								restService.activateApplication(item
										.getModelObject());

								setResponsePage(new StewardRestAgentPage());
							}

						}).newInstance("activate"));
				item.add(new IconLink.Builder("images/icons/cross.png",
						new DefaultClickResponder<AuthorizedRestApplication>() {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								restService.deleteApplication(item
										.getModelObject());

								setResponsePage(new StewardRestAgentPage());
							}

						}).newInstance("delete"));
			}
		});

		final TextField<String> nameField = new TextField<String>("name",
				Model.of(""));
		nameField.setRequired(true);

		Form<AuthorizedRestApplication> addForm = new Form<AuthorizedRestApplication>(
				"addForm") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				final String name = nameField.getModelObject();

				AuthorizedRestApplication application = restService
						.createApplication(name);

				if (application != null) {
					setResponsePage(new StewardRestAgentPage());
				} else {
					error("Could not create application. Does the name already exist?");
				}
			}
		};
		addForm.add(nameField);

		add(addForm);
	}
}
