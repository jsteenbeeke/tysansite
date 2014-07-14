package com.tysanclan.site.projectewok;

import java.util.Set;

import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.request.mapper.StalePageException;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.tysanclan.site.projectewok.util.StringUtil;

public class ErrorReporterListener extends AbstractRequestCycleListener {
	private static final Set<String> RESOLVE_AS_404 = Sets.newHashSet("png",
			"js", "css", "jpg", "gif");

	private static Logger log = LoggerFactory
			.getLogger(ErrorReporterListener.class);

	@Override
	public IRequestHandler onException(RequestCycle cycle, Exception ex) {
		if (ex instanceof PageExpiredException) {
			return null;
		} else if (ex instanceof ReplaceHandlerException) {
			return null;
		} else if (ex instanceof StalePageException) {
			return null;
		} else if (ex instanceof AbortWithHttpErrorCodeException) {
			// Do not log explicit HTTP errors
			return null;
		}

		String target = null;
		String referrer = null;

		final Request request = cycle.getRequest();
		if (request != null) {
			Url url = request.getUrl();
			if (url != null) {
				target = url.toString();
			}

			if (request instanceof WebRequest) {
				WebRequest wr = (WebRequest) request;
				referrer = wr.getHeader("referer"); // Yes, this is mis-spelled
													// in the protocol
			}

		}

		final String extension = StringUtil.getFileExtension(target);

		if (extension != null && !extension.isEmpty()
				&& RESOLVE_AS_404.contains(extension)) {
			return null;
		}
		log.error(ex.getMessage(), ex);

		return new RenderPageRequestHandler(new ExceptionPageProvider(target,
				referrer, ex));

	}
}
