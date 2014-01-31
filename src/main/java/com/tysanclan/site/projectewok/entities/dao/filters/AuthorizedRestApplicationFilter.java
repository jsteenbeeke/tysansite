package com.tysanclan.site.projectewok.entities.dao.filters;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.AuthorizedRestApplication;

public class AuthorizedRestApplicationFilter extends
		SearchFilter<AuthorizedRestApplication> {
	private static final long serialVersionUID = 1L;

	private String clientId;

	private String clientSecret;

	private String name;

	private Boolean active;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
