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
package com.tysanclan.site.projectewok.entities;

import javax.persistence.*;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "user_id",
		"otterNumber" }))
public class OtterSighting extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OtterSighting")
	@SequenceGenerator(name = "OtterSighting", sequenceName = "SEQ_ID_OtterSighting")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_OTTER_USER")
	private User user;

	@Column(nullable = true)
	private Integer otterNumber;

	// $P$

	protected OtterSighting() {
		// $H$
	}

	public OtterSighting(User user, Integer otterId) {
		this.user = user;
		this.otterNumber = otterId;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setOtterNumber(Integer otterNumber) {
		this.otterNumber = otterNumber;
	}

	public Integer getOtterNumber() {
		return otterNumber;
	}

	// $GS$
}
