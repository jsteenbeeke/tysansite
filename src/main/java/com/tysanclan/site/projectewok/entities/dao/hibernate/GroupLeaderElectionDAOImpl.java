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

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.GroupLeaderElection;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.filters.GroupLeaderElectionFilter;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GroupLeaderElectionDAOImpl extends EwokHibernateDAO<GroupLeaderElection>
		implements
		com.tysanclan.site.projectewok.entities.dao.GroupLeaderElectionDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<GroupLeaderElection> filter) {
		Criteria criteria = getSession().createCriteria(
				GroupLeaderElection.class);

		if (filter instanceof GroupLeaderElectionFilter) {
			GroupLeaderElectionFilter f = (GroupLeaderElectionFilter) filter;

			if (f.getStartAfter() != null) {
				criteria.add(Restrictions.ge("start", f.getStartAfter()));
			}
			if (f.getStartBefore() != null) {
				criteria.add(Restrictions.le("start", f.getStartBefore()));
			}
			if (f.getGroup() != null) {
				criteria.add(Restrictions.eq("group", f.getGroup()));
			}
		}

		return criteria;
	}

	@Override
	public void restartElectionsWithParticipant(User participant) {
		for (GroupLeaderElection ge : findAll()) {
			if (ge.getCandidates().contains(participant)) {
				ge.getCandidates().clear();
				ge.setStart(new Date());
				update(ge);
			}
		}
	}

	@Override
	public void flush() {
		getSession().flush();
	}
}
