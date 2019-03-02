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

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(indexes = { @Index(columnList = "user_id", name = "IDX_ATTSUPPR_USER") })
public class AttentionSuppression extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AttentionSuppression")
	@SequenceGenerator(name = "AttentionSuppression", sequenceName = "SEQ_ID_AttentionSuppression", allocationSize = 1)
	private Long id;

	@Column(nullable = false)
	private Long dismissableId;

	@Column(nullable = false)
	private String conditionClass;

	@Column(nullable = false)
	private String containingClass;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User user;

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDismissableId() {
		return dismissableId;
	}

	public void setDismissableId(Long dismissableId) {
		this.dismissableId = dismissableId;
	}

	public String getConditionClass() {
		return conditionClass;
	}

	public void setConditionClass(String conditionClass) {
		this.conditionClass = conditionClass;
	}

	public String getContainingClass() {
		return containingClass;
	}

	public void setContainingClass(String panelClass) {
		this.containingClass = panelClass;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
