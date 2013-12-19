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
package com.jeroensteenbeeke.hyperion.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

/**
 * @author Jeroen Steenbeeke
 */
class EntityListModel<T extends DomainObject> extends
		LoadableDetachableModel<List<T>> implements IModel<List<T>> {
	private static final long serialVersionUID = 1L;

	private Class<T> entityClass = null;

	private List<Serializable> ids;

	@SpringBean
	private BaseEntityFinder entityFinder;

	private transient List<T> list;

	@SuppressWarnings("unchecked")
	public EntityListModel(List<T> list) {
		this.list = list;
		this.ids = new LinkedList<Serializable>();
		if (list != null) {
			if (!list.isEmpty()) {
				this.entityClass = Hibernate.getClass(list.get(0));

				for (T t : list) {
					Class<T> tClass = Hibernate.getClass(t);
					if (!entityClass.isAssignableFrom(tClass)) {
						if (entityClass.getSuperclass() != null
								&& entityClass.getSuperclass()
										.isAssignableFrom(tClass)) {
							entityClass = (Class<T>) entityClass
									.getSuperclass();
						}
					}

					ids.add(t.getDomainObjectId());

				}
			}

		}

		detach(); // Fix voor achterlijke hibernate bug
	}

	@Override
	public void detach() {
		super.detach();

		this.entityFinder = null;
		this.list = null;
	}

	@Override
	protected List<T> load() {
		if (entityFinder == null) {
			Injector.get().inject(this);
		}

		if (entityClass != null) {
			this.list = new LinkedList<T>();

			for (Serializable id : ids) {
				this.list.add(entityFinder.getEntity(entityClass, id));
			}

			return this.list;
		}

		return new LinkedList<T>();
	}
}
