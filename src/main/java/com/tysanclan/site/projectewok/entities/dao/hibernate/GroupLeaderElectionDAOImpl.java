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
import com.tysanclan.site.projectewok.entities.GroupLeaderElection;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.filter.GroupLeaderElectionFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GroupLeaderElectionDAOImpl extends HibernateDAO<GroupLeaderElection, GroupLeaderElectionFilter>
		implements
		com.tysanclan.site.projectewok.entities.dao.GroupLeaderElectionDAO {


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
}
