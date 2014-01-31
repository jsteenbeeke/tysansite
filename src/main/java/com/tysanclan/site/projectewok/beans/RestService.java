package com.tysanclan.site.projectewok.beans;

import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;

public interface RestService {
	AuthorizedRestApplication getClient(String clientId);

	AuthorizedRestApplication createApplication(String name);

	String createChallenge(AuthorizedRestApplication application);

	void timeoutChallenges();

	AuthorizedRestApplication consumeChallengeIfValid(String challenge,
			String response);

	void deactivateApplication(AuthorizedRestApplication application);

	void deleteApplication(AuthorizedRestApplication application);

	void activateApplication(AuthorizedRestApplication application);
}
