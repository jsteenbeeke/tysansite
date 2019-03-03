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
import java.util.Date;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@Table(indexes = { @Index(columnList = "bug_id", name = "IDX_BUGCOMMENT_BUG"),
		@Index(columnList = "commenter_id", name = "IDX_BUGCOMMENT_COMMENTER") })
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class BugComment extends BaseDomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BugComment")
	@SequenceGenerator(name = "BugComment", sequenceName = "SEQ_ID_BugComment")
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Bug bug;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private User commenter;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	// $P$

	/**
	 * Creates a new BugComment object
	 */
	public BugComment() {
		// $H$
	}

	/**
	 * Returns the ID of this BugComment
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this BugComment
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public Bug getBug() {
		return bug;
	}

	public void setBug(Bug bug) {
		this.bug = bug;
	}

	public User getCommenter() {
		return commenter;
	}

	public void setCommenter(User commenter) {
		this.commenter = commenter;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	// $GS$
}
