/**
 * Copyright 2013 Jeroen Steenbeeke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeroensteenbeeke.hyperion.events;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public class DefaultEventDispatcher implements IEventDispatcher,
		ApplicationContextAware {
	private Multimap<Class<? extends Event<?>>, Class<? extends EventHandler<?>>> handlers = LinkedHashMultimap
			.create();

	private ApplicationContext applicationContext;

	public DefaultEventDispatcher() {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public void setScanPackages(List<String> packageNames) {
		ClasspathScanner scanner = new ClasspathScanner(EventHandler.class);

		for (String pkg : packageNames) {
			for (Class<?> cls : scanner.getComponentClasses(pkg)) {
				Class<? extends Event<?>> eventClass = getEventClass(cls);

				handlers.put(eventClass, (Class<? extends EventHandler<?>>) cls);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dispatchEvent(@Nonnull Event<?> event) {
		AutowireCapableBeanFactory factory = applicationContext
				.getAutowireCapableBeanFactory();
		List<Event<?>> queue = Lists.<Event<?>> newArrayList(event);

		while (!queue.isEmpty()) {
			Event<?> evt = queue.remove(0);

			for (Class<? extends EventHandler<?>> eventHandler : handlers
					.get((Class<? extends Event<?>>) evt.getClass())) {
				EventHandler<Event<?>> handler = (EventHandler<Event<?>>) factory
						.autowire(eventHandler,
								AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
								true);

				EventResult result = handler.onEvent(evt);

				if (result.isAbort()) {
					throw new RuntimeException(String.format(
							"Handler %s aborted event %s with message: %s",
							handler, evt, result.getMessage()));
				}

				queue.addAll(result.getTriggeredEvents());
			}
		}
	}

	@SuppressWarnings("unchecked")
	static Class<? extends Event<?>> getEventClass(Class<?> handlerClass) {

		for (Class<?> i : handlerClass.getInterfaces()) {
			if (EventHandler.class.equals(i)) {
				for (Type t : handlerClass.getGenericInterfaces()) {
					if (t instanceof ParameterizedType) {
						ParameterizedType pt = (ParameterizedType) t;
						if (EventHandler.class.equals(pt.getRawType())) {

							return (Class<? extends Event<?>>) pt
									.getActualTypeArguments()[0];

						}
					}
				}
			} else if (EventHandler.class.isAssignableFrom(i)) {
				return getEventClass((Class<? extends EventHandler<?>>) i);
			}
		}

		if (EventHandler.class.isAssignableFrom(handlerClass.getSuperclass())) {
			return getEventClass((Class<?>) handlerClass.getSuperclass());
		}

		return null;

	}

	static final class ClasspathScanner extends
			ClassPathScanningCandidateComponentProvider {
		public ClasspathScanner(Class<?> targetClass) {
			super(false);
			addIncludeFilter(new AssignableTypeFilter(targetClass));
		}

		public final List<Class<?>> getComponentClasses(String basePackage) {
			List<Class<?>> classes = new ArrayList<Class<?>>();

			for (BeanDefinition candidate : findCandidateComponents(basePackage)) {
				Class<?> cls = ClassUtils.resolveClassName(
						candidate.getBeanClassName(),
						ClassUtils.getDefaultClassLoader());

				classes.add(cls);
			}
			return classes;
		}

	}
}
