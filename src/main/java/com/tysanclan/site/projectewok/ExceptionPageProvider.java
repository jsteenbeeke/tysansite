package com.tysanclan.site.projectewok;

import com.tysanclan.site.projectewok.pages.TysanErrorPage;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExceptionPageProvider extends PageProvider {
	private Exception exception;

	private String target;

	private IRequestablePage requestedPage;

	private String referrer;

	public ExceptionPageProvider(@Nullable String target,
			@Nullable String referrer, @Nonnull Exception exception) {
		super(TysanErrorPage.class);
		this.exception = exception;
		this.target = target;
		this.referrer = referrer;
	}

	@Override
	public IRequestablePage getPageInstance() {
		if (requestedPage == null) {
			requestedPage = new TysanErrorPage(target, referrer, exception);
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
