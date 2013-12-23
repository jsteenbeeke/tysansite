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
package com.tysanclan.site.projectewok;

import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.util.tester.WicketTester;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestScope;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class TysanPageTester {
	private static WicketTester tester;

	private SessionFactory sessionFactory;

	private BeanFactory beanFactory;

	@BeforeClass
	public static void setUp() {
		tester = WicketTesterProvider.INST.getTester();
		tester.getRequestCycle();
	}

	@Before
	public void startRequest() {
		ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) TysanApplication
				.getApplicationContext().getAutowireCapableBeanFactory();
		// configurableBeanFactory.registerScope("session", new SessionScope());
		configurableBeanFactory.registerScope("request", new RequestScope());
		this.beanFactory = configurableBeanFactory;

		MockServletContext sctx = new MockServletContext(
				tester.getApplication(), "/src/main/webapp/");
		MockHttpServletRequest request = new MockHttpServletRequest(
				tester.getApplication(), new MockHttpSession(sctx), sctx);
		RequestAttributes attr = new ServletRequestAttributes(request);

		RequestContextHolder.setRequestAttributes(attr);

		sessionFactory = (SessionFactory) configurableBeanFactory
				.getBean("sessionFactory");

		Session session = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, session);
	}

	protected <T> T getBean(Class<T> beanClass) {
		return beanFactory.getBean(beanClass);
	}

	@After
	public void endRequest() {
		RequestContextHolder.resetRequestAttributes();
		Session session = (Session) TransactionSynchronizationManager
				.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(session);
		this.beanFactory = null;
	}

	protected void logIn(Long userId) {
		TysanSession session = (TysanSession) tester.getSession();
		if (session != null) {
			session.setCurrentUserId(userId);
		}
	}

	protected void logOut() {
		TysanSession session = (TysanSession) tester.getSession();
		if (session != null) {
			session.setCurrentUserId(null);
		}

	}

	protected static WicketTester getTester() {
		return tester;
	}

}
