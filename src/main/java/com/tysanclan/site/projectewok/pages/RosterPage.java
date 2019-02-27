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
package com.tysanclan.site.projectewok.pages;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.util.GraphUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

public class RosterPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	private static class MemberListView extends ListView<User> {
		private static final long serialVersionUID = 1L;

		public MemberListView(String id, List<User> users) {
			super(id, ModelMaker.wrap(users));
		}

		/**
		 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
		 */
		@Override
		protected void populateItem(ListItem<User> item) {
			item.add(new MemberListItem("user", item.getModelObject()));

		}
	}

	@SpringBean
	private UserDAO userDAO;

	public RosterPage() {
		super("Roster");

		Map<String, Long> values = new HashMap<>();

		long chancellorCount = userDAO.countByRank(Rank.CHANCELLOR);
		long senatorCount = userDAO.countByRank(Rank.SENATOR);
		long truthsayerCount = userDAO.countByRank(Rank.TRUTHSAYER);
		long reveredMemberCount = userDAO.countByRank(Rank.REVERED_MEMBER);
		long seniorMemberCount = userDAO.countByRank(Rank.SENIOR_MEMBER);
		long memberCount = userDAO.countByRank(Rank.FULL_MEMBER);
		long juniorMemberCount = userDAO.countByRank(Rank.JUNIOR_MEMBER);
		long trialMemberCount = userDAO.countByRank(Rank.TRIAL);

		long total = chancellorCount + senatorCount + truthsayerCount
				+ reveredMemberCount + seniorMemberCount + memberCount
				+ juniorMemberCount + trialMemberCount;

		if (chancellorCount > 0) {
			values.put("Chancellor", 100 * chancellorCount / total);
		}
		if (senatorCount > 0) {
			values.put("Senators", 100 * senatorCount / total);
		}
		if (truthsayerCount > 0) {
			values.put("Truthsayers", 100 * truthsayerCount / total);
		}
		if (reveredMemberCount > 0) {
			values.put("Revered Members", 100 * reveredMemberCount / total);
		}
		if (seniorMemberCount > 0) {
			values.put("Senior Members", 100 * seniorMemberCount / total);
		}
		if (memberCount > 0) {
			values.put("Members", 100 * memberCount / total);
		}
		if (juniorMemberCount > 0) {
			values.put("Junior Members", 100 * juniorMemberCount / total);
		}
		if (trialMemberCount > 0) {
			values.put("Trial Members", 100 * trialMemberCount / total);
		}

		add(GraphUtil.makePieChart("chart", "Member Distribution",
				values));

		User chancellor = null;
		List<User> senators = new LinkedList<User>();
		List<User> truthsayers = new LinkedList<User>();
		List<User> members = new LinkedList<User>();
		List<User> trialmembers = new LinkedList<User>();
		List<User> retiredmembers = new LinkedList<User>();

		for (User u : userDAO.findAll()) {
			if (MemberUtil.isMember(u)) {
				if (!u.isRetired()) {
					switch (u.getRank()) {
						case CHANCELLOR:
							chancellor = u;
							break;
						case SENATOR:
							senators.add(u);
							break;
						case TRUTHSAYER:
							truthsayers.add(u);
							break;
						case TRIAL:
							trialmembers.add(u);
							break;
						default:
							members.add(u);
					}
				} else {
					retiredmembers.add(u);
				}

			}
		}

		if (chancellor != null) {
			add(new MemberListItem("chancellor", chancellor));
		} else {
			add(new WebMarkupContainer("chancellor"));
		}

		members.sort(new Comparator<User>() {
			/**
			 * @see Comparator#compare(Object,
			 *      Object)
			 */
			@Override
			public int compare(User o1, User o2) {
				switch (o1.getRank()) {
					case REVERED_MEMBER:
						if (o2.getRank() == Rank.SENIOR_MEMBER) {
							return -1;
						}
					case SENIOR_MEMBER:
						if (o2.getRank() == Rank.FULL_MEMBER) {
							return -1;
						}
					case FULL_MEMBER:
						if (o2.getRank() == Rank.JUNIOR_MEMBER) {
							return -1;
						}
					default:
				}

				if (o1.getRank() == o2.getRank()) {
					return o1.getJoinDate().compareTo(o2.getJoinDate());
				}

				return 1;
			}
		});

		add(new MemberListView("senators", senators));
		add(new MemberListView("truthsayers", truthsayers));
		add(new MemberListView("members", members));
		add(new MemberListView("trialmembers", trialmembers));

		add(new MemberListView("retiredmembers", retiredmembers));
	}
}
