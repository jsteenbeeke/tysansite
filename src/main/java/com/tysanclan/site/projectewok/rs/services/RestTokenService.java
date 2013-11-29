package com.tysanclan.site.projectewok.rs.services;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.QueryParam;

import org.springframework.context.annotation.Scope;

import com.tysanclan.rest.api.data.Token;
import com.tysanclan.rest.api.services.TokenService;
import com.tysanclan.site.projectewok.entities.RestToken;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RestTokenDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.rs.HttpStatusException;

@Named
@Scope("request")
public class RestTokenService implements TokenService {
	@Inject
	private RestTokenDAO tokenDAO;

	@Inject
	private UserDAO userDAO;

	public void setTokenDAO(RestTokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public Token getToken(@QueryParam("u") String username,
			@QueryParam("p") String password) {
		if (username != null && password != null) {
			User user = userDAO.load(username, password);

			if (user != null) {
				RestToken token = new RestToken(user);
				tokenDAO.save(token);

				return new Token(token.getUser().getUsername(),
						token.getHash(), token.getExpires());
			}
		}

		throw new HttpStatusException(401, "Invalid Username and/or Password");
	}
}
