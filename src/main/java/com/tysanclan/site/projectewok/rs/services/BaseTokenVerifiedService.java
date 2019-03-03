package com.tysanclan.site.projectewok.rs.services;

import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;
import com.tysanclan.site.projectewok.entities.RestToken;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RestTokenDAO;
import com.tysanclan.site.projectewok.rs.HttpStatusException;

import javax.inject.Inject;

public abstract class BaseTokenVerifiedService {
	@Inject
	private RestTokenDAO tokenDAO;

	public void setTokenDAO(RestTokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}

	public User verifyToken(String tokenString) {
		RestToken token = tokenDAO.getToken(tokenString);
		User user = token.getUser();

		if (user == null) {
			throw new HttpStatusException(401, "Invalid or expired token");
		}

		AuthorizedRestApplication application = token.getApplication();

		if (application.isActive()) {
			throw new HttpStatusException(401, "Application access withdrawn");
		}

		return user;
	}
}
