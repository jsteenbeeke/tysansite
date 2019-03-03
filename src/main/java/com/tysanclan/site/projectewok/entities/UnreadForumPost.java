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

/**
 * @author Ties
 */
@Entity
@Table(indexes = { //
		@Index(name = "IDX_UNREADPOST_USER", columnList = "user_id"),
		@Index(name = "IDX_UNREADPOST_FORUMPOST", columnList = "forumPost_id") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class UnreadForumPost extends BaseDomainObject {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UnreadForumPost")
	@SequenceGenerator(name = "UnreadForumPost", sequenceName = "SEQ_ID_UnreadForumPost")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
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
