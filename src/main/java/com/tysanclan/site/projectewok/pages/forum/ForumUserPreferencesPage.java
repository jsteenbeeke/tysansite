/**
 * Tysan Clan Website
 * Copyright (C) 2008-2011 Jeroen Steenbeeke and Ties van de Ven
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
package com.tysanclan.site.projectewok.pages.forum;

import org.odlabs.wiquery.ui.tabs.Tabs;

import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.components.ChangeEmailPanel;
import com.tysanclan.site.projectewok.components.ChangePasswordPanel;
import com.tysanclan.site.projectewok.components.PrimaryPreferencesPanel;

/**
 * @author Jeroen Steenbeeke
 */
public class ForumUserPreferencesPage extends TysanPage {
	/**
     * 
     */
	public ForumUserPreferencesPage() {
		super("User Preferences");

		Tabs tabs = new Tabs("tabs");

		tabs.add(new PrimaryPreferencesPanel("preferences",
		        getUser()) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see com.tysanclan.site.projectewok.components.ChangeEmailPanel#onSubmit()
			 */
			@Override
			public void onSubmit() {
				setResponsePage(new ForumUserPreferencesPage());
			}
		});

		tabs.add(new ChangePasswordPanel("passwordchange",
		        getUser()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onChanged() {
				setResponsePage(new ForumUserPreferencesPage());
			}

		});

		tabs.add(new ChangeEmailPanel("emailchange",
		        getUser()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				setResponsePage(new ForumUserPreferencesPage());
			}
		});

		add(tabs);

	}
}
