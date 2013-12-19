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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.components.RealmGamePanel;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class RealmPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private RealmDAO realmDAO;

	@SpringBean
	private RealmService realmService;

	private IModel<Realm> realmModel;

	public RealmPage(PageParameters params) {
		super("Realm overview");

		Long id = Long.parseLong(params.get("id").toString());

		init(realmDAO.load(id));
	}

	public void init(Realm realm) {
		setPageTitle("Realm overview - " + realm.getName());

		add(new BookmarkablePageLink<Void>("back", AboutPage.class));

		realmModel = ModelMaker.wrap(realm);

		add(new Label("name", realm.getName()));

		if (realm.getOverseer() != null) {
			add(new MemberListItem("supervisor", realm.getOverseer()));
		} else {
			add(new Label("supervisor", "-"));
		}
		add(new Label("playercount", new Model<Integer>(
				realmService.countActivePlayers(realm))));

		add(new ListView<Game>("games", ModelMaker.wrap(realm.getGames())) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
			@Override
			protected void populateItem(ListItem<Game> item) {
				Game game = item.getModelObject();
				Realm rlm = getRealm();

				item.add(new RealmGamePanel("game", rlm, game));
			}
		});
	}

	public Realm getRealm() {
		return realmModel.getObject();
	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		realmModel.detach();
	}

}
