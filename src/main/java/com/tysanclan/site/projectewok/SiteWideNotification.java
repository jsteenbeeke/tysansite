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
package com.tysanclan.site.projectewok;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.util.time.Duration;

/**
 * @author Jeroen Steenbeeke
 */
public final class SiteWideNotification implements
        Serializable {
	private static final long serialVersionUID = 1L;

	public static enum Category {
		INFO {
			@Override
			public void display(Component component,
			        String message) {
				component.info(message);

			}
		},
		WARNING {
			@Override
			public void display(Component component,
			        String message) {
				component.warn(message);

			}
		},
		ERROR {
			@Override
			public void display(Component component,
			        String message) {
				component.error(message);

			}
		};
		public abstract void display(Component component,
		        String message);
	}

	private final Category category;

	private final String message;

	private final Duration duration;

	private final long start;

	public SiteWideNotification(Category category,
	        String message, Duration duration) {
		this.category = category;
		this.message = message;
		this.duration = duration;
		this.start = System.currentTimeMillis();
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	public boolean isExpired() {
		return (start + duration.getMilliseconds()) < System
		        .currentTimeMillis();
	}

	public final void display(Component component) {
		getCategory().display(component, getMessage());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
		        * result
		        + ((category == null) ? 0 : category
		                .hashCode());
		result = prime
		        * result
		        + ((message == null) ? 0 : message
		                .hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SiteWideNotification other = (SiteWideNotification) obj;
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		return true;
	}

}
