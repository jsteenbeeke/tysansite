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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.jeroensteenbeeke.hyperion.solstice.data.HibernateDAO;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumCategory;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.UnreadForumPost;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.filter.ForumFilter;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ForumDAOImpl extends HibernateDAO<Forum, ForumFilter> implements
		com.tysanclan.site.projectewok.entities.dao.ForumDAO {
	@Override
	public boolean isPostUnread(User user, ForumPost post) {
		Criteria criteria = getSession().createCriteria(UnreadForumPost.class);

		criteria.add(Restrictions.eq("forumPost", post));
		criteria.add(Restrictions.eq("user", user));
		criteria.setProjection(Projections.rowCount());

		return ((Number) criteria.uniqueResult()).intValue() == 1;
	}

	/**
	 * @see com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO#createCriteria(com.tysanclan.site.projectewok.dataaccess.SearchFilter)
	 */
	@Override
	protected Criteria createCriteria(SearchFilter<Forum> filter) {
		Criteria criteria = getSession().createCriteria(Forum.class);

		if (filter instanceof ForumFilter) {
			ForumFilter ff = (ForumFilter) filter;
			if (ff.getName() != null) {
				criteria.add(Restrictions.eq("name", ff.getName()));
			}
			if (ff.getInteractive() != null) {
				criteria.add(Restrictions.eq("interactive", ff.getInteractive()));
			}
		}

		return criteria;
	}

	@Override
	public int countByContext(User user, ForumCategory contextObject,
			ForumViewContext context) {
		return context.countForums(getSession(), contextObject, user);
	}

	@Override
	public List<Forum> findByContext(User user, ForumCategory contextObject,
			ForumViewContext context, long first, long count) {
		return context.getForums(getSession(), contextObject, user, first,
				count);
	}
}
