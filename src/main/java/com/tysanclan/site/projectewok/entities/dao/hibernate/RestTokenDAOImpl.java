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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.HibernateDAO;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.RestToken;
import com.tysanclan.site.projectewok.entities.dao.RestTokenDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.RestTokenFilter;

@Component
@Scope("request")
class RestTokenDAOImpl extends HibernateDAO<RestToken> implements RestTokenDAO {

	@Override
	protected Criteria createCriteria(SearchFilter<RestToken> filter) {
		Criteria criteria = getSession().createCriteria(RestToken.class);

		if (filter instanceof RestTokenFilter) {
			RestTokenFilter rtf = (RestTokenFilter) filter;

			if (rtf.getExpired() != null) {
				boolean expired = rtf.getExpired().booleanValue();

				if (expired) {
					criteria.add(Restrictions.lt("expires",
							System.currentTimeMillis()));
				} else {
					criteria.add(Restrictions.gt("expires",
							System.currentTimeMillis()));
				}
			}

			if (rtf.getUser() != null) {
				criteria.add(Restrictions.eq("user", rtf.getUser()));
			}

			if (rtf.getHash() != null) {
				criteria.add(Restrictions.eq("hash", rtf.getHash()));
			}
		}

		return criteria;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void cleanExpiredTokens() {
		Criteria criteria = getSession().createCriteria(RestToken.class);
		criteria.add(Restrictions.lt("expires", System.currentTimeMillis()));

		List<RestToken> tokens = listOf(criteria);

		for (RestToken token : tokens) {
			delete(token);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateTokenExpiry(String hash) {
		Criteria criteria = getSession().createCriteria(RestToken.class);
		criteria.add(Restrictions.gt("expires", System.currentTimeMillis()));
		criteria.add(Restrictions.eq("hash", hash));

		RestToken token = unique(criteria);

		if (token != null) {
			token.refreshToken();
			update(token);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public RestToken getToken(String hash) {
		Criteria criteria = getSession().createCriteria(RestToken.class);
		criteria.add(Restrictions.gt("expires", System.currentTimeMillis()));
		criteria.add(Restrictions.eq("hash", hash));

		return unique(criteria);
	}
}
