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
package com.tysanclan.site.projectewok.beans.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.beans.GalleryService;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.entities.GalleryImage;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.YoutubeGalleryItem;
import com.tysanclan.site.projectewok.entities.dao.GalleryImageDAO;
import com.tysanclan.site.projectewok.entities.dao.GroupDAO;
import com.tysanclan.site.projectewok.entities.dao.YoutubeGalleryItemDAO;
import com.tysanclan.site.projectewok.pages.member.YouTubeUrlValidator;
import com.tysanclan.site.projectewok.util.ImageUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Scope("request")
class GalleryServiceImpl implements GalleryService {
	private static final Logger log = LoggerFactory
			.getLogger(GalleryServiceImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private GalleryImageDAO galleryImageDAO;

	@Autowired
	private YoutubeGalleryItemDAO youtubeGalleryItemDAO;

	@Autowired
	private GroupDAO groupDAO;

	private String galleryPath;

	private String urlBase;

	private String sizeBase;

	@Autowired
	public GalleryServiceImpl(String urlBase, String galleryPath,
			String sizeBase) {
		this.urlBase = urlBase;
		this.galleryPath = galleryPath;
		this.sizeBase = sizeBase;
	}

	@Override
	public String getGalleryPath() {
		return galleryPath;
	}

	/**
	 * @param groupDAO
	 *            the groupDAO to set
	 */
	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	/**
	 * @param galleryImageDAO
	 *            the galleryImageDAO to set
	 */
	public void setGalleryImageDAO(GalleryImageDAO galleryImageDAO) {
		this.galleryImageDAO = galleryImageDAO;
	}

	/**
	 * @param userService
	 *            the membershipService to set
	 */
	public void setMembershipService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param youtubeGalleryItemDAO
	 *            the youtubeGalleryItemDAO to set
	 */
	public void setYoutubeGalleryItemDAO(
			YoutubeGalleryItemDAO youtubeGalleryItemDAO) {
		this.youtubeGalleryItemDAO = youtubeGalleryItemDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GalleryService#getMaxGallerySizeMB()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal getMaxGallerySizeMB() {
		boolean debugMode = System.getProperty("tysan.debug") != null;

		long totalSizeInBytes = debugMode ? 19037388000L : new File(sizeBase)
				.getUsableSpace();

		for (GalleryImage image : galleryImageDAO.findAll()) {
			totalSizeInBytes += image.getFileSize(galleryPath);
		}

		long totalSizeInKB = totalSizeInBytes / 1024;
		long totalSizeInMB = totalSizeInKB / 1024;

		long reservedSize = debugMode ? 0 : 5000;

		long totalSpendableSize = totalSizeInMB - reservedSize;

		int members = userService.countMembers();
		int groups = groupDAO.countAll();

		return new BigDecimal(totalSpendableSize).divide(
				new BigDecimal(2).multiply(new BigDecimal(members + groups)),
				2, RoundingMode.HALF_UP);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GalleryImage uploadImage(String description, User user, Group group,
			byte[] data) throws QuotaExceedException {
		if (description == null) {
			return null;
		}

		if (!ImageUtil.isPNGImage(data) && !ImageUtil.isJPEGImage(data)
				&& !ImageUtil.isGIFImage(data)) {
			return null;
		}

		int thumbWidth = 240;
		int thumbHeight = 192;

		byte[] thumb = ImageUtil.resize(data, thumbWidth, thumbHeight);

		BigDecimal uploadSize = new BigDecimal(thumb.length + data.length)
				.divide(new BigDecimal(1024).pow(2), 2, RoundingMode.HALF_UP);

		if (user != null) {
			if (getCurrentGallerySizeMB(user).add(uploadSize).compareTo(
					getMaxGallerySizeMB()) < 0) {

				GalleryImage img = new GalleryImage();
				img.setDescription(description);
				img.setUser(user);
				img.setMimeType(ImageUtil.getMimeType(data));
				galleryImageDAO.save(img);

				saveImage(data, thumb, img);

				return img;
			}
			throw new QuotaExceedException("Not enough remaining image space");

		} else if (group != null) {
			if (getCurrentGallerySizeMB(group).add(uploadSize).compareTo(
					getMaxGallerySizeMB()) < 0) {
				GalleryImage img = new GalleryImage();
				img.setDescription(description);
				img.setMimeType(ImageUtil.getMimeType(data));
				img.setGroup(group);
				galleryImageDAO.save(img);

				saveImage(data, thumb, img);

				return img;
			}

			throw new QuotaExceedException("Not enough remaining image space");

		}

		return null;
	}

	protected void saveImage(byte[] data, byte[] thumb, GalleryImage img) {
		File imgFile = new File(getGalleryPath() + img.getImageFilename());
		File thumbFile = new File(getGalleryPath() + img.getThumbnailFilename());

		FileOutputStream fos;

		try {
			fos = new FileOutputStream(imgFile);
			fos.write(data);
			fos.flush();
			fos.close();

			fos = new FileOutputStream(thumbFile);
			fos.write(thumb);
			fos.flush();
			fos.close();

		} catch (IOException ioe) {
			galleryImageDAO.delete(img);

			if (imgFile.exists())
				log.trace("Deleting image file: " + imgFile.delete());

			if (thumbFile.exists())
				log.trace("Deleting image file: " + thumbFile.delete());
		}
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GalleryService#getCurrentGallerySizeMB(com.tysanclan.site.projectewok.entities.Group)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal getCurrentGallerySizeMB(Group group) {
		long curSize = 0L;

		for (GalleryImage image : group.getGalleryImages()) {
			curSize += image.getFileSize(galleryPath);
		}

		return new BigDecimal(curSize).divide(new BigDecimal(1024).pow(2), 2,
				RoundingMode.HALF_UP);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GalleryService#getCurrentGallerySizeMB(com.tysanclan.site.projectewok.entities.User)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public BigDecimal getCurrentGallerySizeMB(User user) {
		long curSize = 0L;

		for (GalleryImage image : user.getGalleryImages()) {
			curSize += image.getFileSize(galleryPath);
		}

		return new BigDecimal(curSize).divide(new BigDecimal(1024).pow(2), 2,
				RoundingMode.HALF_UP);
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GalleryService#deleteImage(com.tysanclan.site.projectewok.entities.GalleryImage)
	 */
	@Override
	public void deleteImage(GalleryImage image) {
		galleryImageDAO.delete(image);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public YoutubeGalleryItem addVideo(User user, Group group,
			String description, String url) {
		Matcher m = YouTubeUrlValidator.REGEX.matcher(url);

		if (m.matches() && m.groupCount() > 0) {
			String key = m.group(1);

			String vuri = "http://www.youtube.com/v/" + key;

			YoutubeGalleryItem item = new YoutubeGalleryItem();
			item.setDescription(description);
			item.setGroup(group);
			item.setUrl(vuri);
			item.setUser(user);

			youtubeGalleryItemDAO.save(item);

			return item;

		}

		return null;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.GalleryService#deleteVideo(com.tysanclan.site.projectewok.entities.YoutubeGalleryItem)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteVideo(YoutubeGalleryItem video) {
		youtubeGalleryItemDAO.delete(video);

	}

	@Override
	public String getUrlBase() {
		return urlBase;
	}
}
