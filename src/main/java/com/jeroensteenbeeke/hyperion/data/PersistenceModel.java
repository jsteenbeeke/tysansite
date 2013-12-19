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
		if (object != null && object.getDomainObjectId() != null) {
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
			this.id = object.getDomainObjectId();
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
