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
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(name = "TGROUP", indexes = { //
		@Index(name = "IDX_GROUP_LEADER", columnList = "leader_id") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, length = 255)
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public abstract class Group extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	public enum JoinPolicy {
		OPEN, APPLICATION, INVITATION;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "groupapplications", inverseJoinColumns = @JoinColumn(name = "user_id"), joinColumns = @JoinColumn(name = "group_id"))
	@OrderBy("username")
	private List<User> appliedMembers;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "groupmembers", inverseJoinColumns = @JoinColumn(name = "user_id"), joinColumns = @JoinColumn(name = "group_id"))
	@OrderBy("username")
	private List<User> groupMembers;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GroupSeq")
	@SequenceGenerator(name = "GroupSeq", sequenceName = "SEQ_ID_GROUP", allocationSize = 1)
	private Long id;

	@Column
	private String description;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "groupinvitations", inverseJoinColumns = @JoinColumn(name = "user_id"), joinColumns = @JoinColumn(name = "group_id"))
	@OrderBy("username")
	private List<User> invitedMembers;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User leader;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private JoinPolicy joinPolicy;

	@Column(nullable = true)
	private String messageOfTheDay;

	// $P$

	/**
	 *
	 */
	public Group() {
		this.appliedMembers = new LinkedList<User>();
		this.groupMembers = new LinkedList<User>();
		this.invitedMembers = new LinkedList<User>();
		// $H$
	}

	/**
	 * @return the appliedMembers
	 */
	public List<User> getAppliedMembers() {
		return appliedMembers;
	}

	/**
	 * @return the groupMembers
	 */
	public List<User> getGroupMembers() {
		return groupMembers;
	}

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	/**
	 * @return the invitedMembers
	 */
	public List<User> getInvitedMembers() {
		return invitedMembers;
	}

	/**
	 * @return the leader
	 */
	public User getLeader() {
		return leader;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param appliedMembers
	 *            the appliedMembers to set
	 */
	public void setAppliedMembers(List<User> appliedMembers) {
		this.appliedMembers = appliedMembers;
	}

	/**
	 * @param groupMembers
	 *            the groupMembers to set
	 */
	public void setGroupMembers(List<User> groupMembers) {
		this.groupMembers = groupMembers;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param invitedMembers
	 *            the invitedMembers to set
	 */
	public void setInvitedMembers(List<User> invitedMembers) {
		this.invitedMembers = invitedMembers;
	}

	/**
	 * @param leader
	 *            the leader to set
	 */
	public void setLeader(User leader) {
		this.leader = leader;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the joinPolicy
	 */
	public JoinPolicy getJoinPolicy() {
		return joinPolicy;
	}

	/**
	 * @param joinPolicy
	 *            the joinPolicy to set
	 */
	public void setJoinPolicy(JoinPolicy joinPolicy) {
		this.joinPolicy = joinPolicy;
	}

	public abstract String getTypeDescription();

	/**
	 * @return the messageOfTheDay
	 */
	public String getMessageOfTheDay() {
		return messageOfTheDay;
	}

	/**
	 * @param messageOfTheDay
	 *            the messageOfTheDay to set
	 */
	public void setMessageOfTheDay(String messageOfTheDay) {
		this.messageOfTheDay = messageOfTheDay;
	}

	// $GS$

}
