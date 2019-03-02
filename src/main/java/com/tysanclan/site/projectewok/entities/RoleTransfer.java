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
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(indexes = { //
		@Index(name = "IDX_ROLETRANSFER_CAND", columnList = "candidate_id") //
})
public class RoleTransfer extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RoleTransfer")
	@SequenceGenerator(name = "RoleTransfer", sequenceName = "SEQ_ID_RoleTransfer", allocationSize = 1, initialValue = 1)
	private Long id;

	@Column(unique = true, nullable = false)
	private RoleType roleType;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User candidate;

	@Column(nullable = false)
	private boolean accepted;

	@Column(nullable = false)
	private Date start;

	@OneToMany(mappedBy = "approvesOf", fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<RoleTransferApproval> approvedBy;

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

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public User getCandidate() {
		return candidate;
	}

	public void setCandidate(User candidate) {
		this.candidate = candidate;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public List<RoleTransferApproval> getApprovedBy() {
		if (approvedBy == null)
			approvedBy = new ArrayList<RoleTransferApproval>(0);

		return approvedBy;
	}

	public void setApprovedBy(List<RoleTransferApproval> approvedBy) {
		this.approvedBy = approvedBy;
	}
}
