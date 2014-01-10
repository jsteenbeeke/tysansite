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

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.rest.api.data.Rank;
import com.tysanclan.site.projectewok.auth.TysanRankSecured;
import com.tysanclan.site.projectewok.beans.DemocracyService;
import com.tysanclan.site.projectewok.entities.ChancellorElection;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured({ Rank.CHANCELLOR, Rank.SENATOR, Rank.TRUTHSAYER,
		Rank.REVERED_MEMBER, Rank.SENIOR_MEMBER, Rank.FULL_MEMBER,
		Rank.JUNIOR_MEMBER })
public class ChancellorElectionPage extends
		AbstractElectionPage<ChancellorElection> {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private DemocracyService democracyService;

	private IModel<ChancellorElection> electionModel;

	public ChancellorElectionPage(ChancellorElection election) {
		super("Chancellor Election", election);

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
		ChancellorElection election = electionModel.getObject();

		democracyService.createVote(getUser(), userList, election);

		setResponsePage(new OverviewPage());

	}

	/**
	 * @see com.tysanclan.site.projectewok.pages.member.AbstractElectionPage#getInternetExplorerAlternativePage(com.tysanclan.site.projectewok.entities.Election)
	 */
	@Override
	public AbstractManualElectionPage getInternetExplorerAlternativePage(
			ChancellorElection election) {
		return new ChancellorManualElectionPage(election);
	}

}
