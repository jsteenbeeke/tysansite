package com.tysanclan.site.projectewok.entities.dao;

import com.jeroensteenbeeke.hyperion.data.DAO;
import com.tysanclan.site.projectewok.entities.RestToken;
import com.tysanclan.site.projectewok.entities.User;

public interface RestTokenDAO extends DAO<RestToken> {
	User getTokenUser(String hash);

	void updateTokenExpiry(String hash);

	void cleanExpiredTokens();
}
