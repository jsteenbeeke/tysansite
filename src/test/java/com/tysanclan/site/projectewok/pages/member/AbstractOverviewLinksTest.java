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

import org.apache.wicket.Page;
import org.junit.After;
import org.junit.Before;

import com.tysanclan.site.projectewok.TysanPageTester;

public abstract class AbstractOverviewLinksTest extends TysanPageTester {
	private final Long testUser;

	protected AbstractOverviewLinksTest(Long testUser) {
		this.testUser = testUser;
	}

	@Before
	public void setToOverviewPage() {
		logIn(testUser);

		getTester().startPage(OverviewPage.class);
		getTester().assertRenderedPage(OverviewPage.class);
	}

	@After
	public void logoutAfterTest() {
		logOut();
	}

	protected final void verifyIconLink(String id, Class<? extends Page> target) {
		verifyLink(id + ":label:link", target);
	}

	protected final void verifyLink(String id, Class<? extends Page> target) {
		getTester().assertEnabled(id);
		getTester().assertVisible(id);

		getTester().clickLink(id, false);
		getTester().assertRenderedPage(target);
	}
}
