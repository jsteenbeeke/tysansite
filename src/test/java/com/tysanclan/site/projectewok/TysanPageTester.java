/**
 * Tysan Clan Website
 * Copyright (C) 2008-2013 Jeroen Steenbeeke and Ties van de Ven
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tysanclan.site.projectewok;

import com.jeroensteenbeeke.hyperion.util.Datasets;
import com.jeroensteenbeeke.hyperion.solitary.InMemory;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.protocol.http.mock.MockHttpServletRequest;
import org.apache.wicket.protocol.http.mock.MockHttpSession;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class TysanPageTester {
	private WicketTester tester;

	private InMemory.Handler handler;

	private BeanFactory beanFactory;
	private EntityManagerFactory emf;

	@Before
	public void createHandlerAndStartRequest() throws Exception {
		Datasets.readFromObject(this).ifPresent(Datasets.INSTANCE::set);

		System.setProperty("ewok.testmode", "true");
		handler = InMemory.run("ewok").withContextPath("/tysantest")
						  .withoutShowingSql().atPort(8383).orElseThrow(
				() -> new IllegalStateException(
					"Could not start webserver"));

		TysanApplication application = TysanApplicationReference.INSTANCE
			.getApplication();
		ThreadContext.setApplication(application);

		tester = new WicketTester(application, false);

		MockServletContext sctx = new MockServletContext(
				tester.getApplication(), "/src/main/webapp/");
		MockHttpServletRequest request = new MockHttpServletRequest(
				tester.getApplication(), new MockHttpSession(sctx), sctx);
		RequestAttributes attr = new ServletRequestAttributes(request);

		RequestContextHolder.setRequestAttributes(attr);

		ApplicationContext context = TysanApplication.get()
				.getApplicationContext();
		beanFactory = context.getAutowireCapableBeanFactory();
		emf = context.getBean(EntityManagerFactory.class);
		EntityManager em = context.getBean(EntityManager.class);
		EntityManagerHolder emHolder = new EntityManagerHolder(em);
		TransactionSynchronizationManager.bindResource(emf, emHolder);

		setupAfterRequestStarted();
	}

	protected void setupAfterRequestStarted() {
	}

	@After
	public void endRequest() throws Exception {
		TransactionSynchronizationManager.unbindResource(emf);

		RequestContextHolder.resetRequestAttributes();

		handler.terminate();

		Datasets.INSTANCE.unset();

	}

	protected <T> T getBean(Class<T> beanClass) {
		return beanFactory.getBean(beanClass);
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

	protected WicketTester getTester() {
		return tester;
	}

}
