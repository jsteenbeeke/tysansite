/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.update;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.tysanclan.site.projectewok.entities.UpdateMark;

/**
 * @author Jeroen Steenbeeke
 */
public final class TaskFinder {
	private static final Logger log = LoggerFactory
	        .getLogger(TaskFinder.class);

	private Map<String, HashSet<String>> classMethodMarks;

	
	@SuppressWarnings("unchecked")
	public TaskFinder(Session session) {
		this.classMethodMarks = new HashMap<String, HashSet<String>>();

		Criteria crit = session
		        .createCriteria(UpdateMark.class);
		List<UpdateMark> marks = crit.list();
		for (UpdateMark mark : marks) {
			if (!classMethodMarks.containsKey(mark
			        .getClassName())) {
				classMethodMarks.put(mark.getClassName(),
				        new HashSet<String>());
			}

			classMethodMarks.get(mark.getClassName()).add(
			        mark.getMethodName());
		}
	}

	public List<Method> getTaskMethods(UpdateTask task) {
		List<Method> methods = new LinkedList<Method>();

		for (Method m : task.getClass().getMethods()) {
			Class<?>[] expectedParams = new Class<?>[] { Session.class };

			if (m.isAnnotationPresent(Update.class)
			        && Arrays.equals(expectedParams, m
			                .getParameterTypes())) {
				String fqdn = task.getClass().getName();

				if (!classMethodMarks.containsKey(fqdn)
				        || !classMethodMarks.get(fqdn)
				                .contains(m.getName())) {
					methods.add(m);
				}
			}
		}

		return methods;
	}

	public List<UpdateTask> getUpdateTasks() {
		List<UpdateTask> taskList = new LinkedList<UpdateTask>();

		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
		        false);
		provider.addIncludeFilter(new AssignableTypeFilter(
		        UpdateTask.class));
		Set<BeanDefinition> updaters = provider
		        .findCandidateComponents("com/tysanclan/site/projectewok/update/tasks");
		for (BeanDefinition def : updaters) {
			try {
				Class<?> taskType = Class.forName(def
				        .getBeanClassName());
				if (UpdateTask.class
				        .isAssignableFrom(taskType)) {
					UpdateTask task = (UpdateTask) taskType
					        .newInstance();
					taskList.add(task);
				} else {
					log
					        .warn(def.getBeanClassName()
					                + " is not an update task, please remove it from the tasks package");
				}

			} catch (ClassNotFoundException e) {
				log.error("Could not load task definition "
				        + def.getBeanClassName(), e);
			} catch (InstantiationException e) {
				log.error("Could not create task "
				        + def.getBeanClassName(), e);
			} catch (IllegalAccessException e) {
				log.error("Could not create task "
				        + def.getBeanClassName(), e);
			}
		}

		Collections.sort(taskList);

		return taskList;
	}
}
