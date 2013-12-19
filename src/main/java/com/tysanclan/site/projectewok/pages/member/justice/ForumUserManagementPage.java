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
package com.tysanclan.site.projectewok.pages.member.justice;

import java.util.Calendar;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.UserService;
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;
import com.tysanclan.site.projectewok.pages.member.AbstractMemberPage;
import com.tysanclan.site.projectewok.util.DateUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.TRUTHSAYER)
public class ForumUserManagementPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserDAO userDAO;

	public ForumUserManagementPage() {
		this(true);
	}

	public ForumUserManagementPage(final boolean filterInactive) {
		super("Manage Forum Users");

		UserFilter filter = new UserFilter();
		filter.addRank(Rank.BANNED);
		filter.addRank(Rank.FORUM);

		if (filterInactive) {
			Calendar cal = DateUtil.getCalendarInstance();
			cal.add(Calendar.WEEK_OF_YEAR, -2);

			filter.setActiveSince(cal.getTime());
		}

		filter.addOrderBy("username", true);

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "'content'");

		PageableListView<User> users = new PageableListView<User>("users",
				ModelMaker.wrap(userDAO.findByFilter(filter)), 20) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<User> item) {
				User user = item.getModelObject();

				item.add(new Label("username", user.getUsername()));
				item.add(new Label("email", user.getEMail()));

				item.add(new IconLink.Builder("images/icons/delete.png",
						new BanClickResponder(user)).newInstance("ban")
						.setVisible(user.getRank() == Rank.FORUM));
				item.add(new IconLink.Builder("images/icons/add.png",
						new UnbanClickResponder(user)).newInstance("unban")
						.setVisible(user.getRank() == Rank.BANNED));
			}
		};

		accordion.add(new AjaxPagingNavigator("nav", users));

		accordion.add(users);

		IconLink.Builder inactiveLink = new IconLink.Builder(
				"images/icons/magnifier.png",
				new DefaultClickResponder<Void>() {
					private static final long serialVersionUID = 1L;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						setResponsePage(new ForumUserManagementPage(
								!filterInactive));
					}
				});

		if (filterInactive) {
			inactiveLink.setText("Show inactive users");
		} else {
			inactiveLink.setText("Hide inactive users");
		}

		accordion.add(inactiveLink.newInstance("inactives"));

		add(accordion);

	}

	private class BanClickResponder extends DefaultClickResponder<User> {
		private static final long serialVersionUID = 1L;

		@SpringBean
		private UserService userService;

		/**
		 * 
		 */
		public BanClickResponder(User user) {
			super(ModelMaker.wrap(user));
		}

		/**
		 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
		 */
		@Override
		public void onClick() {
			User banner = getUser();

			userService.banUser(banner, getModelObject());

			setResponsePage(new ForumUserManagementPage());
		}
	}

	private class UnbanClickResponder extends DefaultClickResponder<User> {
		private static final long serialVersionUID = 1L;

		@SpringBean
		private UserService userService;

		/**
		 * 
		 */
		public UnbanClickResponder(User user) {
			super(ModelMaker.wrap(user));
		}

		/**
		 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
		 */
		@Override
		public void onClick() {
			User unbanner = getUser();

			userService.unbanUser(unbanner, getModelObject());

			setResponsePage(new ForumUserManagementPage());
		}
	}
}
