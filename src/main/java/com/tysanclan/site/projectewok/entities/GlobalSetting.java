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
package com.tysanclan.site.projectewok.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.AccessType;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
public class GlobalSetting extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	public static enum GlobalSettings {
		DEFAULT {
			@Override
			public GlobalSetting getNewDefaultGlobalSetting() {
				GlobalSetting setting = new GlobalSetting();
				setting.setId(name());
				setting.setValue("");
				return setting;
			}
		},
		BLAH {
			@Override
			public GlobalSetting getNewDefaultGlobalSetting() {
				GlobalSetting setting = new GlobalSetting();
				setting.setId(name());
				setting.setValue("no");
				return setting;
			}
		};

		public abstract GlobalSetting getNewDefaultGlobalSetting();
	}

	@Id
	private String id;

	@Column(nullable = false)
	private String value;

	// $P$

	/**
	 * Creates a new GlobalSetting object
	 */
	public GlobalSetting() {
		// $H$
	}

	/**
	 * Returns the ID of this GlobalSetting
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	// $GS$
}
