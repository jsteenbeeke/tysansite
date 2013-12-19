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

import java.util.Date;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.UntenabilityVote;

/**
 * @author Jeroen Steenbeeke
 */
public class UntenabilityVoteFilter extends SearchFilter<UntenabilityVote> {
	private static final long serialVersionUID = 1L;

	private Date startBefore;

	private IModel<Regulation> regulation = new Model<Regulation>();

	public Date getStartBefore() {
		return startBefore;
	}

	public void setStartBefore(Date startBefore) {
		this.startBefore = startBefore;
	}

	/**
	 * @return the regulation
	 */
	public Regulation getRegulation() {
		return regulation.getObject();
	}

	/**
	 * @param regulation
	 *            the regulation to set
	 */
	public void setRegulation(Regulation regulation) {
		this.regulation = ModelMaker.wrap(regulation);
	}

	@Override
	public void detach() {
		super.detach();
		regulation.detach();
	}
}
