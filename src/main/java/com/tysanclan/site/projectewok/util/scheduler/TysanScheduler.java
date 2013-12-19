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

import java.util.LinkedList;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tysanclan.site.projectewok.TysanApplication;

/**
 * @author Jeroen Steenbeeke
 */
public class TysanScheduler {
	private static TysanScheduler instance;
	private static Logger logger = LoggerFactory
	        .getLogger(TysanScheduler.class);

	private Scheduler scheduler;

	private TysanApplication application;

	private List<Class<? extends TysanTask>> taskList;

	/**
     * 
     */
	private TysanScheduler() throws SchedulerException {
		taskList = new LinkedList<Class<? extends TysanTask>>();

		SchedulerFactory schedFact = new StdSchedulerFactory();
		scheduler = schedFact.getScheduler();
		scheduler.start();
	}

	public static TysanScheduler getScheduler() {
		if (instance == null) {
			try {
				instance = new TysanScheduler();
			} catch (SchedulerException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return instance;
	}

	public void scheduleTask(TysanTask task) {
		if (!taskList.contains(task.getClass())) {
			taskList.add(task.getClass());
		}

		JobDetail detail = new JobDetail(task.getName(),
		        task.getGroup(), TysanTaskExecutor.class);
		detail.getJobDataMap().put(
		        TysanTaskExecutor.TASK_KEY, task);
		detail.getJobDataMap()
		        .put(TysanTaskExecutor.APP_KEY,
		                getApplication());
		try {
			scheduler.scheduleJob(detail, task
			        .getQuartzTrigger());
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
	public void setApplication(TysanApplication application) {
		this.application = application;
	}

	/**
	 * @return the application
	 */
	public TysanApplication getApplication() {
		return application;
	}

	/**
	 	 */
	public List<? extends Class<? extends TysanTask>> getScheduledTaskTypes() {
		return taskList;
	}
}
