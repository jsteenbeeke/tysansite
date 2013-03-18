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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.ForumPost;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.filters.ForumPostFilter;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ForumPostDAOImpl extends EwokHibernateDAO<ForumPost> implements
		com.tysanclan.site.projectewok.entities.dao.ForumPostDAO {
	/**
	 * @see com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO#createCriteria(com.tysanclan.site.projectewok.dataaccess.SearchFilter)
	 */
	@Override
	protected Criteria createCriteria(SearchFilter<ForumPost> filter) {
		Criteria criteria = getSession().createCriteria(ForumPost.class);

		if (isForumPostFilter(filter)) {
			ForumPostFilter forumPostFilter = (ForumPostFilter) filter;
			checkPosterRestriction(criteria, forumPostFilter);
			checkShadowRestriction(criteria, forumPostFilter);
			checkAfterCriterium(criteria, forumPostFilter);
		}

		return criteria;
	}

	private void checkAfterCriterium(Criteria criteria,
			ForumPostFilter forumPostFilter) {
		if (forumPostFilter.getPostAfter() != null) {
			criteria.add(Restrictions.ge("time", forumPostFilter.getPostAfter()));
		}
	}

	private void checkShadowRestriction(Criteria criteria,
			ForumPostFilter forumPostFilter) {
		if (forumPostFilter.getShadow() != null) {
			criteria.add(Restrictions.eq("shadow", forumPostFilter.getShadow()));
		}
	}

	private void checkPosterRestriction(Criteria criteria,
			ForumPostFilter forumPostFilter) {
		if (forumPostFilter.getUser() != null) {
			criteria.add(Restrictions.eq("poster", forumPostFilter.getUser()));
		}
	}

	private boolean isForumPostFilter(SearchFilter<ForumPost> filter) {
		return filter instanceof ForumPostFilter;
	}

	@Override
	public int getUnreadSize(User user) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.add(Restrictions.eq("id", user.getId()));
		criteria.setProjection(Projections.count("unreadForumPosts"));
		Number number = (Number) criteria.uniqueResult();
		return number.intValue();
	}

	@Override
	public int countByContext(User user, ForumThread contextObject,
			ForumViewContext context) {
		return context.countPosts(getSession(), contextObject, user);
	}

	@Override
	public List<ForumPost> findByContext(User user, ForumThread contextObject,
			ForumViewContext context, long first, long count) {
		return context
				.getPosts(getSession(), contextObject, user, first, count);
	}
}
