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

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeroensteenbeeke.hyperion.scheduling.interval.Interval;

public abstract class PeriodicTask extends HyperionTask {

	@Deprecated
	public enum ExecutionMode implements Interval {
		// Seconds, Minutes, Hours, Day-of-month, Month, Day-of-week, Year
		HOURLY("0 0 */1 * * ?"), ONCE_EVERY_TWO_HOURS("0 0 */2 * * ?"), ONCE_EVERY_FOUR_HOURS(
				"0 0 */4 * * ?"), ONCE_EVERY_SIX_HOURS("0 0 */6 * * ?"), ONCE_EVERY_TWELVE_HOURS(
				"0 0 */12 * * ?"), DAILY("0 0 6 * * ?"), WEEKLY("0 0 6 * * SUN"), MONTHLY(
				"0 0 6 1 * ?"), ANNUALLY("0 0 6 1 1 ?"), DEBUG("*/30 * * * * ?");

		/**
		 * 
		 */
		private ExecutionMode(String cronExpression) {
			this.cronExpression = cronExpression;
		}

		private final String cronExpression;

		/**
		 * @return the cronExpression
		 */
		@Override
		public String getCronExpression() {
			return cronExpression;
		}
	}

	private static Logger log = LoggerFactory.getLogger(PeriodicTask.class);

	private Interval interval;

	protected PeriodicTask(String name, String group, Interval interval) {
		super(name, group);
		this.interval = interval;
	}

	@Override
	public Trigger getQuartzTrigger() {

		CronTrigger trigger = new CronTrigger(getName(), getGroup());
		try {
			trigger.setCronExpression(interval.getCronExpression());
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}

		return trigger;
	}

}
