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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.Application;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperionScheduler {
	private static HyperionScheduler instance;
	private static Logger logger = LoggerFactory
			.getLogger(HyperionScheduler.class);

	private Scheduler scheduler;

	private Application application;

	private List<Class<? extends HyperionTask>> taskList;

	/**
	 * 
	 */
	private HyperionScheduler() throws SchedulerException {
		taskList = new LinkedList<Class<? extends HyperionTask>>();

		SchedulerFactory schedFact = new StdSchedulerFactory();
		scheduler = schedFact.getScheduler();
		scheduler.start();
	}

	public static HyperionScheduler getScheduler() {
		if (instance == null) {
			try {
				instance = new HyperionScheduler();
			} catch (SchedulerException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return instance;
	}

	public void scheduleTask(HyperionTask task) {
		if (!taskList.contains(task.getClass())) {
			taskList.add(task.getClass());
		}

		JobDetail detail = new JobDetail(task.getName(), task.getGroup(),
				HyperionTaskExecutor.class);
		detail.getJobDataMap().put(HyperionTaskExecutor.TASK_KEY, task);
		detail.getJobDataMap().put(HyperionTaskExecutor.APP_KEY,
				getApplication());
		try {
			scheduler.scheduleJob(detail, task.getQuartzTrigger());
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void shutdown() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(Application application) {
		this.application = application;
	}

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 	 */
	public List<? extends Class<? extends HyperionTask>> getScheduledTaskTypes() {
		return taskList;
	}
}
