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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "forum")
public class ForumPost extends BaseDomainObject {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	private ForumThread branchTo;

	@Column
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String content;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ForumPost")
	@SequenceGenerator(name = "ForumPost", sequenceName = "SEQ_ID_ForumPost", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_FORUMPOST_POSTER")
	private User poster;

	@Column
	private boolean shadow;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_FORUMPOST_THREAD")
	private ForumThread thread;

	@Column
	private Date time;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "forumPost", cascade = CascadeType.ALL)
	private List<UnreadForumPost> unreadForumPosts;

	// $P$

	public ForumPost() {
		// $H$
	}

	/**
	 * @return the branchTo
	 */
	public ForumThread getBranchTo() {
		return branchTo;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the poster
	 */
	public User getPoster() {
		return poster;
	}

	/**
	 * @return the thread
	 */
	public ForumThread getThread() {
		return thread;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @return the shadow
	 */
	public boolean isShadow() {
		return shadow;
	}

	/**
	 * @param branchTo
	 *            the branchTo to set
	 */
	public void setBranchTo(ForumThread branchTo) {
		this.branchTo = branchTo;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param poster
	 *            the poster to set
	 */
	public void setPoster(User poster) {
		this.poster = poster;
	}

	/**
	 * @param shadow
	 *            the shadow to set
	 */
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}

	/**
	 * @param thread
	 *            the thread to set
	 */
	public void setThread(ForumThread thread) {
		this.thread = thread;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	public String getPosterVisibleName() {
		return poster != null ? poster.getUsername() : "System";
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Post in " + getThread().getTitle() + " by "
				+ getPosterVisibleName() + " at " + getTime().toString();
	}

	public void setUnreadForumPost(List<UnreadForumPost> unreadForumPosts) {
		this.unreadForumPosts = unreadForumPosts;
	}

	public List<UnreadForumPost> getUnreadForumPost() {
		if (unreadForumPosts == null) {
			return new ArrayList<UnreadForumPost>();
		}
		return unreadForumPosts;
	}

	// $GS$

}
