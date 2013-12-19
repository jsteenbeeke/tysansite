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

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public abstract class AbstractMemberPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	public AbstractMemberPage(String title, IModel<?> model) {
		super(title, model);
		initMemberPage();
	}

	public AbstractMemberPage(String title) {
		super(title);
		initMemberPage();
	}

	/**
	 * 
	 */
	private void initMemberPage() {
		add(new Link<User>("back") {

			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				onClickBackToOverview();

			}
		});

	}

	protected void onClickBackToOverview() {
		setResponsePage(new OverviewPage());
	}

}
