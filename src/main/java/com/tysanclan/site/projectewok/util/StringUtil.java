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
package com.tysanclan.site.projectewok.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

/**
 * @author Jeroen Steenbeeke
 */
public class StringUtil {
	private static final Logger logger = LoggerFactory
			.getLogger(StringUtil.class);

	private static final Random random = new Random();

	public static String combineStrings(Object... strings) {
		StringBuilder builder = new StringBuilder();
		for (Object string : strings) {
			builder.append(string);
		}

		return builder.toString();
	}

	public static String sha1Hash(String input) {
		try {
			MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
			sha1Digest.update(input.getBytes());
			return byteArrayToString(sha1Digest.digest());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "";
	}

	private static String byteArrayToString(byte[] array) {
		StringBuilder builder = new StringBuilder();

		for (byte next : array) {
			byte lower = (byte) (next & 0x0F);

			byte higher = (byte) ((next & 0xF0) >>> 4);

			builder.append(getHexChar(higher));
			builder.append(getHexChar(lower));
		}

		return builder.toString();
	}

	public static boolean isValidEMail(String email) {
		return !(email.length() < 10 || email.indexOf('@') == -1
				|| email.indexOf('.') == -1 || email.indexOf('@') > email
				.lastIndexOf('.'));
	}

	private static char getHexChar(byte convertible) {
		// System.out.println(convertible);

		switch (convertible) {
			case 10:
				return 'a';
			case 11:
				return 'b';
			case 12:
				return 'c';
			case 13:
				return 'd';
			case 14:
				return 'e';
			case 15:
				return 'f';
			default:
				return combineStrings("", (int) convertible).charAt(0);
		}
	}

	public static String generateRequestKey() {
		StringBuilder key = new StringBuilder();
		int size = random.nextInt(5) + 17;

		for (int i = 0; i < size; i++) {
			int mode = random.nextInt(3);
			switch (mode) {
				case 0:
					// Getal
					key.append((char) (random.nextInt(10) + 48));
					break;
				case 1:
					// UPPERCASE CHAR
					key.append((char) (random.nextInt(26) + 65));
					break;
				case 2:
					// lowercase char
					key.append((char) (random.nextInt(26) + 97));
					break;
			}
		}
		return key.toString();
	}

	public static String twitterify(String message) {
		return twitterify(message, false);
	}

	public static String twitterify(String message, boolean convertLinks) {
		StringBuilder result = new StringBuilder();

		int mode = 0; // 0 = normal, 1 = parsing user link, 2 = parsing group
		// link
		StringBuilder currLink = new StringBuilder();

		for (char next : message.toCharArray()) {
			switch (mode) {
				case 0:
					if (next == '@') {
						mode = 1;
						currLink.append(next);
					} else if (next == '#') {
						mode = 2;
						currLink.append(next);
					} else {
						result.append(next);
					}
					break;
				case 1:
					if (Character.isLetterOrDigit(next) || next == '_') {
						currLink.append(next);
					} else {
						mode1Resolve(result, currLink);
						result.append(next);
						currLink = new StringBuilder();
						mode = 0;
					}
					break;
				case 2:
					if (Character.isLetterOrDigit(next)) {
						currLink.append(next);
					} else {
						mode2Resolve(result, currLink);
						result.append(next);
						currLink = new StringBuilder();
						mode = 0;
					}
					break;
			}
		}

		if (mode == 1) {
			mode1Resolve(result, currLink);
		} else if (mode == 2) {
			mode2Resolve(result, currLink);
		}

		StringBuilder res2 = new StringBuilder();
		if (convertLinks) {
			StringTokenizer tokenizer = new StringTokenizer(result.toString(),
					" ");
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();

				if (res2.length() > 0) {
					res2.append(" ");
				}

				if (token.startsWith("http://") || token.startsWith("https://")) {
					res2.append("<a class=\"Yellow\" href=\"");
					res2.append(token);
					res2.append("\">");
					res2.append(token);
					res2.append("</a>");
				} else {
					res2.append(token);
				}

			}
		} else {
			res2.append(result.toString());
		}

		return res2.toString();
	}

	private static void mode2Resolve(StringBuilder result,
			StringBuilder currLink) {
		result.append("<a class=\"Yellow\" href=\"http://twitter.com/#search?q=%23");
		result.append(urlEncode(currLink.substring(1)));
		result.append("\">");
		result.append(currLink.toString());
		result.append("</a>");
	}

	private static void mode1Resolve(StringBuilder result,
			StringBuilder currLink) {
		result.append("<a class=\"Yellow\" href=\"http://twitter.com/");
		result.append(urlEncode(currLink.substring(1)));
		result.append("\">");
		result.append(currLink.toString());
		result.append("</a>");
	}

	public static String urlEncode(String input) {
		try {
			return URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return input;
		}
	}

	public static String md5hash(String input) {
		try {
			MessageDigest sha1Digest = MessageDigest.getInstance("MD5");
			sha1Digest.update(input.getBytes());
			return byteArrayToString(sha1Digest.digest());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "";
	}

	public static int countWords(String input) {
		List<String> words = new LinkedList<String>();
		String current = "";
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (Character.isWhitespace(c) && !current.isEmpty()) {
				words.add(current);
				current = "";
			} else {
				current += c;
			}
		}
		if (!current.isEmpty()) {
			words.add(current);
		}

		return words.size();
	}

	public static Function<String, String> capitalizeFirstFunction() {
		return new CapitalizeFirstFunction();
	}

	private static final class CapitalizeFirstFunction implements
			Function<String, String> {
		@Override
		@Nullable
		public String apply(@Nullable String input) {

			return input != null ? String.format("%s%s", input.substring(0, 1)
					.toUpperCase(), input.substring(1)) : null;
		}
	}
}
