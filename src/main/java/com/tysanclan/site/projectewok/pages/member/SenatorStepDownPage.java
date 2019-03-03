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
import com.tysanclan.site.projectewok.components.IconLink;
import com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder;
import com.tysanclan.site.projectewok.entities.User;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author Jeroen Steenbeeke
 */
@TysanRankSecured(Rank.SENATOR)
public class SenatorStepDownPage extends AbstractMemberPage {
	private static final long serialVersionUID = 1L;

	public SenatorStepDownPage() {
		super("Step down");

		add(new IconLink.Builder("images/icons/tick.png",
				new DefaultClickResponder<User>(ModelMaker.wrap(getUser())) {
					private static final long serialVersionUID = 1L;

					@SpringBean
					private DemocracyService democracyService;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						democracyService.stepDownAsSenator(getUser());

						setResponsePage(new OverviewPage());
					}
				}).setText("Yes, I want to stop being a Senator")
				.newInstance("yes"));
		add(new IconLink.Builder("images/icons/cross.png",
				new DefaultClickResponder<User>(ModelMaker.wrap(getUser())) {
					private static final long serialVersionUID = 1L;

					/**
					 * @see com.tysanclan.site.projectewok.components.IconLink.DefaultClickResponder#onClick()
					 */
					@Override
					public void onClick() {
						setResponsePage(new OverviewPage());
					}
				}).setText("No, I do not want to step down").newInstance("no"));

	}
}
