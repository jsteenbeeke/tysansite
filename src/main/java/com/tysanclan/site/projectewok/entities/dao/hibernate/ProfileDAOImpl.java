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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.Profile;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ProfileDAOImpl extends EwokHibernateDAO<Profile> implements
		com.tysanclan.site.projectewok.entities.dao.ProfileDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<Profile> filter) {
		Criteria criteria = getSession().createCriteria(Profile.class);

		// if (filter instanceof ProfileFilter) {
		// ProfileFilter cf = (ProfileFilter) filter;
		// }

		return criteria;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.ProfileDAO#getAIMUsers()
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<User> getAIMUsers() {
		Criteria criteria = getSession().createCriteria(User.class);

		criteria.createAlias("profile", "profile");
		criteria.add(Restrictions.isNotNull("profile.instantMessengerAddress"));
		criteria.add(Restrictions.in("rank", new Rank[] { Rank.CHANCELLOR,
				Rank.SENATOR, Rank.TRUTHSAYER, Rank.REVERED_MEMBER,
				Rank.SENIOR_MEMBER, Rank.FULL_MEMBER, Rank.JUNIOR_MEMBER,
				Rank.TRIAL }));
		criteria.addOrder(Order.asc("username"));

		return criteria.list();
	}
}
