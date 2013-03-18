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
package com.tysanclan.site.projectewok.components;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanSession;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.ContextBasedForumDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumDataProvider<T extends DomainObject, C extends DomainObject, D extends ContextBasedForumDAO<T, C>>
		implements IDataProvider<T> {
	private static final long serialVersionUID = 1L;

	private D dao;

	private IModel<C> context;

	public ForumDataProvider(D dao) {
		this(dao, null);
	}

	public ForumDataProvider(D dao, C context) {
		this.dao = dao;
		this.context = ModelMaker.wrap(context);
	}

	public static <T extends DomainObject, C extends DomainObject, D extends ContextBasedForumDAO<T, C>> ForumDataProvider<T, C, D> of(
			D dao) {
		return new ForumDataProvider<T, C, D>(dao);
	}

	public static <T extends DomainObject, C extends DomainObject, D extends ContextBasedForumDAO<T, C>> ForumDataProvider<T, C, D> of(
			C context, D dao) {
		return new ForumDataProvider<T, C, D>(dao, context);
	}

	@Override
	public void detach() {
		context.detach();
	}

	@Override
	public Iterator<? extends T> iterator(long first, long count) {
		TysanSession session = TysanSession.get();
		User user = session != null ? session.getUser() : null;

		List<T> list = dao.findByContext(user, context.getObject(),
				TysanSession.getForumContext(), first, count);

		return list.iterator();
	}

	@Override
	public long size() {

		TysanSession session = TysanSession.get();
		User user = session != null ? session.getUser() : null;

		return dao.countByContext(user, context.getObject(),
				TysanSession.getForumContext());
	}

	@Override
	public IModel<T> model(T object) {
		return ModelMaker.wrap(object);
	}
}
