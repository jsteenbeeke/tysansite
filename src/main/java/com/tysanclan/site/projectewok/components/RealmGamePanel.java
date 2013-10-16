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
package com.tysanclan.site.projectewok.components;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.GamingGroup;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.UserGameRealmDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserGameRealmFilter;
import com.tysanclan.site.projectewok.util.ImageUtil;
import com.tysanclan.site.projectewok.util.MemberUtil;

/**
 * @author Jeroen Steenbeeke
 */
public class RealmGamePanel extends Panel {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private UserGameRealmDAO userGameRealmDAO;

	/**
	 * 
	 */
	public RealmGamePanel(String id, Realm realm, Game game) {
		super(id);

		Accordion accordion = new Accordion("accordion");
		accordion.getOptions().put("heightStyle", "content");
		accordion.setAutoHeight(false);
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));

		add(accordion);

		accordion.add(new Label("name", game.getName()));
		accordion.add(new Image("icon", new ByteArrayResource(ImageUtil
				.getMimeType(game.getImage()), game.getImage())));

		UserGameRealmFilter filter = new UserGameRealmFilter();
		filter.setRealm(realm);
		filter.setGame(game);

		List<UserGameRealm> ugrs = new LinkedList<UserGameRealm>();
		ugrs.addAll(userGameRealmDAO.findByFilter(filter));
		Set<UserGameRealm> removeUGR = new HashSet<UserGameRealm>();

		for (UserGameRealm ugr : ugrs) {
			if (!MemberUtil.isMember(ugr.getUser())) {
				removeUGR.add(ugr);
			}
		}

		ugrs.removeAll(removeUGR);

		if (ugrs.isEmpty()) {
			setVisible(false);
		}

		Collections.sort(ugrs, new Comparator<UserGameRealm>() {
			/**
			 * @see java.util.Comparator#compare(java.lang.Object,
			 *      java.lang.Object)
			 */
			@Override
			public int compare(UserGameRealm o1, UserGameRealm o2) {
				return o1.getUser().getUsername()
						.compareToIgnoreCase(o2.getUser().getUsername());
			}
		});

		accordion.add(new Label("count", new Model<Integer>(ugrs.size())));

		accordion.add(new ListView<UserGameRealm>("members", ModelMaker
				.wrap(ugrs)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<UserGameRealm> item) {
				UserGameRealm ugr = item.getModelObject();
				item.add(new MemberListItem("user", ugr.getUser()));

				StringBuilder builder = new StringBuilder();

				for (GameAccount account : ugr.getAccounts()) {
					if (builder.length() > 0) {
						builder.append(", ");
					}
					builder.append(account.toString());
				}

				if (builder.length() == 0) {
					builder.append('-');
				}

				item.add(new Label("accounts", builder.toString()));
			}
		});

		List<GamingGroup> groups = new LinkedList<GamingGroup>();
		groups.addAll(realm.getGroups());
		groups.retainAll(game.getGroups());

		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setVisible(!groups.isEmpty());

		accordion.add(container);

		container.add(new ListView<GamingGroup>("gaminggroups", ModelMaker
				.wrap(groups)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<GamingGroup> item) {
				GamingGroup gg = item.getModelObject();

				item.add(new AutoGroupLink("grouplink", gg));

			}
		});

	}
}
