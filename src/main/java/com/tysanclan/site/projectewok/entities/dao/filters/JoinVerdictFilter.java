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
package com.tysanclan.site.projectewok.entities.dao.filters;

import org.apache.wicket.model.IModel;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.JoinVerdict;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class JoinVerdictFilter extends SearchFilter<JoinVerdict> {
	private static final long serialVersionUID = 1L;

	private IModel<User> senator;
	private IModel<JoinApplication> joinApplication;

	/**
	 * @return the senator
	 */
	public User getSenator() {
		return senator.getObject();
	}

	/**
	 * @param senator
	 *            the senator to set
	 */
	public void setSenator(User senator) {
		this.senator = ModelMaker.wrap(senator);
	}

	/**
	 * @return the joinApplication
	 */
	public JoinApplication getJoinApplication() {
		return joinApplication.getObject();
	}

	/**
	 * @param joinApplication
	 *            the joinApplication to set
	 */
	public void setJoinApplication(JoinApplication joinApplication) {
		this.joinApplication = ModelMaker.wrap(joinApplication);
	}

	@Override
	public void detach() {
		super.detach();
		senator.detach();
		joinApplication.detach();
	}
}
