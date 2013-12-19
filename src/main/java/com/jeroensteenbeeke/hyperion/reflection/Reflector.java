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
package com.jeroensteenbeeke.hyperion.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Reflector {
	private static final Logger log = LoggerFactory.getLogger(Reflector.class);

	public enum Accessor {
		GET, SET;

		private final String prefix;

		Accessor() {
			this.prefix = name().toLowerCase();
		}

		public String getAccessorName(String field) {
			StringBuilder result = new StringBuilder();

			result.append(prefix);
			result.append(Character.toUpperCase(field.charAt(0)));
			result.append(field.substring(1));

			return result.toString();
		}
	}

	private Reflector() {

	}

	@SuppressWarnings("unchecked")
	public static <T> T invokeGetter(Object target, String property) {
		Class<?> targetClass = target.getClass();
		StringBuilder getterName = new StringBuilder().append("get").append(
				property.substring(0, 1).toUpperCase()).append(
				property.substring(1));
		try {
			Method method = targetClass.getMethod(getterName.toString());

			return (T) method.invoke(target);
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(String fqdn) {
		try {
			return (Class<T>) Class.forName(fqdn);
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static <T> Property<T> getProperty(Class<T> classRef, String name) {
		try {
			Field f = classRef.getDeclaredField(name);

			return getProperty(classRef, f);

		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
			return null;
		} catch (NoSuchFieldException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static <T> Property<T> getProperty(Class<T> classRef, Field f) {
		boolean isFinal = Modifier.isFinal(f.getModifiers());

		String fieldName = f.getName();

		Method getter = null;
		Method setter = null;

		try {
			getter = f.getDeclaringClass().getMethod(
					Accessor.GET.getAccessorName(fieldName));
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
			return null;
		}

		if (!isFinal) {
			try {
				setter = f.getDeclaringClass().getMethod(
						Accessor.SET.getAccessorName(fieldName), f.getType());
			} catch (NoSuchMethodException e) {
				log.error(e.getMessage(), e);
				return null;
			}
		}

		if (getter != null && (setter != null || isFinal)) {
			return new DefaultProperty<T>(classRef, getter, setter, f,
					fieldName, isFinal);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<Property<T>> getProperties(T object) {
		return getProperties((Class<T>) object.getClass());
	}

	public static <T> List<Property<T>> getProperties(Class<T> classRef) {
		List<Property<T>> properties = new LinkedList<Property<T>>();
		List<Class<? super T>> refs = getSuperClasses(classRef, true);

		for (Class<? super T> ref : refs) {
			for (Field f : ref.getDeclaredFields()) {
				if (!Modifier.isStatic(f.getModifiers())) {
					Property<T> prop = getProperty(classRef, f);
					if (prop != null) {
						properties.add(prop);
					}
				}

			}
		}

		return properties;
	}

	public static <T> List<Class<? super T>> getSuperClasses(Class<T> classRef) {
		return getSuperClasses(classRef, false);
	}

	public static <T> List<Class<? super T>> getSuperClasses(Class<T> classRef,
			boolean includeCurrent) {
		List<Class<? super T>> results = new LinkedList<Class<? super T>>();
		Class<? super T> next = classRef;
		while (next != Object.class) {
			results.add(classRef);
			next = next.getSuperclass();
		}

		return results;
	}

	public static <T> T instance(Class<T> classRef, Object... params) {
		Class<?>[] paramTypes = new Class<?>[params.length];
		int i = 0;
		for (Object param : params) {
			paramTypes[i++] = param != null ? param.getClass() : null;
		}

		try {
			Constructor<T> c = classRef.getConstructor(paramTypes);
			return c.newInstance(params);
		} catch (SecurityException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(), e);
		} catch (InstantiationException e) {
			log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}

		return null;
	}

	private static class DefaultProperty<T> implements Property<T> {
		private static final long serialVersionUID = 1L;

		private transient Method getter;
		private transient Method setter;
		private transient Field field;
		private String fieldName;
		private final boolean isFinal;
		private transient Class<T> owningClass;

		private String fqdn;

		public DefaultProperty(Class<T> owningClass, Method getter,
				Method setter, Field field, String name, boolean isFinal) {
			super();
			this.getter = getter;
			this.setter = setter;
			this.field = field;
			this.fieldName = name;
			this.owningClass = owningClass;
			this.fqdn = owningClass.getName();
			this.isFinal = isFinal;
		}

		@Override
		public Class<T> owningClass() {
			if (owningClass == null) {
				attach();
			}

			return owningClass;
		}

		private void attach() {
			Class<T> clazz = Reflector.getClass(fqdn);
			Property<T> copy = Reflector.getProperty(clazz, fieldName);

			this.owningClass = clazz;
			this.getter = copy.getter();
			this.setter = copy.setter();
			this.field = copy.field();
		}

		@Override
		public Method getter() {
			if (getter == null)
				attach();

			return getter;
		}

		@Override
		public Method setter() {
			if (setter == null && !isFinal)
				attach();

			return setter;
		}

		@Override
		public Field field() {
			if (field == null)
				attach();

			return field;
		}

		@Override
		public String name() {
			return fieldName;
		}

		@Override
		public Object get(T target) {
			try {
				if (getter == null)
					attach();

				return getter.invoke(target);
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage(), e);
				return null;
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(), e);
				return null;
			} catch (InvocationTargetException e) {
				log.error(e.getMessage(), e);
				return null;
			}
		}

		@Override
		public void set(T target, Object value) {
			try {
				if (!isFinal) {
					if (setter == null)
						attach();

					setter.invoke(target, value);
				} else {
					log.warn("Tried to invoke setter on final property "
							+ fieldName + " of class " + fqdn);
				}
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				log.error(e.getMessage(), e);
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((fieldName == null) ? 0 : fieldName.hashCode());
			result = prime * result + ((fqdn == null) ? 0 : fqdn.hashCode());
			result = prime * result + (isFinal ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DefaultProperty<?> other = (DefaultProperty<?>) obj;
			if (fieldName == null) {
				if (other.fieldName != null)
					return false;
			} else if (!fieldName.equals(other.fieldName))
				return false;
			if (fqdn == null) {
				if (other.fqdn != null)
					return false;
			} else if (!fqdn.equals(other.fqdn))
				return false;
			if (isFinal != other.isFinal)
				return false;
			return true;
		}

	}
}
