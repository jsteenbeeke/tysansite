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
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.Forum;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.filters.ForumThreadFilter;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class ForumThreadDAOImpl extends EwokHibernateDAO<ForumThread> implements
		com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO {
	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.ForumThreadDAO#findByPostTime(com.tysanclan.site.projectewok.entities.Forum)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ForumThread> findByPostTime(Forum forum) {
		Session session = getSession();

		Criteria crit = session.createCriteria(ForumThread.class);
		crit.add(Restrictions.eq("forum", forum));
		crit.addOrder(Order.desc("postTime"));

		return crit.list();
	}

	/**
	 * @see com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO#createCriteria(com.tysanclan.site.projectewok.dataaccess.SearchFilter)
	 */
	@Override
	protected Criteria createCriteria(SearchFilter<ForumThread> filter) {
		Criteria crit = getSession().createCriteria(ForumThread.class);

		if (filter instanceof ForumThreadFilter) {
			ForumThreadFilter cf = (ForumThreadFilter) filter;
			if (cf.getForum() != null) {
				crit.add(Restrictions.eq("forum", cf.getForum()));
			}
		}

		return crit;
	}

	@Override
	public int countByContext(User user, Forum contextObject,
			ForumViewContext context) {
		return context.countThreads(getSession(), contextObject, user);
	}

	@Override
	public List<ForumThread> findByContext(User user, Forum contextObject,
			ForumViewContext context, long first, long count) {
		return context.getThreads(getSession(), contextObject, user, first,
				count);
	}
}
