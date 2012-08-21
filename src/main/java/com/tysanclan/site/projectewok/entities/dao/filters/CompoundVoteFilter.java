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
import com.tysanclan.site.projectewok.entities.CompoundVote;
import com.tysanclan.site.projectewok.entities.Election;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class CompoundVoteFilter extends SearchFilter<CompoundVote> {
	private static final long serialVersionUID = 1L;

	private IModel<Election> election = new Model<Election>();
	private IModel<User> voter = new Model<User>();

	/**
	 * @return the election
	 */
	public Election getElection() {
		return election.getObject();
	}

	/**
	 * @param election
	 *            the election to set
	 */
	public void setElection(Election election) {
		this.election = ModelMaker.wrap(election);
	}

	/**
	 * @return the voter
	 */
	public User getVoter() {
		return voter.getObject();
	}

	/**
	 * @param voter
	 *            the voter to set
	 */
	public void setVoter(User voter) {
		this.voter = ModelMaker.wrap(voter);
	}

	@Override
	public void detach() {
		super.detach();
		voter.detach();
		election.detach();
	}

}
