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
package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.RestToken;
import com.tysanclan.site.projectewok.entities.dao.RestTokenDAO;
import com.tysanclan.site.projectewok.entities.filter.RestTokenFilter;
import io.vavr.collection.Seq;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("request")
class RestTokenDAOImpl extends HibernateDAO<RestToken, RestTokenFilter>
		implements RestTokenDAO {

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void cleanExpiredTokens() {
		RestTokenFilter filter = new RestTokenFilter();
		filter.expires().lessThan(System.currentTimeMillis());

		Seq<RestToken> tokens = findByFilter(filter);

		for (RestToken token : tokens) {
			delete(token);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateTokenExpiry(String hash) {
		RestTokenFilter filter = new RestTokenFilter();
		filter.expires().greaterThanOrEqualTo(System.currentTimeMillis());
		filter.hash(hash);

		RestToken token = getUniqueByFilter(filter).getOrNull();

		if (token != null) {
			token.refreshToken();
			update(token);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public RestToken getToken(String hash) {
		RestTokenFilter filter = new RestTokenFilter();
		filter.expires().greaterThanOrEqualTo(System.currentTimeMillis());
		filter.hash(hash);

		return getUniqueByFilter(filter).getOrNull();
	}
}
