package com.tysanclan.site.projectewok.beans.impl;

import com.tysanclan.rest.api.util.Challenge;
import com.tysanclan.rest.api.util.HashException;
import com.tysanclan.site.projectewok.beans.RestService;
import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;
import com.tysanclan.site.projectewok.entities.RestApplicationChallenge;
import com.tysanclan.site.projectewok.entities.dao.AuthorizedRestApplicationDAO;
import com.tysanclan.site.projectewok.entities.dao.RestApplicationChallengeDAO;
import com.tysanclan.site.projectewok.entities.dao.RestTokenDAO;
import com.tysanclan.site.projectewok.entities.filter.AuthorizedRestApplicationFilter;
import com.tysanclan.site.projectewok.entities.filter.RestApplicationChallengeFilter;
import com.tysanclan.site.projectewok.util.StringUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
@Scope("request")
class RestServiceImpl implements RestService {
	@Autowired
	private AuthorizedRestApplicationDAO applicationDAO;

	@Autowired
	private RestApplicationChallengeDAO challengeDAO;

	@Autowired
	private RestTokenDAO tokenDAO;

	public void setApplicationDAO(AuthorizedRestApplicationDAO applicationDAO) {
		this.applicationDAO = applicationDAO;
	}

	public void setChallengeDAO(RestApplicationChallengeDAO challengeDAO) {
		this.challengeDAO = challengeDAO;
	}

	public void setTokenDAO(RestTokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}

	public RestServiceImpl() {
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void cleanExpiredTokens() {
		tokenDAO.cleanExpiredTokens();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void timeoutChallenges() {
		RestApplicationChallengeFilter filter = new RestApplicationChallengeFilter();
		filter.issueDate().lessThan(new DateTime().minusMinutes(5).toDate());

		for (RestApplicationChallenge challenge : challengeDAO
				.findByFilter(filter)) {
			challengeDAO.delete(challenge);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void activateApplication(AuthorizedRestApplication application) {
		application.setActive(true);
		applicationDAO.update(application);

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deactivateApplication(AuthorizedRestApplication application) {
		application.setActive(false);
		applicationDAO.update(application);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteApplication(AuthorizedRestApplication application) {
		applicationDAO.delete(application);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public AuthorizedRestApplication createApplication(String name) {
		AuthorizedRestApplicationFilter filter = new AuthorizedRestApplicationFilter();
		filter.name(name);

		if (applicationDAO.countByFilter(filter) == 0) {
			long found = -1;

			String clientId = StringUtil.generateRequestKey(8, 0);

			while (found != 0) {

				filter = new AuthorizedRestApplicationFilter();
				filter.clientId(clientId);

				found = applicationDAO.countByFilter(filter);
			}

			final String clientSecret = StringUtil.generateRequestKey(25, 3);

			AuthorizedRestApplication app = new AuthorizedRestApplication();
			app.setActive(true);
			app.setClientId(clientId);
			app.setClientSecret(clientSecret);
			app.setName(name);
			applicationDAO.save(app);

			return app;
		}

		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public AuthorizedRestApplication getClient(String clientId) {
		AuthorizedRestApplicationFilter filter = new AuthorizedRestApplicationFilter();
		filter.active(true);
		filter.clientId(clientId);

		return applicationDAO.getUniqueByFilter(filter).getOrNull();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public AuthorizedRestApplication consumeChallengeIfValid(String challenge,
			String response) {
		RestApplicationChallengeFilter filter = new RestApplicationChallengeFilter();
		filter.challengeString(challenge);
		filter.expectedResponse(response);

		return challengeDAO.getUniqueByFilter(filter).map(foundChallenge -> {
			AuthorizedRestApplication application = foundChallenge
					.getApplication();
			challengeDAO.delete(foundChallenge);

			return application;
		}).getOrNull();

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public String createChallenge(AuthorizedRestApplication application) {
		try {
			String c = StringUtil.generateRequestKey(32, 0);
			RestApplicationChallenge challenge = new RestApplicationChallenge();
			challenge.setApplication(application);
			challenge.setChallengeString(c);
			challenge.setExpectedResponse(
					Challenge.forClient(application.getClientId())
							.withSecret(application.getClientSecret())
							.getResponse(c));
			challenge.setIssueDate(new Date());
			challengeDAO.save(challenge);

			return c;
		} catch (HashException e) {
			return null;
		}
	}
}
