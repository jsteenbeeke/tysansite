package com.tysanclan.site.projectewok.util;


import com.jeroensteenbeeke.lux.TypedResult;

import javax.annotation.Nonnull;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;
import java.util.function.BiFunction;

/**
 * Utility class for creating OWASP-grade secure password hashes
 */
public final class OWASPPasswordHasher {
	private static final int SALT_LENGTH = 32;

	/**
	 * Allowed key lengths
	 */
	public enum KeyLength {
		Length256(256),
		Length384(384),
		Length512(512),
		Length768(768),
		Length1024(1024);

		private final int length;

		KeyLength(int length) {
			this.length = length;
		}

		/**
		 * Gets the length in bytes of the current KeyLength
		 *
		 * @return The key length to use. Generally this is a (sum of) multiple(s) of 2
		 */
		public int getLength() {
			return length;
		}
	}

	private OWASPPasswordHasher() {

	}

	/**
	 * Container object for the hash and salt, both Base64-encoded
	 */
	public static final class HashAndSalt {
		private final String hash;

		private final byte[] salt;

		private HashAndSalt(byte[] hash, byte[] salt) {
			this.hash = Base64.getEncoder().encodeToString(hash);
			this.salt = salt;
		}

		/**
		 * @return The base64 encoded hash
		 */
		public String getHash() {
			return hash;
		}

		/**
		 * @return The base64 encoded salt
		 */
		public byte[] getSalt() {
			return salt;
		}

	}

	/**
	 * Hash a new password (i.e. a password which does not yet have a corresponding salt). You should generally call
	 * this
	 * method when creating a new user, or when a user changes their password to avoid reusing the salt.
	 *
	 * @param password The password to hash
	 * @return A builder that will ask for further information
	 */
	public static NewPasswordHashBuilderStep1 hashNewPassword(String password) {
		return new NewPasswordHashBuilderStep1(password);

	}

	/**
	 * Hash an existing password (i.e. a password which has a corresponding salt)
	 *
	 * @param password The password to hash
	 * @return A builder that will ask for further information
	 */
	public static ExistingPasswordHashBuilderStep1 hashExistingPassword(String password) {
		return new ExistingPasswordHashBuilderStep1(password);

	}

	private static TypedResult<byte[]> hashPassword(final char[] password,
													final byte[] salt, final int iterations, final int keyLength) {
		return TypedResult.attempt(() -> {
			SecretKeyFactory skf = SecretKeyFactory
					.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password, salt, iterations,
											 keyLength);
			SecretKey key = skf.generateSecret(spec);

			return key.getEncoded();
		});
	}

	/**
	 * First step in the password hash builder for existing passwords. Asks for the salt to use
	 */
	public static class ExistingPasswordHashBuilderStep1 {
		private final String password;

		private ExistingPasswordHashBuilderStep1(String password) {
			this.password = password;
		}

		/**
		 * Sets the salt to use for hashing the password
		 *
		 * @param salt The salt to use
		 * @return A builder that will continue the hashing process
		 */
		public PasswordHashBuilderStep2<String> withSalt(byte[] salt) {
			return new PasswordHashBuilderStep2<>((pw, s) -> Base64.getEncoder().encodeToString(pw), password, salt);
		}
	}

	/**
	 * First step in the password hash builder. Asks for the salt to use
	 */
	public static class NewPasswordHashBuilderStep1 {

		private final String password;

		private NewPasswordHashBuilderStep1(String password) {
			this.password = password;
		}

		/**
		 * Sets the salt length to use for hashing the password
		 *
		 * @param length The number of bytes to use for the salt
		 * @return A builder that will continue the hashing process
		 */
		public PasswordHashBuilderStep2<HashAndSalt> withSaltLength(int length) {
			return new PasswordHashBuilderStep2<>(HashAndSalt::new, password, Randomizer.randomBytes(length));
		}
	}

	/**
	 * Second step in the password hash builder. Asks for the key length to use
	 *
	 * @param <T> The type of result given once the builder sequence has been successfully completed
	 */
	public static class PasswordHashBuilderStep2<T> {
		private final BiFunction<byte[], byte[], T> finalizerFunction;

		private final String password;

		private final byte[] salt;

		private PasswordHashBuilderStep2(BiFunction<byte[], byte[], T> finalizerFunction, String password, byte[]
				salt) {
			this.finalizerFunction = finalizerFunction;
			this.password = password;
			this.salt = salt;
		}

		/**
		 * Sets the key length to use for the hash algorithm
		 *
		 * @param keyLength The length to use. Should not be {@code null}
		 * @return A builder that will finalize the hashing process
		 */
		public PasswordHashBuilderStep3<T> withKeyLength(@Nonnull KeyLength keyLength) {

			return new PasswordHashBuilderStep3<>(finalizerFunction, keyLength.getLength(), password, salt);
		}
	}

	/**
	 * Second step in the password hash builder. Asks for the number of iterations to use
	 *
	 * @param <T> The type of result given once the builder sequence has been successfully completed
	 */
	public static class PasswordHashBuilderStep3<T> {
		private final BiFunction<byte[], byte[], T> finalizerFunction;

		private final int keyLength;

		private final String password;

		private final byte[] salt;

		private PasswordHashBuilderStep3(BiFunction<byte[], byte[], T> finalizerFunction, int keyLength,
										 String password, byte[] salt) {
			this.finalizerFunction = finalizerFunction;
			this.keyLength = keyLength;
			this.password = password;
			this.salt = salt;
		}

		/**
		 * Sets the number of iterations to use, and calculates the hash
		 *
		 * @param iterations The iterations to use
		 * @return A TypedResult, either containing the indicated return type, or an error message indicating why it
		 * could not be calculated
		 */
		public TypedResult<T> andIterations(int iterations) {
			return hashPassword(password.toCharArray(), salt, iterations, keyLength)
					.map(pw -> finalizerFunction.apply(pw, salt));
		}
	}

}

