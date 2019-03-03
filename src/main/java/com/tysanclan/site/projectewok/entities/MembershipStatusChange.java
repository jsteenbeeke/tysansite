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
import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = {
		@Index(columnList = "user_id", name = "IDX_MSTATUSCHANGE_USER") })
public class MembershipStatusChange extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	public static enum ChangeType {
		APPLIED(0), TRIAL_DENIED(0), TRIAL_GRANTED(1), MEMBERSHIP_DENIED(
				-1), MEMBERSHIP_GRANTED(0), INACTIVITY_TIMEOUT(
				-1), LEFT_VOLUNTARILY(-1), FORCED_OUT(-1);

		private final int memberCountMutation;

		private ChangeType(int mutation) {
			this.memberCountMutation = mutation;
		}

		public int getMemberCountMutation() {
			return memberCountMutation;
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MembershipStatusChange")
	@SequenceGenerator(name = "MembershipStatusChange", sequenceName = "SEQ_ID_MembershipStatusChange", allocationSize = 1)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChangeType changeType;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private User user;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date changeTime;

	@Column(nullable = false)
	private int memberSizeMutation;

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

	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
		setMemberSizeMutation(changeType.getMemberCountMutation());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setMemberSizeMutation(int memberSizeMutation) {
		this.memberSizeMutation = memberSizeMutation;
	}

	public int getMemberSizeMutation() {
		return memberSizeMutation;
	}
}
