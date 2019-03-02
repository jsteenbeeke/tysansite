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
package com.tysanclan.site.projectewok.rs.services;

import com.tysanclan.rest.api.data.RestUser;
import com.tysanclan.rest.api.data.Token;
import com.tysanclan.rest.api.services.TokenService;
import com.tysanclan.site.projectewok.beans.RestService;
import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;
import com.tysanclan.site.projectewok.entities.RestToken;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.RestTokenDAO;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.rs.HttpStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

@Component
@Scope("request")
public class RestTokenService implements TokenService {
	@Autowired
	private RestTokenDAO tokenDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RestService restService;

	public void setRestService(RestService restService) {
		this.restService = restService;
	}

	public void setTokenDAO(RestTokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	@GET
	@Path("/challenges")
	@Consumes("application/json")
	@Produces("application/json")
	public String issueChallenge(@QueryParam("c") String clientId) {

		AuthorizedRestApplication application = restService.getClient(clientId);

		if (application != null) {
			return restService.createChallenge(application);
		}

		throw new HttpStatusException(401, "Invalid client ID");
	}

	@GET()
	@Path("/stat")
	@Consumes("application/json")
	@Produces("text/plain")
	public String echo() {
		return restService.getClass().getName();
	}

	@Override
	@GET
	@Consumes("application/json")
	@Produces("application/json")
	public Token getToken(@QueryParam("c") String challenge,
			@QueryParam("r") String response, @QueryParam("u") String username,
			@QueryParam("p") String password) {
		AuthorizedRestApplication application = restService
				.consumeChallengeIfValid(challenge, response);

		if (application != null) {
			if (username != null && password != null) {
				User user = userDAO.load(username, password);

				if (user != null) {
					RestToken token = new RestToken(user);
					token.setApplication(application);
					tokenDAO.save(token);

					return new Token(
							new RestUser(user.getUsername(), user.getRank()),
							token.getHash(), token.getExpires());
				}
			}

			throw new HttpStatusException(401,
					"Invalid Username and/or Password");
		}

		throw new HttpStatusException(401, "Invalid challenge and/or response");
	}
}
