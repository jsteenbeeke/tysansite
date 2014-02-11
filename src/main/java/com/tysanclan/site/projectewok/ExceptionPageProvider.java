package com.tysanclan.site.projectewok;

import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.tysanclan.site.projectewok.pages.TysanErrorPage;

public class ExceptionPageProvider extends PageProvider {
	private Exception exception;

	private String target;

	private IRequestablePage requestedPage;

	public ExceptionPageProvider(String target, Exception exception) {
		super(TysanErrorPage.class);
		this.exception = exception;
		this.target = target;
	}

	@Override
	public IRequestablePage getPageInstance() {
		if (requestedPage == null) {
			requestedPage = new TysanErrorPage(target, exception);
		}

		return requestedPage;
	}

	@Override
	public PageParameters getPageParameters() {
		return new PageParameters();
	}

	@Override
	public boolean isNewPageInstance() {
		return false;
	}

	@Override
	public Class<? extends IRequestablePage> getPageClass() {
		return TysanErrorPage.class;
	}

	@Override
	public void detach() {

	}
}
