/*
 * Copyright 2010-2011 Jeroen Steenbeeke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeroensteenbeeke.hyperion.scheduling;

import org.apache.wicket.Application;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.protocol.http.MockHttpServletRequest;
import org.apache.wicket.protocol.http.MockHttpSession;
import org.apache.wicket.protocol.http.MockServletContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HyperionTaskExecutor implements Job {
	public static final Object TASK_KEY = "HyperionTask";
	public static final Object APP_KEY = "HyperionApplication";

	private static final Logger logger = LoggerFactory
			.getLogger(HyperionTaskExecutor.class);

	public HyperionTaskExecutor() {
	}

	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		HyperionTask task = (HyperionTask) context.getMergedJobDataMap().get(
				TASK_KEY);
		Application application = (Application) context.getMergedJobDataMap()
				.get(APP_KEY);
		Application.set(application);

		if (task != null) {
			MockServletContext sctx = new MockServletContext(application,
					"/src/main/webapp/");
			MockHttpServletRequest request = new MockHttpServletRequest(
					application, new MockHttpSession(sctx), sctx);
			RequestAttributes attr = new ServletRequestAttributes(request);

			RequestContextHolder.setRequestAttributes(attr);
			InjectorHolder.getInjector().inject(task);
			task.run();
			task.cleanUp();
			RequestContextHolder.resetRequestAttributes();
		} else {
			logger.error("No task passed to TysanTaskExecutor!");
		}
	}

}
