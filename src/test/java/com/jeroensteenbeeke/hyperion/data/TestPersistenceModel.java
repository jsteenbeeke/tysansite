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

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPersistenceModel {
	private SessionFactory sf;

	private Session current;

	@Before
	public void initHibernate() {
		Configuration config = new Configuration();
		config = config.addAnnotatedClass(Lolcat.class);

		config = config.setProperty("hibernate.hbm2ddl.auto", "create");
		config = config.setProperty("hibernate.dialect",
				"org.hibernate.dialect.H2Dialect");
		config = config.setProperty("hibernate.show_sql", "true");
		config = config.setProperty("hibernate.connection.driver_class",
				"org.h2.Driver");
		config = config.setProperty("hibernate.connection.url",
				"jdbc:h2:mem:tysan");
		config = config.setProperty("hibernate.connection.username", "sa");
		config = config.setProperty("hibernate.connection.password", "");
		config = config.setProperty("hibernate.connection.pool_size", "50");
		config = config.setProperty("hibernate.connection.provider_class",
				"org.hibernate.connection.DriverManagerConnectionProvider");
		// config = config.setProperty("hibernate.c3p0.min_size", "5");
		// config = config.setProperty("hibernate.c3p0.max_size", "20");
		// config = config.setProperty("hibernate.c3p0.timeout", "1800");
		// config = config.setProperty("hibernate.c3p0.max_statements", "50");

		final ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
				.applySettings(config.getProperties()).buildServiceRegistry();

		sf = config.buildSessionFactory(serviceRegistry);
	}

	@Test
	public void testPersistenceModel() {
		current = sf.openSession();

		Lolcat t1 = new Lolcat();
		Lolcat t2 = new Lolcat();

		t2.setParent(t1);

		current.save(t1);
		current.save(t2);

		IModel<Lolcat> model = new PersistenceModel<Lolcat>(t1) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void injectDAO() {
				setDao(new BaseEntityFinder() {

					@SuppressWarnings("unchecked")
					@Override
					public <T extends DomainObject> T getEntity(
							Class<T> entityClass, Serializable id) {
						return (T) current.load(entityClass, id);
					}
				});
			}

		};

		current.close();

		model.detach();

		current = sf.openSession();

		assertEquals(1, model.getObject().getChildren().size());

		current.close();
	}

	@After
	public void tearItAllDown() {
		sf.close();
	}
}
