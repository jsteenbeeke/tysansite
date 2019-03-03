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
package com.tysanclan.site.projectewok.pages.member;

import com.jeroensteenbeeke.hyperion.solstice.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.entities.SenateElection;
import com.tysanclan.site.projectewok.entities.User;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.CHANCELLOR, Rank.SENATOR, Rank.TRUTHSAYER,
		Rank.REVERED_MEMBER, Rank.SENIOR_MEMBER, Rank.FULL_MEMBER,
		Rank.JUNIOR_MEMBER })
public class SenateElectionPage extends AbstractElectionPage<SenateElection> {
	private static final long serialVersionUID = 1L;

	private IModel<SenateElection> electionModel;

	@SpringBean
	private DemocracyService democracyService;

	/**
	 *
	 */
	public SenateElectionPage(SenateElection election) {
		super("Senate election", election);

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
	 * @see com.tysanclan.site.projectewok.pages.member.AbstractElectionPage#onVoteSubmit(java.util.List)
	 */
	@Override
	public void onVoteSubmit(List<User> userList) {
		SenateElection election = electionModel.getObject();

		democracyService.createVote(getUser(), userList, election);

		setResponsePage(new OverviewPage());

	}

	/**
	 * @see com.tysanclan.site.projectewok.pages.member.AbstractElectionPage#getInternetExplorerAlternativePage(com.tysanclan.site.projectewok.entities.Election)
	 */
	@Override
	public AbstractManualElectionPage getInternetExplorerAlternativePage(
			SenateElection election) {
		return new SenateManualElectionPage(election);
	}

}
