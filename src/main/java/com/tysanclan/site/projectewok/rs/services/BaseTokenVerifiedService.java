package com.tysanclan.site.projectewok.rs.services;

import javax.inject.Inject;

import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RestTokenDAO;
import com.tysanclan.site.projectewok.rs.HttpStatusException;

public abstract class BaseTokenVerifiedService {
	@Inject
	private RestTokenDAO tokenDAO;

	public void setTokenDAO(RestTokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}

	public User verifyToken(String tokenString) {
		User user = tokenDAO.getTokenUser(tokenString);

		if (user == null) {
			throw new HttpStatusException(401, "Invalid or expired token");
		}

		return user;
	}
}
