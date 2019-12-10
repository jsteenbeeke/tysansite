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

import io.vavr.control.Option;
import org.hibernate.annotations.Cache;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@DiscriminatorValue("GroupForum")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "forum")
public class GroupForum extends Forum {
	public static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Group group;

	/**
	 * Creates a new GroupForum object
	 */
	public GroupForum() {
		setMembersOnly(true);
	}

	/**
	 * @return The Group of this GroupForum
	 */
	public Group getGroup() {
		return this.group;
	}

	/**
	 * Sets the Group of this GroupForum
	 *
	 * @param group
	 *            The Group of this GroupForum
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.Forum#setMembersOnly(boolean)
	 */
	@Override
	public void setMembersOnly(boolean membersOnly) {
		super.setMembersOnly(true);
	}

	@Override
	public boolean canCreateThread(User user) {
		return (user != null && getGroup().getGroupMembers().contains(user));
	}

	@Override
	public boolean canView(User user) {
		Group g = getGroup();

		return user != null && g.getGroupMembers().contains(user);
	}

	@Override
	public boolean canReply(User user) {
		return super.canReply(user) && canView(user);
	}

	// $GS$


	@Override
	public Option<GroupForum> asGroupForum() {
		return Option.some(this);
	}
}
