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
package com.tysanclan.site.projectewok.components;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.MemberPage;

/**
 * @author Jeroen Steenbeeke
 */
public class MemberListItem extends Panel {
	private static final long serialVersionUID = 1L;

	private IModel<User> userModel;

	public MemberListItem(String id, User user) {
		super(id);

		userModel = ModelMaker.wrap(user);

		add(new RankIcon("icon", user.getRank()));

		PageParameters params = new PageParameters();
		params.add("userid", user.getId().toString());
		Link<User> link = new BookmarkablePageLink<User>("profile",
				MemberPage.class, params);

		link.add(new Label("username", new LoadableDetachableModel<String>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected String load() {
				User user = getUser();
				Page page = getPage();
				if (page instanceof TysanPage) {
					TysanPage tp = (TysanPage) page;
					if (tp.isAprilFoolsDay(2017)) {
						int governorId = (int) (user.getId()
								% governors.length);

						return governors[governorId];
					}
				}

				return user.getUsername();
			}

		}));
		add(link);
	}

	public User getUser() {
		return userModel.getObject();
	}

	/**
	 * @see org.apache.wicket.Component#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		userModel.detach();
	}

	private static final String[] governors = new String[] {
			"James Pinckney Henderson", "George T. Wood",
			"Peter Hansborough Bell", "James W. Henderson", "Elisha M. Pease",
			"Hardin R. Runnels", "Sam Houston", "Edward Clark",
			"Francis R. Lubbock", "Pendleton Murrah", "Andrew J. Hamilton",
			"James W. Throckmorton", "Elisha M. Pease", "Edmund J. Davis",
			"Richard Coke", "Richard B. Hubbard", "Oran M. Roberts",
			"John Ireland", "Lawrence Sullivan Ross", "James Stephen Hogg",
			"Charles A. Culberson", "Joseph D. Sayers", "S. W. T. Lanham",
			"Thomas Mitchell Campbell", "Oscar Branch Colquitt",
			"James E. \"Pa\" Ferguson", "William P. Hobby", "Pat Morris Neff",
			"Miriam A. \"Ma\" Ferguson", "Dan Moody", "Ross S. Sterling",
			"Miriam A. \"Ma\" Ferguson", "James Allred", "W. Lee O'Daniel",
			"Coke R. Stevenson", "Beauford H. Jester", "Allan Shivers",
			"Price Daniel", "John Connally", "Preston Smith", "Dolph Briscoe",
			"Bill Clements", "Mark White", "Bill Clements", "Ann Richards",
			"George W. Bush", "Rick Perry", "Greg Abbott" };
}
