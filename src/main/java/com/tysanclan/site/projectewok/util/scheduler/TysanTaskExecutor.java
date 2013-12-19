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
package com.tysanclan.site.projectewok.util.scheduler;

import org.apache.wicket.ThreadContext;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.beans.BugTrackerService;

/**
 * @author Jeroen Steenbeeke
 */
public class TysanTaskExecutor implements Job {
	public static final Object TASK_KEY = "TysanTask";
	public static final Object APP_KEY = "TysanApplication";

	private static final Logger logger = LoggerFactory
			.getLogger(TysanTaskExecutor.class);

	public TysanTaskExecutor() {
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		TysanTask task = (TysanTask) context.getMergedJobDataMap()
				.get(TASK_KEY);
		TysanApplication application = (TysanApplication) context
				.getMergedJobDataMap().get(APP_KEY);
		ThreadContext.setApplication(application);

		if (task != null) {
			MockServletContext sctx = new MockServletContext(application,
					"/src/main/webapp/");
			MockHttpServletRequest request = new MockHttpServletRequest(
					application, new MockHttpSession(sctx), sctx);
			RequestAttributes attr = new ServletRequestAttributes(request);

			RequestContextHolder.setRequestAttributes(attr);
			Injector.get().inject(task);
			try {
				task.run();
			} catch (Exception e) {
				BugTrackerService tracker = TysanApplication
						.getApplicationContext().getBean(
								BugTrackerService.class);
				if (!tracker.isKnownIssue(e)) {
					tracker.reportCrash(null, task.getClass().getName(), e);
				}
			}
			task.cleanUp();
			RequestContextHolder.resetRequestAttributes();
		} else {
			logger.error("No task passed to TysanTaskExecutor!");
		}
	}
}
