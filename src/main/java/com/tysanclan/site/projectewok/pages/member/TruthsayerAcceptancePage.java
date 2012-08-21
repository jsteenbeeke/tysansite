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

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.LawEnforcementService;
import com.tysanclan.site.projectewok.entities.TruthsayerNomination;
import com.tysanclan.site.projectewok.entities.dao.TruthsayerNominationDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.TruthsayerNominationFilter;
import com.tysanclan.site.projectewok.pages.AccessDeniedPage;

/**
 * @author Jeroen Steenbeeke
 */
public class TruthsayerAcceptancePage extends AbstractMemberPage {
	@SpringBean
	private TruthsayerNominationDAO truthSayerNominationDAO;

	private IModel<TruthsayerNomination> nominationModel;

	/**
	 * 
	 */
	public TruthsayerAcceptancePage() {
		super("Truthsayer Invitation");

		TruthsayerNominationFilter filter = new TruthsayerNominationFilter();
		filter.setNominee(getUser());
		filter.setStartNotSet(true);

		List<TruthsayerNomination> nominations = truthSayerNominationDAO
				.findByFilter(filter);

		if (nominations.isEmpty()) {
			throw new RestartResponseAtInterceptPageException(
					AccessDeniedPage.class);
		}

		nominationModel = ModelMaker.wrap(nominations.get(0));

		Link<TruthsayerNomination> yesLink = new Link<TruthsayerNomination>(
				"yes", ModelMaker.wrap(getNomination())) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private LawEnforcementService lawEnforcementService;

			@Override
			public void onClick() {
				lawEnforcementService
						.acceptTruthsayerNomination(getModelObject());
				setResponsePage(new OverviewPage());

			}

		};

		yesLink.add(new ContextImage("icon", "images/icons/tick.png"));

		add(yesLink);

		Link<TruthsayerNomination> noLink = new Link<TruthsayerNomination>(
				"no", ModelMaker.wrap(getNomination())) {
			private static final long serialVersionUID = 1L;

			@SpringBean
			private LawEnforcementService lawEnforcementService;

			@Override
			public void onClick() {
				lawEnforcementService
						.declineTruthsayerNomination(getModelObject());
				setResponsePage(new OverviewPage());

			}

		};

		noLink.add(new ContextImage("icon", "images/icons/cross.png"));

		add(noLink);

	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		nominationModel.detach();
	}

	private TruthsayerNomination getNomination() {
		return nominationModel.getObject();
	}
}
