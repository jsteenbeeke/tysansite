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

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.pages.member.DeadRealmRemovalPage;
import com.tysanclan.site.projectewok.pages.member.GamingGroupSupervisionPage;

/**
 * @author Jeroen Steenbeeke
 */
public class GameSupervisorPanel extends TysanOverviewPanel<Game> {
	public class DeadRealmCondition implements IRequiresAttentionCondition {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private Realm getDeadRealm() {
			Game game = getModelObject();

			for (Realm realm : game.getRealms()) {
				if (gameService.countPlayers(game, realm) < gameService
						.getRequiredPetitionSignatures()) {
					return realm;
				}

			}

			return null;
		}

		@Override
		public AttentionType requiresAttention() {
			if (getDeadRealm() != null) {
				return AttentionType.WARNING;
			}

			return null;
		}

		@Override
		public Long getDismissableId() {
			Realm realm = getDeadRealm();

			return realm != null ? realm.getId() : null;
		}
	}

	private static final long serialVersionUID = 1L;

	@SpringBean
	private GameService gameService;

	/**
	 * 
	 */
	public GameSupervisorPanel(String id, Game game) {
		super(id, ModelMaker.wrap(game), "Game Supervision: " + game.getName());

		add(createLink("deadrealm", ModelMaker.wrap(game),
				DeadRealmRemovalPage.class, "Remove Dead Realms",
				new DeadRealmCondition()));

		add(new Link<Game>("groups", ModelMaker.wrap(game)) {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new GamingGroupSupervisionPage(getModelObject()));

			}
		});
	}

}
