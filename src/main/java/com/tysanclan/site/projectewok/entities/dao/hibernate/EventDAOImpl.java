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

import java.util.Calendar;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.dataaccess.EwokHibernateDAO;
import com.tysanclan.site.projectewok.entities.Event;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.dao.filters.EventFilter;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class EventDAOImpl extends EwokHibernateDAO<Event> implements
		com.tysanclan.site.projectewok.entities.dao.EventDAO {
	@Override
	protected Criteria createCriteria(SearchFilter<Event> filter) {
		Criteria criteria = getSession().createCriteria(Event.class);

		if (filter instanceof EventFilter) {
			EventFilter eventFilter = (EventFilter) filter;

			if (eventFilter.getDate() != null) {
				Date from = DateUtil.getMidnightDate(eventFilter.getDate());
				Calendar toc = DateUtil.getCalendarInstance();
				toc.setTime(from);
				toc.add(Calendar.DAY_OF_YEAR, 1);
				toc.add(Calendar.SECOND, 1);
				Date to = toc.getTime();

				criteria.add(Restrictions.between("date", from, to));
			}
		}

		return criteria;
	}

	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.EventDAO#getEventByThread(com.tysanclan.site.projectewok.entities.ForumThread)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Event getEventByThread(ForumThread thread) {
		Criteria criteria = getSession().createCriteria(Event.class);

		criteria.add(Restrictions.eq("eventThread", thread));

		return (Event) criteria.uniqueResult();
	}
}
