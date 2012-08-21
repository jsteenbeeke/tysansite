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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

@Entity
public class RoleTransferApproval extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RoleTransferApproval")
	@SequenceGenerator(name = "RoleTransferApproval", sequenceName = "SEQ_ID_RoleTransferApproval", allocationSize = 1, initialValue = 1)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private RoleTransfer approvesOf;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User approvedBy;

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public RoleTransfer getApprovesOf() {
		return approvesOf;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public void setApprovesOf(RoleTransfer approvesOf) {
		this.approvesOf = approvesOf;
	}
}
