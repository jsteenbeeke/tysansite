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

import org.quartz.Trigger;

public abstract class HyperionTask {
	private final String name;
	private final String group;

	/**
	 * 
	 */
	protected HyperionTask(String name, String group) {
		this.name = name;
		this.group = group;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @return The Quartz trigger for executing
	 */
	public abstract Trigger getQuartzTrigger();

	/**
	 * Executes the task
	 */
	public abstract void run();

	/**
	 * Called at the end of task execution
	 */
	public void cleanUp() {
		// Do nothing, overriding method may do cleanup actions here
	}
}
