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

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionAnimated;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.FilterDataProvider;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.components.BasicMemberPanel;
import com.tysanclan.site.projectewok.components.ChancellorPanel;
import com.tysanclan.site.projectewok.components.GameSupervisorPanel;
import com.tysanclan.site.projectewok.components.GroupOverviewPanel;
import com.tysanclan.site.projectewok.components.HeraldPanel;
import com.tysanclan.site.projectewok.components.MentorPanel;
import com.tysanclan.site.projectewok.components.PupilPanel;
import com.tysanclan.site.projectewok.components.RealmSupervisorPanel;
import com.tysanclan.site.projectewok.components.SenatorPanel;
import com.tysanclan.site.projectewok.components.StewardPanel;
import com.tysanclan.site.projectewok.components.TreasurerPanel;
import com.tysanclan.site.projectewok.components.TruthsayerPanel;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Group;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.GameDAO;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.GameFilter;
import com.tysanclan.site.projectewok.entities.dao.filters.RealmFilter;

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

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
		accordion.setAnimated(new AccordionAnimated("slide"));
		accordion.setAutoHeight(false);
		accordion.getOptions().put("heightStyle", "'content'");

		accordion.add(new MentorPanel("pupils", getUser()));
		accordion.add(new PupilPanel("mentor", getUser()));

		accordion.add(new BasicMemberPanel("basicpanel", getUser()));
		accordion.add(new TruthsayerPanel("truthsayerpanel")
				.setVisible(getUser().getRank().equals(Rank.TRUTHSAYER)));
		accordion.add(new SenatorPanel("senatorpanel").setVisible(getUser()
				.getRank().equals(Rank.SENATOR)));
		accordion.add(new ChancellorPanel("chancellorpanel")
				.setVisible(getUser().getRank().equals(Rank.CHANCELLOR)));
		accordion.add(new TreasurerPanel("treasurerpanel", getUser()));
		accordion.add(new StewardPanel("stewardpanel", getUser()));
		accordion.add(new HeraldPanel("heraldpanel", getUser()));

		User user = getUser();
		List<Group> groups = new LinkedList<Group>();
		groups.addAll(user.getGroups());

		accordion.add(new ListView<Group>("groups", ModelMaker.wrap(groups)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Group> item) {
				Group group = item.getModelObject();

				item.add(new GroupOverviewPanel("grouppanel", getUser(), group));

			}
		});

		GameFilter gfilter = new GameFilter();
		gfilter.setCoordinator(user);

		accordion.add(new DataView<Game>("games", FilterDataProvider.of(
				gfilter, gameDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Game> item) {
				Game game = item.getModelObject();

				item.add(new GameSupervisorPanel("supervisorpanel", game));

			}
		});

		RealmFilter rfilter = new RealmFilter();
		rfilter.setOverseer(user);

		accordion.add(new DataView<Realm>("realms", FilterDataProvider.of(
				rfilter, realmDAO)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Realm> item) {
				Realm realm = item.getModelObject();

				item.add(new RealmSupervisorPanel("supervisorpanel", realm));

			}
		});

		add(accordion);
	}
}
