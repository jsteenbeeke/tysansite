/*
 * Copyright 2010-2011 Jeroen Steenbeeke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeroensteenbeeke.hyperion.data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.injection.web.InjectorHolder;
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
		this.entityClass = list != null && list.isEmpty() ? null : Hibernate
				.getClass(list.get(0));
		for (T t : list) {
			Class<T> tClass = Hibernate.getClass(t);
			if (!entityClass.isAssignableFrom(tClass)) {
				if (entityClass.getSuperclass() != null
						&& entityClass.getSuperclass().isAssignableFrom(tClass)) {
					entityClass = (Class<T>) entityClass.getSuperclass();
				}
			}

			ids.add(t.getId());

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
	public List<T> getObject() {
		if (list == null) {
			list = load();
		}

		return list;
	}

	@Override
	protected List<T> load() {
		if (entityFinder == null) {
			InjectorHolder.getInjector().inject(this);
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
