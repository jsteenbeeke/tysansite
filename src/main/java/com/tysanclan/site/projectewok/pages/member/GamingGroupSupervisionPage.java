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
package com.tysanclan.site.projectewok.pages.member;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.topicus.wqplot.data.BaseSeries;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.util.ListModel;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GamingGroup;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;
import com.tysanclan.site.projectewok.util.GraphUtil;

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
		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		List<User> users = new LinkedList<User>();
		for (GamingGroup group : groups) {
			users.addAll(group.getGroupMembers());
		}

		accordion.add(GraphUtil.makePieChart("rankcomposition",
				"Overall rank composition", createCompositionChart(users))
				.setVisible(!users.isEmpty()));
		accordion.add(GraphUtil.makeBarChart("groupsize", "Group sizes",
				createGroupSizeChart(groups)).setVisible(!users.isEmpty()));

		accordion.add(new ListView<GamingGroup>("groups", ModelMaker
				.wrap(groups)) {
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

		add(accordion);
	}

	private ListModel<BaseSeries<String, Integer>> createGroupSizeChart(
			List<GamingGroup> groups) {

		BaseSeries<String, Integer> series = new BaseSeries<String, Integer>();

		for (GamingGroup group : groups) {
			series.addEntry(group.getName(), group.getGroupMembers().size());
		}
		List<BaseSeries<String, Integer>> res = new LinkedList<BaseSeries<String, Integer>>();
		res.add(series);

		return new ListModel<BaseSeries<String, Integer>>(res);
	}

	/**
	 	 */
	private ListModel<BaseSeries<String, Integer>> createCompositionChart(
			List<User> users) {
		BaseSeries<String, Integer> series = new BaseSeries<String, Integer>();

		Map<Rank, Integer> count = new HashMap<Rank, Integer>();

		for (User user : users) {
			if (count.containsKey(user.getRank())) {
				count.put(user.getRank(), count.get(user.getRank()) + 1);
			} else {
				count.put(user.getRank(), 1);
			}
		}

		for (Entry<Rank, Integer> e : count.entrySet()) {
			series.addEntry(e.getKey().toString(), e.getValue());
		}

		List<BaseSeries<String, Integer>> res = new LinkedList<BaseSeries<String, Integer>>();
		res.add(series);

		return new ListModel<BaseSeries<String, Integer>>(res);
	}
}
