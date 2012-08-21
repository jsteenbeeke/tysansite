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

import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.Activation;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class ActivationFilter extends SearchFilter<Activation> {
	private static final long serialVersionUID = 1L;

	private IModel<User> user = new Model<User>();
	private String key;
	private Date dateBefore;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user.getObject();
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = ModelMaker.wrap(user);
	}

	/**
	 * @return the dateBefore
	 */
	public Date getDateBefore() {
		return dateBefore;
	}

	/**
	 * @param dateBefore
	 *            the dateBefore to set
	 */
	public void setDateBefore(Date dateBefore) {
		this.dateBefore = dateBefore;
	}

	@Override
	public void detach() {
		super.detach();
		if (user != null)
			user.detach();
	}

}
