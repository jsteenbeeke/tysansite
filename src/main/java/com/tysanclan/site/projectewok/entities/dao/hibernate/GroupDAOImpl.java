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
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Group_;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.filter.GroupFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class GroupDAOImpl extends HibernateDAO<Group, GroupFilter>
		implements com.tysanclan.site.projectewok.entities.dao.GroupDAO {

	@Override
	public List<Group> getMemberGroups(User user) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Group> query = criteriaBuilder.createQuery(Group.class);
		Root<Group> root = query.from(Group.class);

		query.select(root).where(criteriaBuilder
			 .isMember(user, root.get(Group_.groupMembers)));

		return entityManager.createQuery(query).getResultList();
	}
}
