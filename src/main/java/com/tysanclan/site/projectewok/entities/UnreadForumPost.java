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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Ties
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class UnreadForumPost extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UnreadForumPost")
	@SequenceGenerator(name = "UnreadForumPost", sequenceName = "SEQ_ID_UnreadForumPost")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_UNREADPOST_USER")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_UNREADPOST_FORUMPOST")
	private ForumPost forumPost;

	public UnreadForumPost() {
	}

	public UnreadForumPost(User user, ForumPost forumPost) {
		this.setUser(user);
		this.setForumPost(forumPost);
	}

	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return id;
	}

	public void setForumPost(ForumPost forumPost) {
		this.forumPost = forumPost;
	}

	public ForumPost getForumPost() {
		return forumPost;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
