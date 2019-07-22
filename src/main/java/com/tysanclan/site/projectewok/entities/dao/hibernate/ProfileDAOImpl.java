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
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.Profile;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.filter.ProfileFilter;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ProfileDAOImpl extends HibernateDAO<Profile, ProfileFilter>
		implements com.tysanclan.site.projectewok.entities.dao.ProfileDAO {

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.ProfileDAO#getSkypeUsers()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<User> getSkypeUsers() {
		ProfileFilter filter = new ProfileFilter();
		filter.instantMessengerAddress().isNotNull();
		filter.user(new UserFilter().rank(Rank.CHANCELLOR).orRank(Rank.SENATOR)
				.orRank(Rank.TRUTHSAYER).orRank(Rank.REVERED_MEMBER)
				.orRank(Rank.SENIOR_MEMBER).orRank(Rank.FULL_MEMBER)
				.orRank(Rank.JUNIOR_MEMBER).orRank(Rank.TRIAL)

		);

		return properties(filter.user()).toJavaList();
	}
}
