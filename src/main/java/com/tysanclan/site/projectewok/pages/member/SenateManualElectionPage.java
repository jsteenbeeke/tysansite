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
package com.tysanclan.site.projectewok.pages.member;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class SenateManualElectionPage extends
        AbstractManualElectionPage {

	@SpringBean
	private DemocracyService democracyService;

	private IModel<SenateElection> electionModel;

	/**
     * 
     */
	public SenateManualElectionPage(SenateElection election) {
		super("Senate Election", election);
		electionModel = ModelMaker.wrap(election);
	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		electionModel.detach();
	}

	/**
	 * @see com.tysanclan.site.projectewok.pages.member.AbstractManualElectionPage#onVoteSubmit(java.util.List)
	 */
	@Override
	public void onVoteSubmit(List<User> userList) {
		SenateElection election = electionModel.getObject();

		democracyService.createVote(getUser(), userList,
		        election);

		setResponsePage(new OverviewPage());

	}

}
