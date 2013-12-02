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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.filters.UserGameRealmFilter;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class UserGameRealmDAOImpl extends EwokHibernateDAO<UserGameRealm> implements
		com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<UserGameRealm> filter) {
		Criteria criteria = getSession().createCriteria(UserGameRealm.class);

		if (filter instanceof UserGameRealmFilter) {
			UserGameRealmFilter cf = (UserGameRealmFilter) filter;
			if (cf.getUser() != null) {
				criteria.add(Restrictions.eq("user", cf.getUser()));
			}
			if (cf.getGame() != null) {
				criteria.add(Restrictions.eq("game", cf.getGame()));
			}
			if (cf.getRealm() != null) {
				criteria.add(Restrictions.eq("realm", cf.getRealm()));
			}
		}

		return criteria;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO#getActiveUsers(com.tysanclan.site.projectewok.entities.Game,
	 *      com.tysanclan.site.projectewok.entities.Realm)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getActiveUsers(Game game, Realm realm) {
		Criteria criteria = getSession().createCriteria(User.class);

		criteria.add(Restrictions.in("rank", MemberUtil.getMemberRanks()));

		Criteria sub = criteria.createCriteria("playedGames");
		sub.add(Restrictions.eq("game", game));
		sub.add(Restrictions.eq("realm", realm));

		return criteria.list();
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO#countActivePlayers(com.tysanclan.site.projectewok.entities.Realm)
	 */
	@Override
	public int countActivePlayers(Realm realm) {
		Criteria criteria = getSession().createCriteria(UserGameRealm.class);

		criteria.createAlias("user", "user");
		criteria.add(Restrictions.in("user.rank", MemberUtil.getMemberRanks()));

		criteria.add(Restrictions.eq("realm", realm));
		criteria.setProjection(Projections.countDistinct("user"));

		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public int countActivePlayers(Game game) {
		Criteria criteria = getSession().createCriteria(UserGameRealm.class);

		criteria.add(Restrictions.eq("game", game));
		criteria.setProjection(Projections.countDistinct("user"));

		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public void removeUserGameRealmsByRealm(Realm realm) {
		Query query = getSession().createQuery(
				"delete from UserGameRealm where realm = :realm");
		query.setEntity("realm", realm);
		query.executeUpdate();
	}

	@Override
	public void removeUserGameRealmsByGame(Game game) {
		Query query = getSession().createQuery(
				"delete from UserGameRealm where game = :game");
		query.setEntity("game", game);
		query.executeUpdate();
	}
}
