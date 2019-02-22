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
package com.tysanclan.site.projectewok.model;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;

/**
 * @author Jeroen Steenbeeke
 */
public final class GameRealmCartesian implements IDetachable {
	private static final long serialVersionUID = 1L;

	private final IModel<Game> game;
	private final IModel<Realm> realm;

	private final int hash;

	public GameRealmCartesian(Game game, Realm realm) {
		this.game = ModelMaker.wrap(game);
		this.realm = ModelMaker.wrap(realm);
		this.hash = game.hashCode() + 31 * realm.hashCode();
	}

	public Game getGame() {
		return game.getObject();
	}

	public Realm getRealm() {
		return realm.getObject();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj == null) {
			return false;
		}

		if (obj.getClass() == GameRealmCartesian.class) {
			GameRealmCartesian other = (GameRealmCartesian) obj;

			return other.getGame().equals(getGame())
					&& other.getRealm().equals(getRealm());
		}

		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hash;

	}

	/**
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
		game.detach();
		realm.detach();
	}
}
