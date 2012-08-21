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
package com.tysanclan.site.projectewok.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeroen Steenbeeke
 */
public class ImageUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

	public static byte[] resize(byte[] image, int targetWidth, int targetHeight) {
		InputStream imageStream = new ByteArrayInputStream(image);

		BufferedImage sourceImage;

		try {
			sourceImage = (BufferedImage) ImageIO.read(imageStream);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			return image;
		}

		int thumbWidth = targetWidth;
		int thumbHeight = targetHeight;

		// Make sure the aspect ratio is maintained, so the image is not skewed
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = sourceImage.getWidth(null);
		int imageHeight = sourceImage.getHeight(null);

		if (imageWidth > thumbWidth || imageHeight > thumbHeight) {

			double imageRatio = (double) imageWidth / (double) imageHeight;
			if (thumbRatio < imageRatio) {
				thumbHeight = (int) (thumbWidth / imageRatio);
			} else {
				thumbWidth = (int) (thumbHeight * imageRatio);
			}

			BufferedImage thumbImage = new BufferedImage(thumbWidth,
					thumbHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = thumbImage.createGraphics();

			AffineTransform at = AffineTransform.getScaleInstance(
					(double) thumbWidth / sourceImage.getWidth(null),
					(double) thumbHeight / sourceImage.getHeight(null));
			graphics2D.drawRenderedImage(sourceImage, at);

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try {
				ImageIO.write(thumbImage, getBlobType(image), out);

				out.flush();

				byte[] newImage = out.toByteArray();

				return newImage;

			} catch (IOException e) {
				log.error(e.getMessage(), e);
				return image;
			}
		}

		return image;
	}

	/**
	 * Checks if an image is a GIF file
	 * 
	 * @param image
	 *            The image to check
	 * @return {@code true} if we suspect this file is a GIF file, {@code false}
	 *         otherwise
	 */
	public static boolean isGIFImage(byte[] image) {
		return image.length > 6 && image[0] == (byte) 0x47
				&& image[1] == (byte) 0x49 && image[2] == (byte) 0x46
				&& image[3] == (byte) 0x38
				&& (image[4] == (byte) 0x39 || image[4] == (byte) 0x37)
				&& image[5] == (byte) 0x61;
	}

	public static boolean isJPEGImage(byte[] image) {
		return image.length > 2 && image[0] == (byte) 0xFF
				&& image[1] == (byte) 0xD8;
	}

	public static boolean isPNGImage(byte[] image) {
		return image.length > 8 && image[0] == -119 && image[1] == 80
				&& image[2] == 78 && image[3] == 71 && image[4] == 13
				&& image[5] == 10 && image[6] == 26 && image[7] == 10;
	}

	public static Dimension getImageDimensions(byte[] image) {
		if (isGIFImage(image) || isJPEGImage(image) || isPNGImage(image)) {
			InputStream imageStream = new ByteArrayInputStream(image);
			Image img;

			try {
				img = (Image) ImageIO.read(imageStream);
			} catch (IOException e) {
				return new Dimension(0, 0);
			}

			return new Dimension(img.getWidth(null), img.getHeight(null));

		}

		return new Dimension(0, 0);
	}

	public static String getBlobType(byte[] image) {
		if (isJPEGImage(image)) {
			return "jpg";
		} else if (isPNGImage(image)) {
			return "png";
		} else if (isGIFImage(image)) {
			return "gif";
		}

		return "png";
	}

	public static String getBlobType(Blob blob) {
		try {
			byte[] image = blob.getBytes(0, (int) blob.length());

			return getBlobType(image);
		} catch (SQLException e) {
			// Silent ignore
		}

		return "png";
	}

	public static String getMimeType(byte[] image) {
		if (isJPEGImage(image)) {
			return "image/jpeg";
		} else if (isPNGImage(image)) {
			return "image/png";
		} else if (isGIFImage(image)) {
			return "image/gif";
		}

		return "application/octet-stream";
	}

	public static String getExtensionByMimeType(String mimeType) {
		if ("image/jpeg".equals(mimeType)) {
			return ".jpg";
		} else if ("image/png".equals(mimeType)) {
			return ".png";
		} else if ("image/gif".equals(mimeType)) {
			return ".gif";
		}

		return ".bin";
	}

}
