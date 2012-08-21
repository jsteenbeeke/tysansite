/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.dao.filters.GameAccountFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GameAccountDAOImpl extends EwokHibernateDAO<GameAccount> implements
		com.tysanclan.site.projectewok.entities.dao.GameAccountDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<GameAccount> filter) {
		Criteria criteria = getSession().createCriteria(GameAccount.class);

		if (filter instanceof GameAccountFilter) {
			GameAccountFilter cf = (GameAccountFilter) filter;

			if (cf.getAccountName() != null) {
				criteria.add(Restrictions.eq("name", cf.getAccountName()));
			}
			if (cf.getChannelWebServiceUID() != null
					|| cf.getBotLinked() != null) {
				criteria.createAlias("userGameRealm", "userGameRealm");
				criteria.createAlias("userGameRealm.realm", "realm");

				if (cf.getChannelWebServiceUID() != null) {
					criteria.createAlias("realm.channels", "channel");

					criteria.add(Restrictions.eq("channel.webServiceUserId",
							cf.getChannelWebServiceUID()));
				}
			}
		}

		return criteria;
	}
}
