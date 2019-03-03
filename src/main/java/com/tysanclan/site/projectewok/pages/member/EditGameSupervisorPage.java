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
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.User.CaseInsensitiveUserComparator;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.entities.dao.UserDAO;
import com.tysanclan.site.projectewok.entities.filter.UserFilter;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.CHANCELLOR)
public class EditGameSupervisorPage extends TysanPage {
	private static final long serialVersionUID = 1L;

	private DropDownChoice<User> userSelect;

	@SpringBean
	private UserDAO userDAO;

	/**
	 *
	 */
	public EditGameSupervisorPage(Game game) {
		super("Game Supervisor for " + game.getName());

		Form<Game> form = new Form<Game>("form",
				new CompoundPropertyModel<Game>(ModelMaker.wrap(game))) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private GameService gameService;

			/**
			 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
			 */
			@Override
			protected void onSubmit() {
				super.onSubmit();

				Game game = getModelObject();
				User user = userSelect.getModelObject();

				gameService.setGameSupervisor(game, user);

				setResponsePage(new GameManagementPage());
			}
		};

		UserFilter filter = new UserFilter();
		filter.rank(Rank.CHANCELLOR);
		filter.orRank(Rank.SENATOR);
		filter.orRank(Rank.TRUTHSAYER);
		filter.orRank(Rank.REVERED_MEMBER);
		filter.orRank(Rank.SENIOR_MEMBER);
		filter.orRank(Rank.FULL_MEMBER);
		filter.orRank(Rank.JUNIOR_MEMBER);

		List<User> users = userDAO.findByFilter(filter)
				.filter(u -> u.getPlayedGames().stream()
						.map(UserGameRealm::getGame).anyMatch(game::equals))
				.toJavaList();
		users.sort(new CaseInsensitiveUserComparator());

		userSelect = new DropDownChoice<>("coordinator", users);

		form.add(userSelect);

		add(new Label("pagetitle", "Realm Supervisor for " + game.getName()));

		add(form);

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
