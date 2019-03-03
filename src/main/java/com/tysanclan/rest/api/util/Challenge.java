package com.tysanclan.rest.api.util;


public final class Challenge {
	public static class Builder {
		private final String clientId;

		private Builder(String clientId) {
			super();
			this.clientId = clientId;
		}

		public Challenge withSecret(String clientSecret) {
			return new Challenge(clientId, clientSecret);
		}
	}

	public static Builder forClient(String clientId) {
		return new Builder(clientId);
	}

	private final String clientId;

	private final String clientSecret;

	private Challenge(String clientId, String clientSecret) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public String getResponse(String challenge) throws HashException {
		return HashUtil.sha1Hash(String.format("%s!%s!%s", clientId, challenge,
				clientSecret));
	}
}
