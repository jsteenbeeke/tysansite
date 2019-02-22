/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok.entities.dao.hibernate;

import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.*;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import com.tysanclan.site.projectewok.entities.filter.UserGameRealmFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class UserGameRealmDAOImpl extends HibernateDAO<UserGameRealm, UserGameRealmFilter> implements
		com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO {

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO#getActiveUsers(com.tysanclan.site.projectewok.entities.Game,
	 * com.tysanclan.site.projectewok.entities.Realm)
	 */
	@Override
	public List<User> getActiveUsers(Game game, Realm realm) {
		UserGameRealmFilter filter = createDefaultFilter();
		filter.game(game);
		filter.realm(realm);

		return properties(filter.user(), filter).distinct().toJavaList();
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO#countActivePlayers(com.tysanclan.site.projectewok.entities.Realm)
	 */
	@Override
	public int countActivePlayers(Realm realm) {
		UserGameRealmFilter filter = createDefaultFilter();
		filter.realm(realm);

		return properties(filter.user(), filter).distinct().size();
	}

	protected UserGameRealmFilter createDefaultFilter() {
		UserGameRealmFilter filter = new UserGameRealmFilter();
		filter.user(new UserFilter()
							.rank(Rank.CHANCELLOR)
							.orRank(Rank.SENATOR)
							.orRank(Rank.TRUTHSAYER)
							.orRank(Rank.REVERED_MEMBER)
							.orRank(Rank.SENIOR_MEMBER)
							.orRank(Rank.FULL_MEMBER)
							.orRank(Rank.JUNIOR_MEMBER)
		);
		return filter;
	}

	@Override
	public int countActivePlayers(Game game) {
		UserGameRealmFilter filter = createDefaultFilter();
		filter.game(game);


		return properties(filter.user(), filter).distinct().size();

	}

	@Override
	public void removeUserGameRealmsByRealm(Realm realm) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<UserGameRealm> criteriaDelete = criteriaBuilder.createCriteriaDelete(UserGameRealm.class);
		Root<UserGameRealm> root = criteriaDelete.from(UserGameRealm.class);

		entityManager
				.createQuery(criteriaDelete.where(criteriaBuilder.equal(root.get(UserGameRealm_.realm), realm)))
				.executeUpdate();
	}

	@Override
	public void removeUserGameRealmsByGame(Game game) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaDelete<UserGameRealm> criteriaDelete = criteriaBuilder.createCriteriaDelete(UserGameRealm.class);
		Root<UserGameRealm> root = criteriaDelete.from(UserGameRealm.class);

		entityManager
				.createQuery(criteriaDelete.where(criteriaBuilder.equal(root.get(UserGameRealm_.game), game)))
				.executeUpdate();
	}
}
