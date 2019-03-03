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
import com.tysanclan.site.projectewok.entities.Event;
import com.tysanclan.site.projectewok.entities.ForumThread;
import com.tysanclan.site.projectewok.entities.filter.EventFilter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class EventDAOImpl extends HibernateDAO<Event, EventFilter>
		implements com.tysanclan.site.projectewok.entities.dao.EventDAO {
	/**
	 * @see com.tysanclan.site.projectewok.entities.dao.EventDAO#getEventByThread(com.tysanclan.site.projectewok.entities.ForumThread)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Event getEventByThread(ForumThread thread) {
		return getUniqueByFilter(new EventFilter().eventThread(thread))
				.getOrNull();
	}
}
