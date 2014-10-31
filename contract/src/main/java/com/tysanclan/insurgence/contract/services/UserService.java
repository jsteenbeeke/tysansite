package com.tysanclan.insurgence.contract.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.tysanclan.insurgence.contract.datamodel.RSUser;
import com.tysanclan.insurgence.contract.datamodel.RSUserRank;

@Path("/users")
public interface UserService {
	@GET
	@Produces("text/json")
	public List<RSUser> getUsers();

	@GET
	@Consumes("text/json")
	@Produces("text/json")
	public List<RSUser> getUsersByRank(RSUserRank rank);
}
