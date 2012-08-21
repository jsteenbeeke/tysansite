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
package com.tysanclan.site.projectewok.beans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.dao.SenateElectionDAO;

/**
 * @author Jeroen Steenbeeke
 */
@Component
@Scope("request")
class WrapperServiceImpl implements
		com.tysanclan.site.projectewok.beans.WrapperService {

	@Autowired
	private SenateElectionDAO senateElectionDAO;

	/**
	 * @param senateElectionDAO
	 *            the senateElectionDAO to set
	 */
	public void setSenateElectionDAO(SenateElectionDAO senateElectionDAO) {
		this.senateElectionDAO = senateElectionDAO;
	}

	/**
	 * @see com.tysanclan.site.projectewok.beans.WrapperService#countElectionWinner(com.tysanclan.site.projectewok.entities.SenateElection)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int countElectionWinner(SenateElection senateElection) {
		SenateElection _election = senateElectionDAO.load(senateElection
				.getId());

		return _election.getWinners().size();
	}

}
