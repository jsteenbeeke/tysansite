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
package com.tysanclan.site.projectewok.util.forum;

import java.util.List;

import org.hibernate.SQLQuery;

/**
 * @author jeroen
 */
public abstract class AbstractForumViewContext implements ForumViewContext {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected final <U> List<U> listOf(SQLQuery query) {
		return (List<U>) query.list();
	}

	protected final int count(SQLQuery query) {
		return ((Number) query.uniqueResult()).intValue();
	}

}
