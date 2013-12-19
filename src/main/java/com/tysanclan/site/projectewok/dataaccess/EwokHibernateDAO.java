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
package com.tysanclan.site.projectewok.dataaccess;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.jeroensteenbeeke.hyperion.data.HibernateDAO;

/**
 * A Data Access Object that uses the Hibernate ORM package
 * 
 * @author Jeroen Steenbeeke
 * @param <T>
 *            The domain object loadable by this DAO
 */
@SuppressWarnings("unchecked")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public abstract class EwokHibernateDAO<T extends DomainObject> extends
		HibernateDAO<T> implements EwokDAO<T> {
	private Class<T> domainClass = (Class<T>) ((ParameterizedType) getClass()
			.getGenericSuperclass()).getActualTypeArguments()[0];
	private SessionFactory sessionFactory;

	/**
	 * Get a list of all objects
	 * 
	 * @return The list of objects, which may be empty
	 */
	@Override
	public List<T> findAll() {
		Criteria crit = getSession().createCriteria(domainClass);
		return crit.list();
	}

	/**
	 * @return The Session for this factory
	 */
	@Override
	public Session getSession() {
		Session session = sessionFactory.getCurrentSession();
		session.setFlushMode(FlushMode.COMMIT);
		return session;
	}

	/**
	 * @return The session factory
	 */
	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public T load(Serializable id) {
		return (T) getSession().load(domainClass, id);
	}

	@Override
	public T get(Serializable id) {

		return (T) getSession().get(domainClass, id);
	}

	/**
	 * Save the indicated object
	 * 
	 * @param object
	 *            The object to save
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(T object) {
		getSession().save(object);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void update(T object) {
		getSession().update(object);
	}

	/**
	 * Sets the session factory of this DAO
	 * 
	 * @param sessionFactory
	 *            The factory to set
	 */
	@Override
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
