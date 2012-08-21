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
package com.tysanclan.site.projectewok.entities.dao;

import java.util.List;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.util.forum.ForumViewContext;

/**
 * @author jeroen
 */
public interface ContextBasedForumDAO<T extends DomainObject, ContextType extends DomainObject> {

	int countByContext(User user, ContextType contextId,
			ForumViewContext context);

	List<T> findByContext(User user, ContextType contextId,
			ForumViewContext context, int first, int count);

}
