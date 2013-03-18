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
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * A Data Access Object that uses the Hibernate ORM package
 * 
 * @author Jeroen Steenbeeke
 * @param <T>
 *            The domain object loadable by this DAO
 */
@SuppressWarnings("unchecked")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public abstract class HibernateDAO<T extends DomainObject> implements DAO<T> {

	private static final Logger log = LoggerFactory
			.getLogger(HibernateDAO.class);

	private Class<T> domainClass = (Class<T>) ((ParameterizedType) getClass()
			.getGenericSuperclass()).getActualTypeArguments()[0];
	private SessionFactory sessionFactory;

	/**
	 * @return The total number of items of this type
	 */
	public long countAll() {
		Criteria crit = getSession().createCriteria(domainClass);
		crit.setProjection(Projections.rowCount());
		return ((Number) crit.uniqueResult()).longValue();
	}

	@Override
	public void evict(T object) {
		getSession().evict(object);
	}

	/**
	 * Delete the indicated object
	 * 
	 * @param object
	 *            The object to delete
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void delete(T object) {
		getSession().delete(object);
	}

	/**
	 * Get a list of all objects
	 * 
	 * @return The list of objects, which may be empty
	 */
	public List<T> findAll() {
		Criteria crit = getSession().createCriteria(domainClass);
		return crit.list();
	}

	@Override
	public long countByFilter(SearchFilter<T> filter) {
		Criteria crit = createCriteria(filter);
		if (crit != null) {
			crit.setProjection(Projections.rowCount());

			return ((Number) crit.uniqueResult()).longValue();
		}

		return 0;
	}

	@Override
	public T getUniqueByFilter(SearchFilter<T> filter) {
		Criteria crit = applyOrderBy(createCriteria(filter), filter);

		if (crit != null) {

			return (T) crit.uniqueResult();
		}

		return null;
	}

	@Override
	public List<T> findByFilter(SearchFilter<T> filter) {
		Criteria crit = applyOrderBy(createCriteria(filter), filter);

		if (crit != null) {
			return crit.list();
		}

		return new LinkedList<T>();
	}

	@Override
	public List<T> findByFilter(SearchFilter<T> filter, long offset, long count) {
		Criteria crit = applyOrderBy(createCriteria(filter), filter);

		if (crit != null) {
			crit.setMaxResults((int) count);
			crit.setFirstResult((int) offset);

			return crit.list();
		}

		return new LinkedList<T>();
	}

	private Criteria applyOrderBy(Criteria criteria, SearchFilter<T> filter) {
		if (criteria != null) {
			List<String> properties = filter.getOrderBy();
			List<Boolean> directions = filter.getOrderByDirections();
			int amount = properties.size();

			for (int i = 0; i < amount; i++) {
				boolean ascending = directions.get(i);
				String property = properties.get(i).substring(0, 1)
						.toLowerCase()
						+ properties.get(i).substring(1);

				if (ascending) {
					criteria.addOrder(Order.asc(property));
				} else {
					criteria.addOrder(Order.desc(property));
				}
			}
		}

		return criteria;
	}

	protected abstract Criteria createCriteria(SearchFilter<T> filter);

	/**
	 * @return The Session for this factory
	 */
	public Session getSession() {
		Session session = sessionFactory.getCurrentSession();
		session.setFlushMode(FlushMode.COMMIT);

		log.info("DAO using session " + session.hashCode());

		return session;
	}

	/**
	 * @return The session factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Load the object with the indicated ID
	 * 
	 * @param id
	 *            The ID of the requested object
	 * @return The loaded object, or <code>null</code> if it does not exist
	 */
	public T load(Serializable id) {

		return (T) getSession().load(domainClass, id);
	}

	/**
	 * Save the indicated object
	 * 
	 * @param object
	 *            The object to save
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void save(T object) {
		getSession().save(object);
	}

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
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected final <U> List<U> listOf(Criteria criteria) {
		return (List<U>) criteria.list();
	}

	protected final <U> U unique(Criteria criteria) {
		return (U) criteria.uniqueResult();
	}

}
