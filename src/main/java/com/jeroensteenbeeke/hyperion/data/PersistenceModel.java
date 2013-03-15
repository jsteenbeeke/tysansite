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

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.hibernate.Hibernate;

/**
 * A model to safely include Hibernate models within Wicket components
 * 
 * @author Jeroen Steenbeeke
 * @param <T>
 *            The Domain object that is contained by this model
 */
@SuppressWarnings("unchecked")
class PersistenceModel<T extends DomainObject> implements IModel<T> {
	private static final long serialVersionUID = 1L;

	@SpringBean(name = "EntityFinder")
	private BaseEntityFinder dao;

	private Class<T> entityClass = null;
	private Serializable id = null;

	private T object = null;

	/**
	 * Create a new Hibernate for the indicated object and the provided DAO
	 * class
	 * 
	 * @param object
	 *            The object to store in this model
	 * @param dao
	 *            The DAO to use to reattach this object
	 */
	public PersistenceModel(T object) {
		super();
		setObject(object);

	}

	@Override
	public final void detach() {
		dao = null;
		if (object != null && object.getId() != null) {
			object = null;
		}
	}

	@Override
	public T getObject() {
		if (object == null)
			object = load();

		return object;
	}

	@Override
	public void setObject(T object) {
		if (object != null) {
			this.id = object.getId();
			this.object = object;
			this.entityClass = Hibernate.getClass(object);
		}
	}

	protected final T load() {
		if (entityClass != null && object == null) {
			if (dao == null) {
				injectDAO();
			}

			this.object = dao.getEntity(entityClass, id);
		}

		return this.object;
	}

	void injectDAO() {
		Injector.get().inject(this);
	}

	protected final void setDao(BaseEntityFinder dao) {
		this.dao = dao;
	}
}
