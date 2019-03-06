package com.tysanclan.site.projectewok.pages.member.admin;

import com.tysanclan.site.projectewok.beans.MyriadExportService;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.time.Duration;

import java.io.File;

public class ExportToMyriadPage extends AbstractMemberPage {

	@SpringBean
	private RoleService roleService;

	@SpringBean
	private MyriadExportService myriadExportService;

	public ExportToMyriadPage() {
		super("Export to Project Myriad");

		if (!getUser().equals(roleService.getSteward())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		add(new Link<File>("export") {
			@Override
			public void onClick() {
				myriadExportService.export().map(file -> {
					IResourceStream resourceStream = new FileResourceStream(
							new org.apache.wicket.util.file.File(file));
					getRequestCycle().scheduleRequestHandlerAfterCurrent(
							new ResourceStreamRequestHandler(resourceStream) {
								@Override
								public void respond(
										IRequestCycle requestCycle) {
									super.respond(requestCycle);

									Files.remove(file);
								}
							}.setFileName("ewok.xml").setContentDisposition(
									ContentDisposition.ATTACHMENT)
									.setCacheDuration(Duration.minutes(5)));

					return file;
				}).ifNotOk(ExportToMyriadPage.this::error);

			}
		});

	}
}
