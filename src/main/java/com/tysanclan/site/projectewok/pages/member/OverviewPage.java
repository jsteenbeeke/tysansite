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

import com.jeroensteenbeeke.hyperion.solstice.data.FilterDataProvider;
import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.components.*;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;
import com.tysanclan.site.projectewok.entities.filter.GameFilter;
import com.tysanclan.site.projectewok.entities.filter.RealmFilter;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class OverviewPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RealmDAO realmDAO;

	@SpringBean
	private GameDAO gameDAO;

	/**
	 *
	 */
	public OverviewPage() {
		super("Member overview");

		add(new MentorPanel("pupils", getUser()));
		add(new PupilPanel("mentor", getUser()));

		add(new BasicMemberPanel("basicpanel", getUser()));
		add(new TruthsayerPanel("truthsayerpanel")
				.setVisible(getUser().getRank().equals(Rank.TRUTHSAYER)));
		add(new SenatorPanel("senatorpanel")
				.setVisible(getUser().getRank().equals(Rank.SENATOR)));
		add(new ChancellorPanel("chancellorpanel")
				.setVisible(getUser().getRank().equals(Rank.CHANCELLOR)));
		add(new TreasurerPanel("treasurerpanel", getUser()));
		add(new StewardPanel("stewardpanel", getUser()));
		add(new HeraldPanel("heraldpanel", getUser()));

		User user = getUser();
		List<Group> groups = new LinkedList<Group>();
		groups.addAll(user.getGroups());

		add(new ListView<Group>("groups", ModelMaker.wrap(groups)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Group> item) {
				Group group = item.getModelObject();

				item.add(
						new GroupOverviewPanel("grouppanel", getUser(), group));

			}
		});

		GameFilter gfilter = new GameFilter();
		gfilter.coordinator(user);

		add(new DataView<Game>("games",
				FilterDataProvider.of(gfilter, gameDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Game> item) {
				Game game = item.getModelObject();

				item.add(new GameSupervisorPanel("supervisorpanel", game));

			}
		});

		RealmFilter rfilter = new RealmFilter();
		rfilter.overseer(user);

		add(new DataView<Realm>("realms",
				FilterDataProvider.of(rfilter, realmDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Realm> item) {
				Realm realm = item.getModelObject();

				item.add(new RealmSupervisorPanel("supervisorpanel", realm));

			}
		});
	}
}
