package com.tysanclan.rest.api.util;

import java.security.MessageDigest;

public final class HashUtil {
	private HashUtil() {
	}

	public static String sha1Hash(String input) throws HashException {
		try {
			MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
			sha1Digest.update(input.getBytes());
			return byteArrayToString(sha1Digest.digest());
		} catch (Exception e) {
			throw new HashException(e.getMessage(), e);
		}
	}

	public static String md5hash(String input) throws HashException {
		try {
			MessageDigest sha1Digest = MessageDigest.getInstance("MD5");
			sha1Digest.update(input.getBytes());
			return byteArrayToString(sha1Digest.digest());
		} catch (Exception e) {
			throw new HashException(e.getMessage(), e);
		}

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

	private static char getHexChar(byte convertible) {
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
				return Byte.toString(convertible).charAt(0);
		}
	}
}
