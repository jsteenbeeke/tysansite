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
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.twitter.Tweet;

/**
 * @author Jeroen Steenbeeke
 */
public class TweetFilter extends SearchFilter<Tweet> {

	private static final long serialVersionUID = 1L;

	private boolean searchUserNull = false;

	private IModel<User> user = new Model<User>();

	private String contents;

	public void setSearchUserNull(boolean searchUserNull) {
		this.searchUserNull = searchUserNull;
	}

	public boolean isSearchUserNull() {
		return searchUserNull;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public User getUser() {
		return user.getObject();
	}

	public void setUser(User user) {
		this.user = ModelMaker.wrap(user);
	}

	@Override
	public void detach() {
		super.detach();
		user.detach();
	}

}
