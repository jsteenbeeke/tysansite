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
package com.tysanclan.site.projectewok.beans;

import java.math.BigDecimal;

import com.tysanclan.site.projectewok.entities.GalleryImage;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.YoutubeGalleryItem;

/**
 * @author Jeroen Steenbeeke
 */
public interface GalleryService {
	public static class QuotaExceedException extends Exception {
		private static final long serialVersionUID = 1L;

		public QuotaExceedException(String message) {
			super(message);
		}
	}

	BigDecimal getMaxGallerySizeMB();

	BigDecimal getCurrentGallerySizeMB(User user);

	BigDecimal getCurrentGallerySizeMB(Group group);

	GalleryImage uploadImage(String description, User user, Group group,
			byte[] data) throws QuotaExceedException;

	void deleteImage(GalleryImage image);

	void deleteVideo(YoutubeGalleryItem video);

	YoutubeGalleryItem addVideo(User user, Group group, String description,
			String url);

	String getUrlBase();

	String getGalleryPath();

}
