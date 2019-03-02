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
package com.tysanclan.site.projectewok.components;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.pages.member.DeadRealmRemovalPage;
import com.tysanclan.site.projectewok.pages.member.GamingGroupSupervisionPage;
import com.tysanclan.site.projectewok.util.GraphUtil;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jeroen Steenbeeke
 */
public class RealmSupervisorPanel extends TysanOverviewPanel<Realm> {

	public class DeadRealmsCondition implements IRequiresAttentionCondition {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public Game getDeadGame() {
			Realm realm = getModelObject();

			for (Game game : realm.getGames()) {
				if (gameService.countPlayers(game, realm) < gameService
						.getRequiredPetitionSignatures()) {
					return game;
				}

			}

			return null;
		}

		@Override
		public AttentionType requiresAttention() {
			Game deadGame = getDeadGame();

			if (deadGame != null) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			Game deadGame = getDeadGame();

			if (deadGame != null) {
				return deadGame.getId();
			}

			return null;
		}
	}

	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	public RealmSupervisorPanel(String id, Realm realm) {
		super(id, ModelMaker.wrap(realm),
				"Realm Supervision: " + realm.getName());

		add(GraphUtil.makePieChart("gamesize", "Game percentages",
				createGameSizeChart(realm)));

		add(createLink("deadrealm", ModelMaker.wrap(realm),
				DeadRealmRemovalPage.class, "Remove Dead Realms",
				new DeadRealmsCondition()));
		add(new Link<Realm>("groups", ModelMaker.wrap(realm)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(
						new GamingGroupSupervisionPage(getModelObject()));

			}
		});
	}

	private Map<String, Integer> createGameSizeChart(Realm realm) {

		Map<String, Integer> series = new HashMap<>();

		for (Game game : realm.getGames()) {
			int playerSize = game.getPlayers().size();
			if (playerSize > 0) {
				series.put(game.getName(), playerSize);
			}
		}

		return series;
	}
}
