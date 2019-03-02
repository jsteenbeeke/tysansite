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
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.filter.ForumCategoryFilter;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ForumCategoryDAOImpl
		extends HibernateDAO<ForumCategory, ForumCategoryFilter> implements
		com.tysanclan.site.projectewok.entities.dao.ForumCategoryDAO {

	@Override
	public int countByContext(User user, User contextObject,
			ForumViewContext context) {

		return context.countCategories(entityManager, user);
	}

	@Override
	public List<ForumCategory> findByContext(User user, User contextObject,
			ForumViewContext context, int first, int count) {
		return context.getCategories(entityManager, user, first, count);
	}
}
