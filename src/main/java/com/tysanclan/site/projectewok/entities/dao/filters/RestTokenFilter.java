package com.tysanclan.site.projectewok.entities.dao.filters;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.RestToken;
import com.tysanclan.site.projectewok.entities.User;

public class RestTokenFilter extends SearchFilter<RestToken> {
	private static final long serialVersionUID = 1L;

	private Boolean expired;

	private IModel<User> user = new Model<User>();

	private String hash;

	public RestTokenFilter() {
	}

	@Override
	public void detach() {
		super.detach();
		user.detach();
	}

	public void setUser(User user) {
		this.user = ModelMaker.wrap(user);
	}

	public User getUser() {
		return user.getObject();
	}

	public Boolean getExpired() {
		return expired;
	}

	public void setExpired(Boolean expired) {
		this.expired = expired;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
}
