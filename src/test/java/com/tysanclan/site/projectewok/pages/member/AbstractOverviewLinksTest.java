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
package com.tysanclan.site.projectewok.pages.member;

import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanApplication;
import com.tysanclan.site.projectewok.TysanPageTester;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import org.apache.wicket.Page;
import org.junit.After;
import org.junit.Before;

import java.util.List;
import java.util.Random;

public abstract class AbstractOverviewLinksTest extends TysanPageTester {
	private static final Random random = new Random();

	private Long testUser;

	@Override
	protected void setupAfterRequestStarted() {
		testUser = determineUserId();
	}

	protected abstract long determineUserId();

	protected static long userIdOfRank(Rank rank) {
		UserDAO userDAO = TysanApplication.get().getApplicationContext()
				.getBean(UserDAO.class);
		List<User> byRank = userDAO.findByRank(rank);

		if (byRank.isEmpty()) {
			throw new IllegalStateException();
		}

		User user = byRank.get(random.nextInt(byRank.size()));

		return user.getId();

	}

	@Before
	public void setToOverviewPage() {
		logIn(testUser);

		wicket().startPage(OverviewPage.class);
		wicket().assertRenderedPage(OverviewPage.class);
	}

	@After
	public void logoutAfterTest() {
		logOut();
	}

	protected final void verifyIconLink(String id,
			Class<? extends Page> target) {
		verifyLink(id + ":label:link", target);
	}

	protected final void verifyLink(String id, Class<? extends Page> target) {
		wicket().assertEnabled(id);
		wicket().assertVisible(id);

		wicket().clickLink(id, false);
		wicket().assertRenderedPage(target);
	}
}
