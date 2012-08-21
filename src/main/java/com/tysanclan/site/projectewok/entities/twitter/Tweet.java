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
package com.tysanclan.site.projectewok.entities.twitter;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Type;

import com.jeroensteenbeeke.hyperion.data.BaseDomainObject;
import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class Tweet extends BaseDomainObject implements DomainObject, ITweet {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TwitterStatus")
	@SequenceGenerator(name = "TwitterStatus", sequenceName = "SEQ_ID_TwitterStatus")
	private Long id;

	@Column(nullable = false)
	private long twitterId;

	@ManyToOne(optional = true)
	private User user; // null == not made by a member

	@Column(nullable = false)
	private Date posted;

	@Column(nullable = false, length = 255)
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String contents;

	@Column(nullable = false)
	private String screenName;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String source;

	// $P$

	/**
	 * Creates a new TwitterStatus object
	 */
	public Tweet() {
		// $H$
	}

	/**
	 * Returns the ID of this TwitterStatus
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The User of this TwitterStatus
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this TwitterStatus
	 * 
	 * @param user
	 *            The User of this TwitterStatus
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The Posted of this TwitterStatus
	 */
	@Override
	public Date getPosted() {
		return this.posted;
	}

	/**
	 * Sets the Posted of this TwitterStatus
	 * 
	 * @param posted
	 *            The Posted of this TwitterStatus
	 */
	public void setPosted(Date posted) {
		this.posted = posted;
	}

	/**
	 * @return The Contents of this TwitterStatus
	 */
	@Override
	public String getContents() {
		return this.contents;
	}

	/**
	 * Sets the Contents of this TwitterStatus
	 * 
	 * @param contents
	 *            The Contents of this TwitterStatus
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * @return the screenName
	 */
	@Override
	public String getScreenName() {
		return screenName;
	}

	/**
	 * @param screenName
	 *            the screenName to set
	 */
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the source
	 */
	@Override
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the twitterId
	 */
	public long getTwitterId() {
		return twitterId;
	}

	/**
	 * @param twitterId
	 *            the twitterId to set
	 */
	public void setTwitterId(long twitterId) {
		this.twitterId = twitterId;
	}

	// $GS$
}
