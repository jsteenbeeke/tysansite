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

import javax.persistence.Column;
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
import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class YoutubeGalleryItem extends BaseDomainObject implements
		DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "YoutubeGalleryItem")
	@SequenceGenerator(name = "YoutubeGalleryItem", sequenceName = "SEQ_ID_YoutubeGalleryItem")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_YoutubeGalleryItem_user")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@Index(name = "IDX_YoutubeGalleryItem_group")
	private Group group;

	@Column
	private String url;

	@Column
	private String description;

	// $P$

	/**
	 * Creates a new YoutubeGalleryItem object
	 */
	public YoutubeGalleryItem() {
		// $H$
	}

	/**
	 * Returns the ID of this YoutubeGalleryItem
	 */
	@Override
	public Serializable getDomainObjectId() {
		return getId();
	}

	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this YoutubeGalleryItem
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The User of this YoutubeGalleryItem
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this YoutubeGalleryItem
	 * 
	 * @param user
	 *            The User of this YoutubeGalleryItem
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The Url of this YoutubeGalleryItem
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Sets the Url of this YoutubeGalleryItem
	 * 
	 * @param url
	 *            The Url of this YoutubeGalleryItem
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(Group group) {
		this.group = group;
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

	// $GS$
}
