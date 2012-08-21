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

import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Rank;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.User.CaseInsensitiveUserComparator;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.UserFilter;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class EditGameSupervisorPage extends TysanPage {
	private DropDownChoice<User> userSelect;

	@SpringBean
	private UserDAO userDAO;

	/**
     * 
     */
	public EditGameSupervisorPage(Game game) {
		super("Game Supervisor for " + game.getName());

		Form<Realm> form = new Form<Realm>("form",
		        new CompoundPropertyModel<Realm>(ModelMaker
		                .wrap(game))) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private RealmService realmService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				super.onSubmit();

				Realm realm = getModelObject();
				User user = userSelect.getModelObject();

				realmService.setSupervisor(realm, user);

				setResponsePage(new RealmManagementPage());
			}
		};

		UserFilter filter = new UserFilter();
		filter.addRank(Rank.CHANCELLOR);
		filter.addRank(Rank.SENATOR);
		filter.addRank(Rank.TRUTHSAYER);
		filter.addRank(Rank.REVERED_MEMBER);
		filter.addRank(Rank.SENIOR_MEMBER);
		filter.addRank(Rank.FULL_MEMBER);
		filter.addRank(Rank.JUNIOR_MEMBER);
		filter.setGame(game);

		List<User> users = userDAO.findByFilter(filter);
		Collections.sort(users,
		        new CaseInsensitiveUserComparator());

		userSelect = new DropDownChoice<User>(
		        "coordinator", users);

		form.add(userSelect);

		Accordion accordion = new Accordion("accordion");
		accordion.setHeader(new AccordionHeader(
		        new LiteralOption("h2")));
		accordion.setAutoHeight(false);

		accordion.add(new Label("title",
		        "Realm Supervisor for " + game.getName()));

		add(accordion);

		accordion.add(form);

		add(new Link<Void>("back") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new GameManagementPage());

			}
		});
	}
}
