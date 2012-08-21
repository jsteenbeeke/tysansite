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

import java.io.File;

import javax.persistence.*;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.tysanclan.site.projectewok.util.ImageUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONE)
public class GalleryImage implements DomainObject {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GalleryImage")
	@SequenceGenerator(name = "GalleryImage", sequenceName = "SEQ_ID_GalleryImage")
	private Long id;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@Index(name = "IDX_GALLERYIMAGE_USER")
	private User user;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@Index(name = "IDX_GALLERYIMAGE_GROUP")
	private Group group;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String mimeType;

	// $P$
	/**
	 * Creates a new GalleryImage object
	 */
	public GalleryImage() {
		// $H$
	}

	/**
	 * Returns the ID of this GalleryImage
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the ID of this GalleryImage
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return The User of this GalleryImage
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Sets the User of this GalleryImage
	 * 
	 * @param user
	 *            The User of this GalleryImage
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return The Group of this GalleryImage
	 */
	public Group getGroup() {
		return this.group;
	}

	/**
	 * Sets the Group of this GalleryImage
	 * 
	 * @param group
	 *            The Group of this GalleryImage
	 */
	public void setGroup(Group group) {
		this.group = group;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Transient
	public String getImageFilename() {
		return "/" + getId() + ImageUtil.getExtensionByMimeType(getMimeType());
	}

	@Transient
	public String getThumbnailFilename() {
		return "/" + getId() + ".thumb"
				+ ImageUtil.getExtensionByMimeType(getMimeType());
	}

	public long getFileSize(String path) {
		long size = 0L;

		File thumb = new File(path + getThumbnailFilename());
		if (thumb.exists()) {
			size += thumb.length();
		}

		File img = new File(path + getImageFilename());
		if (img.exists()) {
			size += img.length();
		}

		return size;
	}

	// $GS$
}
