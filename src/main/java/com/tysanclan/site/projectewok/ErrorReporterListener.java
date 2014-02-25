package com.tysanclan.site.projectewok;

import java.util.Set;

import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.protocol.http.PageExpiredException;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
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
		}

		String target = null;

		final Request request = cycle.getRequest();
		if (request != null) {
			Url url = request.getUrl();
			if (url != null) {
				target = url.toString();
			}
		}

		final String extension = StringUtil.getFileExtension(target);

		if (extension != null && !extension.isEmpty()
				&& RESOLVE_AS_404.contains(extension)) {
			return null;
		}
		log.error(ex.getMessage(), ex);
		Throwable e = ex.getCause();

		log.error("<EXTRALOGGING>");
		while (e != null) {

			log.error(e.getMessage(), e);
			e = e.getCause();
		}
		log.error("</EXTRALOGGING>");

		return new RenderPageRequestHandler(new ExceptionPageProvider(target,
				ex));

	}
}
