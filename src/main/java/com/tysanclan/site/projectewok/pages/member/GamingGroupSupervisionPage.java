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

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GamingGroup;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.util.GraphUtil;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Jeroen Steenbeeke
 */
public class GamingGroupSupervisionPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public GamingGroupSupervisionPage(Game game) {
		super("Gaming groups for " + game.getName());

		if (!getUser().equals(game.getCoordinator())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		initComponents(game.getName(), game.getGroups());
	}

	public GamingGroupSupervisionPage(Realm realm) {
		super("Gaming groups for " + realm.getName());

		if (!getUser().equals(realm.getOverseer())) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		initComponents(realm.getName(), realm.getGroups());
	}

	private void initComponents(String name, List<GamingGroup> groups) {
		List<User> users = new LinkedList<User>();
		for (GamingGroup group : groups) {
			users.addAll(group.getGroupMembers());
		}

		add(GraphUtil.makePieChart("rankcomposition",
				"Overall rank composition", createCompositionChart(users))
				.setVisible(!users.isEmpty()));
		add(GraphUtil.makeDonationsBarChart("groupsize", "Group sizes",
				createGroupSizeChart(groups)).setVisible(!users.isEmpty()));

		add(new ListView<GamingGroup>("groups", ModelMaker.wrap(groups)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<GamingGroup> item) {
				GamingGroup group = item.getModelObject();

				List<User> userList = new LinkedList<User>();
				userList.addAll(group.getGroupMembers());

				item.add(new Label("name", group.getName()));
				item.add(GraphUtil.makePieChart("rankcomposition",
						group.getName() + " rank composition",
						createCompositionChart(userList)));

			}
		});

	}

	private SortedMap<String, Integer> createGroupSizeChart(
			List<GamingGroup> groups) {

		SortedMap<String, Integer> series = new TreeMap<>();

		for (GamingGroup group : groups) {
			series.put(group.getName(), group.getGroupMembers().size());
		}

		return series;
	}

	/**
	 	 */
	private SortedMap<String, Integer> createCompositionChart(
			List<User> users) {
		SortedMap<String, Integer> series = new TreeMap<>();

		Map<Rank, Integer> count = new HashMap<Rank, Integer>();

		for (User user : users) {
			if (count.containsKey(user.getRank())) {
				count.put(user.getRank(), count.get(user.getRank()) + 1);
			} else {
				count.put(user.getRank(), 1);
			}
		}

		for (Entry<Rank, Integer> e : count.entrySet()) {
			series.put(e.getKey().toString(), e.getValue());
		}

		return series;
	}
}
