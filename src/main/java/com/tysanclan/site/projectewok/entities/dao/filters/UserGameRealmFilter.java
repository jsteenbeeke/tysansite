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
package com.tysanclan.site.projectewok.entities.dao.filters;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.UserGameRealm;

/**
 * @author Jeroen Steenbeeke
 */
public class UserGameRealmFilter extends SearchFilter<UserGameRealm> {

	private static final long serialVersionUID = 1L;

	private IModel<User> user = new Model<User>();

	private IModel<Game> game = new Model<Game>();

	private IModel<Realm> realm = new Model<Realm>();

	public Game getGame() {
		return game.getObject();
	}

	public void setGame(Game game) {
		this.game = ModelMaker.wrap(game);
	}

	public Realm getRealm() {
		return realm.getObject();
	}

	public void setRealm(Realm realm) {
		this.realm = ModelMaker.wrap(realm);
	}

	public User getUser() {
		return user.getObject();
	}

	public void setUser(User user) {
		this.user = ModelMaker.wrap(user);
	}

}
