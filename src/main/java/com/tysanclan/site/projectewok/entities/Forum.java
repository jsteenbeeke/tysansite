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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "forum")
public class Forum extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_FORUM_CATEGORY")
	private ForumCategory category;

	@Column
	private String description;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Forum")
	@SequenceGenerator(name = "Forum", sequenceName = "SEQ_ID_Forum", allocationSize = 1)
	private Long id;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ForumModerators", joinColumns = @JoinColumn(name = "forum_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> moderators;

	@Column
	private String name;

	@Column
	// If set to true, allows any user to post on the forum
	private boolean publicAccess;

	@Column
	// If set to true, only members can see the forum
	private boolean membersOnly;

	@OneToMany(mappedBy = "forum", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("postTime desc")
	private Set<ForumThread> threads;

	@Column
	private int position;

	@Column
	// If set to true, this forum will be used for event announcements,
	// user applications and other interactive threads
	private boolean interactive;

	// $P$

	public Forum() {
		threads = new HashSet<ForumThread>();
		moderators = new HashSet<User>();
		// $H$
	}

	public ForumCategory getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the moderators
	 */
	public Set<User> getModerators() {
		if (moderators == null)
			moderators = new HashSet<User>();

		return moderators;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return the threads
	 */
	public Set<ForumThread> getThreads() {
		return threads;
	}

	@Transient
	public boolean isAccessible(User u) {
		return canView(u);
	}

	/**
	 * @return the publicAccess
	 */
	public boolean isPublicAccess() {
		return publicAccess;
	}

	public void setCategory(ForumCategory category) {
		this.category = category;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param moderators
	 *            the moderators to set
	 */
	public void setModerators(Set<User> moderators) {
		this.moderators = moderators;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param publicAccess
	 *            the publicAccess to set
	 */
	public void setPublicAccess(boolean publicAccess) {
		this.publicAccess = publicAccess;
	}

	/**
	 * @param threads
	 *            the threads to set
	 */
	public void setThreads(Set<ForumThread> threads) {
		this.threads = threads;
	}

	/**
	 * @return the membersOnly
	 */
	public boolean isMembersOnly() {
		return membersOnly;
	}

	/**
	 * @param membersOnly
	 *            the membersOnly to set
	 */
	public void setMembersOnly(boolean membersOnly) {
		this.membersOnly = membersOnly;
	}

	/**
	 * @return the interactive
	 */
	public boolean isInteractive() {
		return interactive;
	}

	/**
	 * @param interactive
	 *            the interactive to set
	 */
	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return getName();
	}

	public boolean canCreateThread(User user) {
		return !isMembersOnly() || (user != null && MemberUtil.isMember(user));
	}

	public boolean canView(User user) {
		return !isMembersOnly() || (user != null && MemberUtil.isMember(user));
	}

	public boolean canReply(User user) {
		return user != null;
	}

	// $GS$

}
